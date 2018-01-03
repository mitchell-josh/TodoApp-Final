package com.example.joshmitchell.noteapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.joshmitchell.noteapp.Fragment.NoteListFragment;
import com.example.joshmitchell.noteapp.R;
import com.example.joshmitchell.noteapp.Fragment.ViewPagerFragment;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListActivity extends SingleFragmentActivity implements NoteListFragment.OnEditSelectedListener {


    public void onEditSelected(long noteId){
        //Do something here
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putLong(ViewPagerFragment.EXTRA_NOTE_ID, noteId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
