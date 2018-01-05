package com.example.joshmitchell.noteapp.DB;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.joshmitchell.noteapp.Model.Note;

import java.util.ArrayList;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class NoteModel {

    private ArrayList<Note> mNotes;

    private static final String PREFS_FILE = "notes";
    private static final String PREF_CURRENT_NOTE_ID = "NoteModel.currentNoteId";
    private DatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentNoteId;

    private static NoteModel sNoteModel;
    private Context mAppContext;

    private NoteModel(Context appContext){
        mNotes = new ArrayList<Note>();
        mAppContext = appContext;
        mHelper = new DatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentNoteId = mPrefs.getLong(PREF_CURRENT_NOTE_ID, -1);


    }

    public static NoteModel get(Context c){
        if (sNoteModel == null){
            sNoteModel = new NoteModel(c.getApplicationContext());
        }
        return sNoteModel;
    }

    public ArrayList<Note> getTextNotes() {
        DatabaseHelper.NoteCursor cursor = mHelper.queryNotes();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            mNotes.add(cursor.getNote());
            cursor.moveToNext();
        }
        cursor.close();
        return mNotes;
    }

    public Note getTextNote(long id){
        Note note = null;
        DatabaseHelper.NoteCursor cursor = mHelper.queryNote(id);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            note = cursor.getNote();
        cursor.close();
        return note;
    }

    public void addNote(Note note){
        mHelper.insertNote(note);
    }

    public DatabaseHelper.NoteCursor queryRuns(){
        return mHelper.queryNotes();
    }

    public void removeNote(Note note){
        mHelper.removeNote(note);
    }

    public void updateNote(Note note){
        mHelper.updateNote(note);
    }
}
