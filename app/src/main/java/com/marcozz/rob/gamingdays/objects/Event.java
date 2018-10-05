package com.marcozz.rob.gamingdays.objects;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event {

    public String date;
    public String title;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Event() {
    }

    public Event(String date, String title, String url) {
        this.date = date;
        this.title = title;
        this.url = url;
    }
}
