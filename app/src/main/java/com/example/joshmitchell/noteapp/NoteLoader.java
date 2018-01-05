package com.example.joshmitchell.noteapp;

import android.content.Context;
import android.util.Log;

import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;

/**
 * Created by Josh Mitchell on 04/01/2018.
 */

public class NoteLoader extends DataLoader<Note> {

    private long mNoteId;

    public NoteLoader(Context context, long noteId){
        super(context);
        mNoteId = noteId;
        Log.d("NoteLoader", "NoteLoader");
    }

    @Override
    public Note loadInBackground() {
        Log.d("NoteLoader", "loadInBackground");
        return NoteModel.get(getContext()).getTextNote(mNoteId);

    }
}
