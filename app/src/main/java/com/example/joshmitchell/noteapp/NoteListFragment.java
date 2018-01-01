package com.example.joshmitchell.noteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListFragment extends ListFragment {

    private ArrayList<Note> mNotes;

    OnEditSelectedListener mCallback;

    public interface OnEditSelectedListener {
        public void onEditSelected(UUID noteId);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallback = (OnEditSelectedListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "must implement onEditSelectedListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.notes_title);
        mNotes = NoteModel.get(getActivity()).getTextNotes();

        NoteAdapter adapter = new NoteAdapter(mNotes);
        setListAdapter(adapter);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((NoteAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_item_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_note:
                Note note = new Note();
                NoteModel.get(getActivity()).addNote(note);

                Intent intent = NoteActivity.newIntent(getActivity(), note.getId());
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Note t = ((NoteAdapter)getListAdapter()).getItem(position);

        //Start NoteActivity
        mCallback.onEditSelected(t.getId());
    }

    private class NoteAdapter extends ArrayAdapter<Note>{

        public NoteAdapter(ArrayList<Note> notes){
            super(getActivity(), 0, notes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_note, null);
            }

            Note t = getItem(position);

            TextView titleTextView = convertView.findViewById(R.id.note_list_item_titleTextView);
            titleTextView.setText(t.getTitle());

            TextView dateTextView = convertView.findViewById(R.id.note_list_item_dateTextView);
            //Change date format for list
            String stringDate = DateFormat.getPatternInstance(DateFormat.ABBR_MONTH_DAY)
                    .format(t.getDate());
            dateTextView.setText(stringDate);

            //Strike through if item is checked
            if (t.getArchived() == true){
                titleTextView.setPaintFlags(titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dateTextView.setPaintFlags(dateTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                titleTextView.setPaintFlags(titleTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                dateTextView.setPaintFlags(titleTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }

            return convertView;
        }
    }
}
