package com.lasergiraffe.rideshare;

import android.view.View;

import com.parse.*;

@ParseClassName("notes")
public class Note extends ParseObject{

    private String id;
    private String title;
    private String content;

    public Note(){
        //empty constructor
    }


    public Note(String noteId, String noteTitle, String noteContent) {
        id = noteId;
        title = noteTitle;
        content = noteContent;

    }

    @Override
    // toString is the function for the content of the action bar
    public String toString() {
        return this.getTitle();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}