package com.example.joshmitchell.noteapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.joshmitchell.noteapp.Model.Note;

/**
 * Created by Josh Mitchell on 03/01/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "note-db";
    private static final int VERSION = 1;

    private static final String TABLE_NOTE = "note";
    private static final String NOTE_ID = "_id";
    private static final String NOTE_DATE = "date";
    private static final String NOTE_TITLE = "title";
    private static final String NOTE_CONTENT = "content";
    private static final String NOTE_CREATED_DATE = "created_date";
    private static final String NOTE_SOLVED = "solved";
    private static final String NOTE_ARCHIVED = "archived";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Create database");
        //Create note table
        db.execSQL("create table " + TABLE_NOTE + "(" +
                "_id integer primary key autoincrement, date integer, title text, content text, " +
                "created_date integer, solved integer, archived integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, null, null);
    }

    public long insertNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(NOTE_DATE, note.getDate().getTime());
        cv.put(NOTE_TITLE, note.getTitle());
        cv.put(NOTE_CONTENT, note.getContent());
        cv.put(NOTE_CREATED_DATE, note.getDate().getTime());
        cv.put(NOTE_ARCHIVED, false);
        cv.put(NOTE_SOLVED, false);
        return getWritableDatabase().insert(TABLE_NOTE, null, cv);
    }

    public long updateNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(NOTE_DATE, note.getDate().getTime());
        cv.put(NOTE_TITLE, note.getTitle());
        cv.put(NOTE_CONTENT, note.getContent());
        cv.put(NOTE_CREATED_DATE, note.getCreatedDate().getTime());
        cv.put(NOTE_ARCHIVED, note.getArchived());
        cv.put(NOTE_SOLVED, note.getSolved());
        return getWritableDatabase().update(TABLE_NOTE, cv, "_id = ?",
                new String[] { String.valueOf(note.getId()) });
    }

    public long removeNote(Note note){
        return getWritableDatabase().delete(TABLE_NOTE, "_id = ?",
                new String[] { String.valueOf(note.getId()) });
    }

    public NoteCursor queryNotes(){
        Cursor wrapped = getReadableDatabase().query(TABLE_NOTE,
                null, NOTE_SOLVED + " = ?", new String[] { "0" }, null, null, NOTE_DATE + " asc");
        return new NoteCursor(wrapped);
    }

    public NoteCursor queryArchived(){
        Cursor wrapped = getReadableDatabase().query(
                TABLE_NOTE,
                null,
                NOTE_SOLVED + " = ?",
                new String[] { "1" },
                null,
                null,
                null
        );
        return new NoteCursor(wrapped);
    }

    public NoteCursor queryNote(long id){
        Cursor wrapped = getReadableDatabase().query(TABLE_NOTE,
                null,
                NOTE_ID + " = ?",
                new String[]{ String.valueOf(id) },
                null,
                null,
                null,
                "1");
        return new NoteCursor(wrapped);
    }

    public static class NoteCursor extends CursorWrapper {

        public NoteCursor(Cursor c){
            super(c);
        }

        public Note getNote(){
            if (isBeforeFirst() || isAfterLast())
                return null;
            Note note = new Note();
            note.setId(getLong(getColumnIndex(NOTE_ID)));
            note.setDate(getLong(getColumnIndex(NOTE_DATE)));
            note.setCreatedDate(getLong(getColumnIndex(NOTE_CREATED_DATE)));
            note.setTitle(getString(getColumnIndex(NOTE_TITLE)));
            note.setContent(getString(getColumnIndex(NOTE_CONTENT)));
            note.setArchived(getInt(getColumnIndex(NOTE_ARCHIVED)));
            note.setSolved(getInt(getColumnIndex(NOTE_SOLVED)));
            //TODO Update these later
            return note;
        }
    }
}
