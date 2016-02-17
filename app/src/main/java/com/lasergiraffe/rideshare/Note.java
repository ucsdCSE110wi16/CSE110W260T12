package com.lasergiraffe.rideshare;

import android.view.View;

import com.parse.*;



@ParseClassName("notes")
public class Note extends ParseObject implements View.OnClickListener{

    private String id;
    private String title;
    private String name;
    private String phone;
    private String content;

    public Note(){
        //empty constructor
    }


    public Note(String noteId, String noteTitle, String noteName, String notePhone, String noteContent) {
        id = noteId;
        title = noteTitle;
        name = noteName;
        phone = notePhone;
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
    public String getName() { return name; }
    public void setName( String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone( String phone ) { this.phone = phone;}


    @Override
    public void onClick(View v) {
        System.out.println("hi");
    }
}