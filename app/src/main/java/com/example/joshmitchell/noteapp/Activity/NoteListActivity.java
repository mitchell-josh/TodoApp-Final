package com.example.joshmitchell.noteapp.Activity;

import android.content.Context;
import android.content.Intent;
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

    public static final String LIST_FILTER = "todo_id";

    public static Intent newIntent(Context packageContext, int listFilter) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(LIST_FILTER, listFilter);
        return intent;
    }

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
        int listFilter = getIntent().getIntExtra(LIST_FILTER, -1);

        return NoteListFragment.newInstance(listFilter);
    }
}
