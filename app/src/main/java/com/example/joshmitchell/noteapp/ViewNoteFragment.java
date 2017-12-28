package com.example.joshmitchell.noteapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Josh Mitchell on 28/12/2017.
 */

public class ViewNoteFragment extends Fragment {

    private Note mNote;
    private TextView mTitleField, mDateField, mDateDetailField, mContentField;
    private int mCheckId, mUncheckId;

    public static final String EXTRA_NOTE_ID = "com.example.joshmitchell.noteapp.note_id";


    public static ViewNoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_NOTE_ID, noteId);

        ViewNoteFragment fragment = new ViewNoteFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
        mNote = NoteModel.get(getActivity()).getTextNote(noteId);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_view, menu);

        MenuItem checkedItem = menu.findItem(R.id.checked);
        mCheckId = checkedItem.getItemId();

        MenuItem uncheckedItem = menu.findItem(R.id.unchecked);
        mUncheckId = uncheckedItem.getItemId();

        //Initial conditional checks for check/uncheck box
        if(mNote.getArchived() == false) {
            uncheckedItem.setVisible(false);
            checkedItem.setVisible(true);
        }
        if(mNote.getArchived() == true) {
            checkedItem.setVisible(false);
            uncheckedItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_note:
                Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
                startActivity(intent);

                return true;

            case R.id.checked:
                mNote.setArchived(true);
                item.setVisible(false);
                System.out.println(item);

                return true;

            case R.id.unchecked:
                mNote.setArchived(false);
                item.setVisible(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_view, parent, false);

        mTitleField = v.findViewById(R.id.view_title);
        mTitleField.setText(mNote.getTitle());

        mDateField = v.findViewById(R.id.view_date);
        mDateField.setText(DateFormat.getPatternInstance(DateFormat.ABBR_MONTH_DAY)
                .format(mNote.getDate()));

        mDateDetailField = v.findViewById(R.id.view_date_detail);
        mDateDetailField.setText(mNote.getDate().toString());

        mContentField = v.findViewById(R.id.view_content);
        mContentField.setText(mNote.getContent());

        return v;
    }
}
