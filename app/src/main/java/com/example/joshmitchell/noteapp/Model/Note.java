package com.example.joshmitchell.noteapp.Model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class Note {

    private long mId;
    private String mTitle;
    private String mContent;
    private Date mDate;
    private boolean mArchived;

    public Note(){
        // Generate text note
        mDate = new Date();
        mId = -1;
    }

    @Override
    public String toString(){
        return mTitle;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
        mDate = new Date();
    }

    public void setId(long id){
        mId = id;
    }

    public long getId(){
        return mId;
    }

    public void setDate(long date){
        Date d = new Date(date);
        mDate = d;
    }

    public Date getDate(){
        return mDate;
    }

    public boolean getArchived(){
        return mArchived;
    }

    public void setArchived(boolean archived){
        mArchived = archived;
    }

    public void setContent(String content) {
        mContent = content;
        mDate = new Date();
    }

    public String getContent(){
        return mContent;
    }

}
