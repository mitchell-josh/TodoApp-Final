package com.example.joshmitchell.noteapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.NoteLoader;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class EditNoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Note>{

    private NoteModel noteModel;
    private Note mNote;
    private EditText mTitleField, mContentField;
    private static final int LOAD_NOTE = 0;
    private long noteId;

    public static String EXTRA_NOTE_ID =
            "com.example.joshmitchell.noteapp.note_id";

    @Override
    public Loader<Note> onCreateLoader(int id, Bundle args){
        return new NoteLoader(getActivity(), args.getLong(EXTRA_NOTE_ID));
    }

    @Override
    public void onLoadFinished(Loader<Note> loader, Note note){
        mNote = note;
    }

    @Override
    public void onLoaderReset(Loader<Note> loader){
        //Do Nothing
    }

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
        Log.d("NoteID", String.valueOf(noteId));
        if (args != null){
            noteId = args.getLong(EXTRA_NOTE_ID, -1);
                Log.d("NoteID", String.valueOf(noteId));
            if( noteId != -1 ){
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_NOTE, args, new NoteLoaderCallbacks());
            }
        }

        if (args == null) {
            noteId = -1;
            mNote = new Note();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_textnote, parent, false);

        mTitleField = v.findViewById(R.id.textnote_title);
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

    @Override
    public void onPause(){
        super.onPause();
        if(noteId == -1)
            noteModel.addNote(mNote);
        noteModel.updateNote(mNote);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("EditNoteFragment", "FRAG STOPPED");

    }

    public void updateUI(){
        if(mNote != null){
            mTitleField.setText(mNote.getTitle());
            mContentField.setText(mNote.getContent());
        }

    }

    private class NoteLoaderCallbacks implements LoaderManager.LoaderCallbacks<Note> {

        @Override
        public Loader<Note> onCreateLoader(int id, Bundle args) {
            return new NoteLoader(getActivity(), args.getLong(EXTRA_NOTE_ID));
        }

        @Override
        public void onLoadFinished(Loader<Note> loader, Note data) {
            Log.d("EditNoteFragment", "Load Finished");

            if(data == null)
                Log.d("EditNoteFragment", "Load Finished NULL");

            mNote = data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Note> loader) {
            // Do Nothing
        }
    }
}
