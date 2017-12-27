package com.example.joshmitchell.noteapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID)getIntent()
                .getSerializableExtra(TextNoteFragment.EXTRA_CRIME_ID);

        return TextNoteFragment.newInstance(noteId);
    }
}
