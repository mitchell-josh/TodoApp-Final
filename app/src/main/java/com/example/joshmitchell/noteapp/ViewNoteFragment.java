package com.example.joshmitchell.noteapp;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Josh Mitchell on 28/12/2017.
 */

public class ViewNoteFragment extends Fragment {

    private Note mNote;
    private TextView mTitleField, mDateField, mDateDetailField;

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

        return v;
    }
}
