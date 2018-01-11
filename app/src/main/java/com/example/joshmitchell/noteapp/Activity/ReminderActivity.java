package com.example.joshmitchell.noteapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Fragment.ReminderFragment;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 10/01/2018.
 */

public class ReminderActivity extends SingleFragmentActivity {

    public static final String EXTRA_NOTE_ID = "ReminderActivity.java";

    public static Intent newIntent(Context packageContext, long id) {
        Intent intent = new Intent(packageContext, ReminderActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        long Id = getIntent()
                .getLongExtra(EXTRA_NOTE_ID, -1);

        return ReminderFragment.newInstance(Id);
    }
}
