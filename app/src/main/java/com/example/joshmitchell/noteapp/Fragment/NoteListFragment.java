package com.example.joshmitchell.noteapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.example.joshmitchell.noteapp.Activity.NoteActivity;
import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;
import com.example.joshmitchell.noteapp.DB.Loaders.SQLiteCursorLoader;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_NEW_NOTE = 0;
    NoteCursorAdapter adapter;

    OnEditSelectedListener mCallback;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new NoteListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        Log.d("NoteListFragment", "Load Finished");
        adapter = new NoteCursorAdapter(getActivity(),
                (DatabaseHelper.NoteCursor) cursor);
        adapter.swapCursor(cursor);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        registerForContextMenu(listView);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.note_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_note:
                        NoteCursorAdapter adapter = (NoteCursorAdapter) getListAdapter();
                        NoteModel noteModel = NoteModel.get(getActivity());
                        for (int i = adapter.getCount() - 1; i >= 0; i--){
                            if(getListView().isItemChecked(i)) {
                                adapter.mNoteCursor.moveToPosition(i);
                                noteModel.removeNote(adapter.mNoteCursor.getNote());
                            }
                        }
                        mode.finish();
                        restartLoader();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        return v;

    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0, null, this);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.note_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        NoteCursorAdapter adapter = (NoteCursorAdapter) getListAdapter();
        int itemPos = info.position;
        adapter.mNoteCursor.moveToPosition(itemPos);
        Note t = adapter.mNoteCursor.getNote();

        switch (item.getItemId()) {
            case R.id.menu_item_delete_note:
                Log.d("NoteListFragment", "Delete Note");
                NoteModel.get(getActivity()).removeNote(t);
                adapter.notifyDataSetChanged();
                getLoaderManager().restartLoader(0, null, this);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int ResultCode, Intent data){
        Log.d("EditNoteFragment", "Request new Note called");
        if (REQUEST_NEW_NOTE == requestCode){
            adapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("NoteListFragment", "onResume Called");
        getLoaderManager().restartLoader(0, null, this);
        if(adapter != null) {
            Log.d("NoteListFragment", "Adapter is not null");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //Start NoteActivity
        Log.d("ViewNoteFragment", "NoteListFrag" + String.valueOf(id));
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

            ListView listView = view.findViewById(android.R.id.list);

            TextView titleTextView = view.findViewById(R.id.note_list_item_titleTextView);
            titleTextView.setText(t.getTitle() + " " + String.valueOf(t.getArchived()));

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
}
