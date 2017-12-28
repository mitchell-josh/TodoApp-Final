package com.example.joshmitchell.noteapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.UUID;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListActivity extends SingleFragmentActivity implements NoteListFragment.OnEditSelectedListener{

    public void onEditSelected(UUID noteId){
        //Do something here
        ViewNoteFragment fragment = new ViewNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ViewNoteFragment.EXTRA_NOTE_ID, noteId);
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
