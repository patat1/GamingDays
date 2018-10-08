package com.marcozz.rob.gamingdays;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcozz.rob.gamingdays.objects.Chat;
import com.marcozz.rob.gamingdays.objects.Event;

import static com.marcozz.rob.gamingdays.Constants.ADMIN_UID;
import static com.marcozz.rob.gamingdays.Constants.CHAT;
import static com.marcozz.rob.gamingdays.Constants.TAG;
import static com.marcozz.rob.gamingdays.Constants.USERS;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference databaseUserReference, databaseChatReference;
    private Chat chat;
    private String currentUserName;
    private TextView chatMessages;
    private EditText chatMessage;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);auth = FirebaseAuth.getInstance();
        databaseChatReference = FirebaseDatabase.getInstance().getReference(CHAT);
        databaseUserReference = FirebaseDatabase.getInstance().getReference(USERS);
        
        checkIfLogged();
        updateChat();

        chatMessages = findViewById(R.id.messages_list);
        chatMessage = findViewById(R.id.write_message);
        scrollView = findViewById(R.id.scrolllist);
    }

    private void updateChat() {
        databaseChatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chat = dataSnapshot.getValue(Chat.class);
                Log.e(TAG, "chat loaded");
                if (chat==null)
                    chat = new Chat();
                chatMessages.setText(chat.generateChatList());
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, getString(R.string.toast_generic_error), error.toException());
            }
        });
    }

    private void checkIfLogged() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
            finish();
        } else
            updateUserName();
    }

    private void updateUserName() {
        if (auth.getUid() != null) {
            databaseUserReference.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUserName = dataSnapshot.getValue(String.class);
                    Log.e(TAG, currentUserName);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(TAG, getString(R.string.toast_generic_error), error.toException());
                }
            });
        }
    }

    public void sendMessage(View view) {
        if (!chatMessage.getText().toString().equals("")){
            chat.addMessage(currentUserName, chatMessage.getText().toString());
            chatMessage.setText("");
            databaseChatReference.setValue(chat);
        }
    }
}
