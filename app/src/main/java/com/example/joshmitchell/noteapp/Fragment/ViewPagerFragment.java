package com.example.joshmitchell.noteapp.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;
import com.example.joshmitchell.noteapp.SQLiteCursorLoader;

import java.util.ArrayList;

/**
 * Created by Josh Mitchell on 01/01/2018.
 */

public class ViewPagerFragment extends Fragment {

    private DatabaseHelper.NoteCursor mCursor;
    public static final String EXTRA_NOTE_ID = "todo_id";
    private long noteId;
    MyAdapter adapter;

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
        Log.d("ViewPagerFragment", "onResume Called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteId = getArguments().getLong(EXTRA_NOTE_ID);
        Log.d("ViewPagerFragment", "onCreate Called");
        mCursor = NoteModel.get(getActivity()).queryRuns();
        adapter = new MyAdapter(getChildFragmentManager(),
                (DatabaseHelper.NoteCursor) mCursor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d("ViewPagerFragment", "onCreateView Called");


        View v = inflater.inflate(R.layout.activity_note_list_note_pager, container, false);

        ViewPager viewPager = v.findViewById(R.id.note_view_pager);
        viewPager.setAdapter(adapter);
        mCursor = NoteModel.get(getActivity()).queryRuns();
        Log.d("ViewPagerFragment", String.valueOf(mCursor.getCount()));
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToPosition(i);
            Note t = mCursor.getNote();
            Log.d("ViewPagerFragment", String.valueOf(t.getId()));
            Log.d("ViewPagerFragment", String.valueOf(noteId));
            if (t.getId() == noteId) {
                viewPager.setCurrentItem(i);
                Log.d("ViewPagerFragment", "Set current item called");

                break;
            }
        }

        return v;
    }

    public class MyAdapter extends FragmentStatePagerAdapter {

        DatabaseHelper.NoteCursor mNoteCursor;

        public MyAdapter(FragmentManager fm, DatabaseHelper.NoteCursor cursor){
            super(fm);
            Log.d("ViewPagerFragment", "Super Called");

            mNoteCursor = cursor;
        }

        @Override
        public int getCount(){
            Log.d("ViewPagerFragment", "getCount Called");
            Log.d("ViewPagerFragment", String.valueOf(mNoteCursor.getCount()));
            return mNoteCursor.getCount();
        }

        public Fragment getItem(int position){
            mNoteCursor.moveToPosition(position);
            Note t = mNoteCursor.getNote();
            Log.d("ViewPagerFragment", "getItem Called");
            Log.d("ViewPagerFragment", String.valueOf(t.getId()));
            return ViewNoteFragment.newInstance(t.getId());
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
