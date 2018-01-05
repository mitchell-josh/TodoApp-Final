package com.example.joshmitchell.noteapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.example.joshmitchell.noteapp.Activity.NoteActivity;
import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;
import com.example.joshmitchell.noteapp.SQLiteCursorLoader;

import java.util.ArrayList;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_NEW_NOTE = 0;

    OnEditSelectedListener mCallback;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new NoteListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        NoteCursorAdapter adapter = new NoteCursorAdapter(getActivity(),
                (DatabaseHelper.NoteCursor) cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        setListAdapter(null);
    }

    public interface OnEditSelectedListener {
        public void onEditSelected(long noteId);
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
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(0, null, this);
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
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivityForResult(intent, REQUEST_NEW_NOTE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int ResultCode, Intent data){
        if (REQUEST_NEW_NOTE == requestCode){
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //Start NoteActivity
        mCallback.onEditSelected(id);
    }

    public static class NoteListCursorLoader extends SQLiteCursorLoader {

        public NoteListCursorLoader(Context context){
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return NoteModel.get(getContext()).queryRuns();
        }
    }

    private static class NoteCursorAdapter extends CursorAdapter {

        private DatabaseHelper.NoteCursor  mNoteCursor;

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

            TextView titleTextView = view.findViewById(R.id.note_list_item_titleTextView);
            titleTextView.setText(t.getTitle());

            TextView dateTextView = view.findViewById(R.id.note_list_item_dateTextView);
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
        }
    }
}
