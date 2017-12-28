package com.example.joshmitchell.noteapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class EditNoteFragment extends Fragment {

    private Note mNote;
    private EditText mTitleField;

    public static final String EXTRA_NOTE_ID =
            "com.example.joshmitchell.noteapp.crime_id";

    public static EditNoteFragment newInstance(UUID noteId){
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_NOTE_ID, noteId);

        EditNoteFragment fragment = new EditNoteFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
        mNote = NoteModel.get(getActivity()).getTextNote(noteId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_textnote, parent, false);

        mTitleField = v.findViewById(R.id.textnote_title);
        mTitleField.setText(mNote.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Left blank
            }
        });

        return v;
    }
}
