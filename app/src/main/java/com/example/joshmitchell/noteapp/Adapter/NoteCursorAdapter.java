package com.example.joshmitchell.noteapp.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.icu.text.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 17/01/2018.
 */

public class NoteCursorAdapter extends CursorAdapter {

    public DatabaseHelper.NoteCursor  mNoteCursor;

    public NoteCursorAdapter(Context context, DatabaseHelper.NoteCursor cursor){
        super(context, cursor, 0);
        mNoteCursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_item_note, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        Note t = mNoteCursor.getNote();

        ListView listView = view.findViewById(android.R.id.list);

        TextView titleTextView = view.findViewById(R.id.note_list_item_titleTextView);
        titleTextView.setText(t.getTitle() + " " + String.valueOf(t.getArchived()) + " " + String.valueOf(t.getSolved()));

        TextView dateTextView = view.findViewById(R.id.note_list_item_dateTextView);
        //Change date format for list
        String stringDate = DateFormat.getPatternInstance(DateFormat.ABBR_MONTH_DAY)
                .format(t.getDate());
        dateTextView.setText(stringDate);

        Log.d("NoteListFragment", String.valueOf(t.getArchived()));

        //Strike through if item is checked
        if (t.getArchived() == 1){
            titleTextView.setPaintFlags(titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dateTextView.setPaintFlags(dateTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            titleTextView.setPaintFlags(titleTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dateTextView.setPaintFlags(titleTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
