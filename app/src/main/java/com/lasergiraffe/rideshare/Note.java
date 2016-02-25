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
    private int capacity;
    private int currNumRiders;

    public Note(){
        //empty constructor
    }


    public Note(String noteId, String noteTitle, String noteName, String notePhone, String noteContent, int noteCapacity, int noteCurrNumRiders) {
        id = noteId;
        title = noteTitle;
        name = noteName;
        phone = notePhone;
        content = noteContent;
        capacity = noteCapacity;
        currNumRiders = noteCurrNumRiders;

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
    public void setCapacity( int capacity ){this.capacity=capacity;}
    public int getCapacity() {return capacity;}
    public void setCurrNumRiders( int currNumRiders ){this.currNumRiders=currNumRiders;}
    public int getCurrNumRiders() {return currNumRiders;}


    @Override
    public void onClick(View v) {
        System.out.println("hi");
    }
}