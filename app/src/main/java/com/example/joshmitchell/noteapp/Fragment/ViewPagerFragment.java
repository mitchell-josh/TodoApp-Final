package com.example.joshmitchell.noteapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;

import java.util.ArrayList;

/**
 * Created by Josh Mitchell on 01/01/2018.
 */

public class ViewPagerFragment extends Fragment {

    private DatabaseHelper.NoteCursor mCursor;
    public static final String EXTRA_NOTE_ID = "todo_id";
    private long noteId;
    MyAdapter adapter;

    private ArrayList<Note> mNotes;

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
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteId = getArguments().getLong(EXTRA_NOTE_ID);
        mNotes = NoteModel.get(getActivity()).getTextNotes();
        adapter = new MyAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.activity_note_list_note_pager, container, false);

        ViewPager viewPager = v.findViewById(R.id.note_view_pager);
        viewPager.setAdapter(adapter);
        for (int i = 0; i < mNotes.size(); i++){
            if (mNotes.get(i).getId() == noteId) {
                viewPager.setCurrentItem(i);
                break;
            }
        }

        return v;
    }

    public class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return mNotes.size();
        }

        public Fragment getItem(int position){
            Note t = mNotes.get(position);
            return ViewNoteFragment.newInstance(t.getId());
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment fragment = (Fragment) object;
            for (int i = 0; i < getCount() ; i++){
                Fragment item = (Fragment) getItem(i);
                if(item.equals(fragment))
                    return POSITION_UNCHANGED;
            }
            return POSITION_NONE;
        }
    }
}
