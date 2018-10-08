package com.marcozz.rob.gamingdays.objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Event {

    public String date;
    public String title;
    public String url;
    public ArrayList <String> confirmed;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Event() {
    }

    public Event(String date, String title, String url, ArrayList<String> confirmed) {
        this.date = date;
        this.title = title;
        this.url = url;
        this.confirmed = confirmed;
    }
}
