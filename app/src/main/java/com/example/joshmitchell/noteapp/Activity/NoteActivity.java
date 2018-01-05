package com.example.joshmitchell.noteapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.joshmitchell.noteapp.Fragment.EditNoteFragment;

public class NoteActivity extends SingleFragmentActivity {

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

        if ( Id != -1){
            Log.d("NoteActivity", "Existing Note");
            return EditNoteFragment.newInstance(Id);
        }
        else{
            Log.d("NoteActivity", "New Note");
            return new EditNoteFragment();
        }
    }
}
