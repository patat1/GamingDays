package com.marcozz.rob.gamingdays;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcozz.rob.gamingdays.objects.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.marcozz.rob.gamingdays.Constants.ADMIN_UID;
import static com.marcozz.rob.gamingdays.Constants.EVENT;
import static com.marcozz.rob.gamingdays.Constants.USERS;
import static com.marcozz.rob.gamingdays.Constants.TAG;

public class MainActivity extends AppCompatActivity {
    private TextView name, confirmedList, eventDate, eventTitle, eventDescription;
    private ImageView eventImage, confirmImage, notConfirmImage;
    private FirebaseAuth auth;
    private DatabaseReference databaseUserReference, databaseEventReference;
    private Event event;
    private String currentUserName;
    private ProgressBar progressBar;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        databaseUserReference = FirebaseDatabase.getInstance().getReference(USERS);
        databaseEventReference = FirebaseDatabase.getInstance().getReference(EVENT);

        name = findViewById(R.id.name);
        confirmedList = findViewById(R.id.confirmed_list);
        eventImage = findViewById(R.id.image);
        eventDate = findViewById(R.id.date);
        eventTitle = findViewById(R.id.title);
        eventDescription = findViewById(R.id.description);
        progressBar = findViewById(R.id.progress);
        editButton = findViewById(R.id.edit_button);

        confirmImage = findViewById(R.id.confirm_button);
        notConfirmImage = findViewById(R.id.not_confirm_button);

        checkIfLogged();
        updateUserName();
        updateEvent();
    }

    private void updateEvent() {
        databaseEventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                Log.e(TAG, event.title);
                Picasso.get().load(event.url).fit().centerCrop().into(eventImage);
                eventDate.setText(event.date);
                eventTitle.setText(event.title);
                eventDescription.setText(event.description);

                if (event.confirmed.size() == 0)
                    confirmedList.setText(R.string.no_one);
                else
                    confirmedList.setText(event.listPeople());
                progressBar.setVisibility(View.GONE);
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
        } else if (auth.getUid().equals(ADMIN_UID))
            editButton.setVisibility(View.VISIBLE);
    }

    public void doLogout(View view) {
        auth.signOut();
        checkIfLogged();
    }

    public void doConfirm(View view) {
        if (!event.confirmed.contains(currentUserName)) {
            event.confirmed.add(currentUserName);
            databaseEventReference.setValue(event);
            Toast.makeText(this, R.string.confirm_thankyou, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.confirm_already_confirmed, Toast.LENGTH_SHORT).show();
    }

    public void doNotConfirm(View view) {
        if (event.confirmed.contains(currentUserName)) {
            event.confirmed.remove(currentUserName);
            databaseEventReference.setValue(event);
            Toast.makeText(this, R.string.confirm_goodbye, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.confirm_already_not_confirmed, Toast.LENGTH_SHORT).show();
    }

    public void doCallEdit(View view) {
        startActivity(new Intent(getApplicationContext(), EventUpdateActivity.class));
    }
}
