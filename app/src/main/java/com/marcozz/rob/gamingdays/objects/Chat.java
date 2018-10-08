package com.marcozz.rob.gamingdays.objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Chat {
    public ArrayList<String> messageList;

    public Chat() {
        if (messageList == null)
            messageList = new ArrayList<>();
    }

    public Chat(ArrayList<String> messageList) {
        if (messageList == null)
            this.messageList = new ArrayList<>();
        else
            this.messageList = messageList;
    }

    public String generateChatList() {
        StringBuilder messages = new StringBuilder();
        for(String entry : messageList) {
            messages.append("Messaggio di: ").append(entry).append("\n\n");
        }
        return messages.toString();
    }

    public void addMessage(String currentUserName, String s) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/YYY, HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        messageList.add(currentUserName + " "+ strDate + " \n" + s + "\n");
    }
}
