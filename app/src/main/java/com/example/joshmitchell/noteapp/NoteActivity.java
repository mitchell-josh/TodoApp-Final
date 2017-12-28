package com.example.joshmitchell.noteapp;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID)getIntent()
                .getSerializableExtra(ViewNoteFragment.EXTRA_NOTE_ID);

        return ViewNoteFragment.newInstance(noteId);
    }
}
