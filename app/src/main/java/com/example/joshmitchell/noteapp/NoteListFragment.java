package com.example.joshmitchell.noteapp;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListFragment extends ListFragment {

    private ArrayList<Note> mNotes;

    private static final String TAG = "NoteListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.notes_title);
        mNotes = NoteModel.get(getActivity()).getTextNotes();

        NoteAdapter adapter = new NoteAdapter(mNotes);
        setListAdapter(adapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        ((NoteAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Note t = ((NoteAdapter)getListAdapter()).getItem(position);

        //Start NoteActivity
        Intent i = new Intent(getActivity(), NoteActivity.class);
        i.putExtra(ViewNoteFragment.EXTRA_NOTE_ID, t.getId());
        startActivity(i);
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

            return convertView;
        }
    }
}
