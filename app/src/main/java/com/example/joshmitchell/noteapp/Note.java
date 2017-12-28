package com.example.joshmitchell.noteapp;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public class Note {

    private UUID mId;
    private String mTitle;
    private String mContent;
    private Date mDate;
    private boolean mArchived;

    public Note(){
        // Generate text note
        mId = UUID.randomUUID();
        mDate = new Date();
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

    public UUID getId(){
        return mId;
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
