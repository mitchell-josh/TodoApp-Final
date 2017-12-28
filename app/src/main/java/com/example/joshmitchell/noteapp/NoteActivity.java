package com.example.joshmitchell.noteapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity implements ViewNoteFragment.OnEditSelectedListener {

    public void onEditSelected(UUID noteId){
        //Do something here
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(EditNoteFragment.EXTRA_NOTE_ID, noteId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID)getIntent()
                .getSerializableExtra(ViewNoteFragment.EXTRA_NOTE_ID);

        return ViewNoteFragment.newInstance(noteId);
    }
}
