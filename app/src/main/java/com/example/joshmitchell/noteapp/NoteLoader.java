package com.example.joshmitchell.noteapp;

import android.content.Context;

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
    }

    @Override
    public Note loadInBackground() {
        return NoteModel.get(getContext()).getTextNote(mNoteId);
    }
}
