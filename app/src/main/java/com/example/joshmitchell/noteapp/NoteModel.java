package com.example.joshmitchell.noteapp;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteModel {

    private ArrayList<Note> mNotes;

    private static NoteModel sNoteModel;
    private Context mAppContext;

    private NoteModel(Context appContext){
        mAppContext = appContext;
        mNotes = new ArrayList<Note>();
    }

    public static NoteModel get(Context c){
        if (sNoteModel == null){
            sNoteModel = new NoteModel(c.getApplicationContext());
        }
        return sNoteModel;
    }

    public ArrayList<Note> getTextNotes() {
        return mNotes;
    }

    public Note getTextNote(UUID id){
        for (Note t : mNotes){
            if(t.getId().equals(id)){
                return t;
            }
        }
        return null;
    }

    public void addNote(Note note){
        mNotes.add(note);
    }

    public void removeNote(UUID noteId){
        Note t = getTextNote(noteId);
        mNotes.remove(t);
    }


}
