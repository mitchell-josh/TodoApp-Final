package com.example.joshmitchell.noteapp.Fragment;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
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
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 28/12/2017.
 */

public class ViewNoteFragment extends Fragment {

    private Note mNote;
    private TextView mTitleField, mDateField, mDateDetailField, mContentField;
    private Menu menu;
    public static final String EXTRA_NOTE_ID = "com.example.joshmitchell.noteapp.note_id";


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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long noteId = getArguments().getLong(EXTRA_NOTE_ID);
        mNote = NoteModel.get(getActivity()).getTextNote(noteId);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_view, menu);
        Log.d("ViewNoteFragment", "onCreateOptionsMenu");
        updateMenuUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_note:
                Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
                startActivity(intent);

                return true;

            case R.id.remove:
                NoteModel.get(getActivity()).removeNote(mNote);
                Intent i = new Intent(getActivity(), NoteListActivity.class);
                startActivity(i);

                return true;


            case R.id.Check:
                if (item.isChecked())
                    item.setChecked(false);
                else item.setChecked(true);

                mNote.setArchived(item.isChecked() ? 1 : 0);
                NoteModel.get(getActivity()).updateNote(mNote);
                updateMenuUI();
                return true;

            case R.id.send:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, mNote.getTitle());
                sendIntent.putExtra(Intent.EXTRA_TEXT, mNote.getContent());
                startActivity(sendIntent);

                return true;

            case R.id.archived:
                mNote.setSolved(1);
                NoteModel.get(getActivity()).updateNote(mNote);

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.note_list_item_context, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_view, parent, false);

        mTitleField = v.findViewById(R.id.view_title);

        mDateField = v.findViewById(R.id.view_date);

        mDateDetailField = v.findViewById(R.id.view_date_detail);

        mContentField = v.findViewById(R.id.view_content);

        updateUI();

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private void updateUI(){
        mTitleField.setText(mNote.getTitle());
        mDateField.setText(DateFormat.getPatternInstance(DateFormat.ABBR_MONTH_DAY)
                .format(mNote.getDate()));
        mDateDetailField.setText(mNote.getDate().toString());
        mContentField.setText(mNote.getContent());

    }

    private void updateMenuUI(){
        Log.d("ViewNoteFragment", "updateMenuUI");
        if(mNote.getArchived() == 1) {
            menu.findItem(R.id.Check).setChecked(true).setTitle("Uncheck");
        }else {
            menu.findItem(R.id.Check).setChecked(false).setTitle("Check");
        }
    }
}
