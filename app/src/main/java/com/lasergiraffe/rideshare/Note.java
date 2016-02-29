package com.lasergiraffe.rideshare;

import android.view.View;

import com.parse.*;



@ParseClassName("notes")
public class Note extends ParseObject implements View.OnClickListener{

    private String id;
    private String title;
    private String toDest;
    private String fromDest;
    private String name;
    private String phone;
    private String details;
    private double price;
    private int capacity;
    private int currNumRiders;

    public Note(){
        //empty constructor
    }


    public Note(String noteId, String noteTitle, String noteName, String notePhone,
                String noteDetails, int noteCapacity, int noteCurrNumRiders, String destTo,
                String destFrom, double priceTotal,) {
        id = noteId;
        title = noteTitle;
        name = noteName;
        phone = notePhone;
        details = noteDetails;
        capacity = noteCapacity;
        currNumRiders = noteCurrNumRiders;
        toDest = destTo;
        fromDest = destFrom;
        price = priceTotal;

    }

    @Override
    // toString is the function for the content of the action bar
    public String toString() {
        return fromDest "  -->  " + toDest;
    }

    //GETTERS
    public String getId() { return id; }
    public String getTitle() { return toString(); }
    public String getDetails() { return details; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getCapacity() { return capacity; }
    public int getCurrNumRiders() { return currNumRiders; }
    public double getPrice() { return price; }
    public String getToDest() { return toDest; }
    public String getFromDest() { return fromDest; }

    //SETTERS
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDetails(String details) { this.details = details; }
    public void setName( String name) { this.name = name; }
    public void setPhone( String phone ) { this.phone = phone;}
    public void setCapacity( int capacity ){ this.capacity=capacity; }
    public void setCurrNumRiders( int currNumRiders ){ this.currNumRiders = currNumRiders; }
    public double setPrice(double price) { return this.price = price; }
    public String setToDest(String toDest) { return this.toDest = toDest; }
    public String setFromDest(String fromDest) { return this.fromDest = fromDest; }



    @Override
    public void onClick(View v) {
        System.out.println("hi");
    }
}