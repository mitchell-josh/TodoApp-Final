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
    private int mArchived;
    private int mSolved;
    private Date mCreatedDate;

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(long date){
        Date d = new Date(date);
        mCreatedDate = d;
    }


    public int getSolved() {
        return mSolved;
    }

    public void setSolved(int mSolved) {
        this.mSolved = mSolved;
    }

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

    public int getArchived(){
        return mArchived;
    }

    public void setArchived(int archived){
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
