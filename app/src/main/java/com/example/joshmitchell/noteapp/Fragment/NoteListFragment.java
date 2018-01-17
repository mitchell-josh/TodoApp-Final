package com.example.joshmitchell.noteapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.joshmitchell.noteapp.Activity.NoteActivity;
import com.example.joshmitchell.noteapp.DB.DatabaseHelper;
import com.example.joshmitchell.noteapp.DB.Loaders.NoteListCursorLoader;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.Adapter.NoteCursorAdapter;
import com.example.joshmitchell.noteapp.R;
import com.example.joshmitchell.noteapp.DB.Loaders.SQLiteCursorLoader;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_NEW_NOTE = 0;
    private static final String LIST_FILTER = "listFilter";

    NoteCursorAdapter mAdapter;

    OnEditSelectedListener mCallback;

    private DrawerLayout mDrawer;
    private int mListFilter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new NoteListCursorLoader(getActivity(), mListFilter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        Log.d("NoteListFragment", "Load Finished");
        mAdapter = new NoteCursorAdapter(getActivity(),
                (DatabaseHelper.NoteCursor) cursor);
        mAdapter.swapCursor(cursor);
        setListAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        setListAdapter(null);
    }

    public interface OnEditSelectedListener {
        public void onEditSelected(long noteId);
    }

    public static NoteListFragment newInstance(int listFilter){
        Bundle args = new Bundle();

        args.putInt(LIST_FILTER, listFilter);

        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);

        return fragment;
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
        mListFilter = -1;

        Bundle args = getArguments();
        if (args != null){
            mListFilter = args.getInt(LIST_FILTER);
        }

        getActivity().setTitle(R.string.notes_title);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        // Find our drawer view
        mDrawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);

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

            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);

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
        mAdapter.mNoteCursor.moveToPosition(itemPos);
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
            mAdapter.notifyDataSetChanged();
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("NoteListFragment", "onResume Called");
        getLoaderManager().restartLoader(0, null, this);
        if(mAdapter != null) {
            Log.d("NoteListFragment", "Adapter is not null");
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        //Start NoteActivity
        Log.d("ViewNoteFragment", "NoteListFrag" + String.valueOf(id));
        mCallback.onEditSelected(id);
    }
}
