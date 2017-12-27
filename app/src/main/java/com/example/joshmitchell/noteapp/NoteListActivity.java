package com.example.joshmitchell.noteapp;

import android.support.v4.app.Fragment;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
