package com.marcozz.rob.gamingdays.objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Event {

    public String date;
    public String title;
    public String description;
    public String url;

    public void setDate(String input) {
        if (!input.equals(""))
            this.date = input;
    }

    public void setTitle(String input) {
        if (!input.equals(""))
            this.title = input;
    }

    public void setDescription(String input) {
        if (!input.equals(""))
            description = input;
    }

    public void setUrl(String input) {
        if (!input.equals(""))
            url = input;
    }

    public void setConfirmed(ArrayList<String> confirmed) {
        this.confirmed = confirmed;
    }

    public ArrayList <String> confirmed = new ArrayList<>();

    public Event() {}

    public Event(String date, String title, String url, ArrayList<String> confirmed, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.url = url;
        if (confirmed!=null)
            this.confirmed = confirmed;
        else
            this.confirmed = new ArrayList<>();
    }

    public String listPeople() {
        StringBuilder list = new StringBuilder();
        list.append("Lista di chi ha già confermato:\n");
        for (String item: confirmed) {
            list.append("- ").append(item).append("\n");
        }

        return list.toString();
    }
}
