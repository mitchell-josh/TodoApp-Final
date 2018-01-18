package com.example.joshmitchell.noteapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.Adapter.PagerAdapter;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 01/01/2018.
 */

public class ViewPagerFragment extends Fragment {

    private DatabaseHelper.NoteCursor mCursor;
    public static final String EXTRA_NOTE_ID = "todo_id";
    private long noteId;
    PagerAdapter mAdapter;

    public static ViewPagerFragment newInstance(long noteId){
        Bundle args = new Bundle();

        args.putLong(EXTRA_NOTE_ID, noteId);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
        Log.d("ViewPagerFragment", "onResume Called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteId = getArguments().getLong(EXTRA_NOTE_ID);
        mCursor = NoteModel.get(getActivity()).queryNotes();
        mAdapter = new PagerAdapter(getChildFragmentManager(),
                (DatabaseHelper.NoteCursor) mCursor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_note_list_note_pager, container, false);

        ViewPager viewPager = v.findViewById(R.id.note_view_pager);
        viewPager.setAdapter(mAdapter);
        mCursor = NoteModel.get(getActivity()).queryNotes();
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToPosition(i);
            Note t = mCursor.getNote();
            if (t.getId() == noteId) {
                viewPager.setCurrentItem(i);
                break;
            }
        }

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        mCursor.close();
    }
}
