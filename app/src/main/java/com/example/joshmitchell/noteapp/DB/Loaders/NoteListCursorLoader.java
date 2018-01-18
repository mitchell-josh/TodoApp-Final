package com.example.joshmitchell.noteapp.DB.Loaders;

import android.content.Context;
import android.database.Cursor;

import com.example.joshmitchell.noteapp.DB.NoteModel;

/**
 * Created by Josh Mitchell on 17/01/2018.
 */

public class NoteListCursorLoader extends SQLiteCursorLoader {

    private int filter;

    public NoteListCursorLoader(Context context, int filter){
        super(context);
        this.filter = filter;
    }

    @Override
    protected Cursor loadCursor() {
        if(filter == -1)
            return NoteModel.get(getContext()).queryNotes();
        if(filter == 1)
            return NoteModel.get(getContext()).queryArchived();

        return null;
    }
}
