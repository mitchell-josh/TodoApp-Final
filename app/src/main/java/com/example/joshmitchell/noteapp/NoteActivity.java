package com.example.joshmitchell.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity{

    public static final String EXTRA_NOTE_ID = "todo_id";

    public static Intent newIntent(Context packageContext, UUID todoId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, todoId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID)getIntent()
                .getSerializableExtra(EditNoteFragment.EXTRA_NOTE_ID);

        return EditNoteFragment.newInstance(noteId);
    }
}
