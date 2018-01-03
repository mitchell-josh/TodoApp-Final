package com.example.joshmitchell.noteapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class EditNoteFragment extends Fragment {

    private NoteModel noteModel;
    private Note mNote;
    private EditText mTitleField, mContentField;

    public static final String EXTRA_NOTE_ID =
            "com.example.joshmitchell.noteapp.crime_id";

    public static EditNoteFragment newInstance(long noteId){
        Bundle args = new Bundle();

        args.putLong(EXTRA_NOTE_ID, noteId);

        EditNoteFragment fragment = new EditNoteFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteModel = noteModel.get(getActivity());
        Bundle args = getArguments();
        if (args != null){
            long noteId = args.getLong(EXTRA_NOTE_ID, -1);
            if( noteId != -1 ){
                mNote = noteModel.getTextNote(noteId);
            }
        }
        if (mNote == null) {
            mNote = new Note();
            noteModel.addNote(mNote);
        }
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

        mContentField = v.findViewById(R.id.textnote_content);
        mContentField.setText(mNote.getContent());
        mContentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
}