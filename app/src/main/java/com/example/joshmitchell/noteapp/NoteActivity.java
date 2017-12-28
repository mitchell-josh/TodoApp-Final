package com.example.joshmitchell.noteapp;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID)getIntent()
                .getSerializableExtra(EditNoteFragment.EXTRA_CRIME_ID);

        return EditNoteFragment.newInstance(noteId);
    }
}
