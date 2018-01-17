package com.example.joshmitchell.noteapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.Fragment.ViewNoteFragment;
import com.example.joshmitchell.noteapp.Model.Note;

/**
 * Created by Josh Mitchell on 17/01/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    DatabaseHelper.NoteCursor mNoteCursor;

    public PagerAdapter(FragmentManager fm, DatabaseHelper.NoteCursor cursor){
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
        Log.d("DatabaseHelper", "The Id Is: (ViewPagerFragment) " + String.valueOf(t.getId()));
        return ViewNoteFragment.newInstance(t.getId());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
