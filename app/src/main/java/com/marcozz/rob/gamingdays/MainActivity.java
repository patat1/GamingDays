package com.marcozz.rob.gamingdays;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcozz.rob.gamingdays.objects.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.marcozz.rob.gamingdays.Constants.EVENT;
import static com.marcozz.rob.gamingdays.Constants.USERS;
import static com.marcozz.rob.gamingdays.Constants.TAG;

public class MainActivity extends AppCompatActivity {
    private TextView name, eventDate, eventTitle;
    private ImageView eventImage;
    private FirebaseAuth auth;
    private DatabaseReference databaseUserReference, databaseEventReference;
    private Event event;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        databaseUserReference = FirebaseDatabase.getInstance().getReference(USERS);
        databaseEventReference = FirebaseDatabase.getInstance().getReference(EVENT);

        checkIfLogged();

        name = findViewById(R.id.name);
        eventImage = findViewById(R.id.image);
        eventDate = findViewById(R.id.date);
        eventTitle = findViewById(R.id.title);
        updateUserName();

        updateEvent();
    }

    private void updateEvent() {
        databaseEventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                Log.e(TAG, event.title);
                Picasso.get().load(event.url).into(eventImage);
                eventDate.setText(event.date);
                eventTitle.setText(event.title);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, getString(R.string.toast_generic_error), error.toException());
            }
        });
    }

    private void updateUserName() {
        if (auth.getUid() != null) {
            databaseUserReference.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUserName = dataSnapshot.getValue(String.class);
                    Log.e(TAG, currentUserName);
                    String msg = currentUserName + ", scegli cosa fare!";
                    name.setText(msg);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(TAG, getString(R.string.toast_generic_error), error.toException());
                }
            });
        }
    }

    private void checkIfLogged() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void doLogout(View view) {
        auth.signOut();
        checkIfLogged();
    }

    public void doConfirm(View view) {
        if (event.confirmed == null)
            event.confirmed = new ArrayList<>();
        if (!event.confirmed.contains(currentUserName)) {
            event.confirmed.add(currentUserName);
            databaseEventReference.setValue(event);
        }
    }

    public void doNotConfirm(View view) {
        if (event.confirmed == null)
            event.confirmed = new ArrayList<>();
        if (event.confirmed.contains(currentUserName)) {
            event.confirmed.remove(currentUserName);
            databaseEventReference.setValue(event);
        }
    }
}
