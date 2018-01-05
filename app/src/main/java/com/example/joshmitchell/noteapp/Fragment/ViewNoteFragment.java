package com.example.joshmitchell.noteapp.Fragment;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joshmitchell.noteapp.Activity.NoteActivity;
import com.example.joshmitchell.noteapp.Activity.NoteListActivity;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.NoteLoader;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 28/12/2017.
 */

public class ViewNoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Note> {

    private Note mNote;
    private TextView mTitleField, mDateField, mDateDetailField, mContentField;
    private Menu menu;

    public static final int LOAD_NOTE = 0;
    public static final String EXTRA_NOTE_ID = "com.example.joshmitchell.noteapp.note_id";

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


    public static ViewNoteFragment newInstance(long noteId) {
        Bundle args = new Bundle();

        args.putLong(EXTRA_NOTE_ID, noteId);

        ViewNoteFragment fragment = new ViewNoteFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        long noteId = getArguments().getLong(EXTRA_NOTE_ID);
        mNote = NoteModel.get(getActivity()).getTextNote(noteId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        long noteId = args.getLong(EXTRA_NOTE_ID);

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOAD_NOTE, args, new ViewNoteFragment.NoteLoaderCallbacks());

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_view, menu);
        updateMenuUI();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_note:
                Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
                startActivity(intent);

                return true;

            case R.id.checkbox:
                if (item.isChecked())
                    item.setChecked(false);
                else item.setChecked(true);

                mNote.setArchived(item.isChecked());
                NoteModel.get(getActivity()).updateNote(mNote);
                updateMenuUI();
                return true;

            case R.id.remove:
                NoteModel.get(getActivity()).removeNote(mNote);
                Intent i = new Intent(getActivity(), NoteListActivity.class);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_view, parent, false);

        mTitleField = v.findViewById(R.id.view_title);

        mDateField = v.findViewById(R.id.view_date);

        mDateDetailField = v.findViewById(R.id.view_date_detail);

        mContentField = v.findViewById(R.id.view_content);

        return v;
    }

    private void updateUI(){
        mTitleField.setText(mNote.getTitle());
        mDateField.setText(DateFormat.getPatternInstance(DateFormat.ABBR_MONTH_DAY)
                .format(mNote.getDate()));
        mDateDetailField.setText(mNote.getDate().toString());
        mContentField.setText(mNote.getContent());

    }

    private void updateMenuUI(){
        if(mNote.getArchived() == true) {
            menu.findItem(R.id.checkbox).setChecked(true).setTitle("Uncheck");
        }else {
            menu.findItem(R.id.checkbox).setChecked(false).setTitle("Check");
        }
    }


    private class NoteLoaderCallbacks implements LoaderManager.LoaderCallbacks<Note> {

        @Override
        public Loader<Note> onCreateLoader(int id, Bundle args) {
            return new NoteLoader(getActivity(), args.getLong(EXTRA_NOTE_ID));
        }

        @Override
        public void onLoadFinished(Loader<Note> loader, Note data) {
            mNote = data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Note> loader) {
            // Do Nothing
        }
    }
}
