package com.example.joshmitchell.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity{

    public static final String EXTRA_NOTE_ID = "todo_id";

    public static Intent newIntent(Context packageContext, long id) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        long Id = getIntent()
                .getLongExtra(EXTRA_NOTE_ID, -1);
        Log.d("NoteActivity", String.valueOf(Id));

        return EditNoteFragment.newInstance(Id);
    }
}
