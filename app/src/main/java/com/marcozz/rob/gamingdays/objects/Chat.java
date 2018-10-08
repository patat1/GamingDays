package com.marcozz.rob.gamingdays.objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Chat {
    public HashMap<String, String> messageList;

    public Chat() {}

    public Chat(HashMap<String, String> messageList) {
        this.messageList = messageList;
    }
}
