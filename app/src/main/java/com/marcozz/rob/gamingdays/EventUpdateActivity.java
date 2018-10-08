package com.marcozz.rob.gamingdays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcozz.rob.gamingdays.objects.Event;
import com.squareup.picasso.Picasso;

import static com.marcozz.rob.gamingdays.Constants.ADMIN_UID;
import static com.marcozz.rob.gamingdays.Constants.EVENT;
import static com.marcozz.rob.gamingdays.Constants.TAG;

public class EventUpdateActivity extends AppCompatActivity {

    private EditText eventDate, eventTitle, eventDescription, eventImageUrl;
    private ImageView eventImage;
    private CheckBox deleteAllCheckBox;
    private FirebaseAuth auth;
    private DatabaseReference databaseEventReference;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);

        auth = FirebaseAuth.getInstance();
        checkIfLogged();
        databaseEventReference = FirebaseDatabase.getInstance().getReference(EVENT);

        eventImage = findViewById(R.id.image);
        eventImageUrl = findViewById(R.id.image_url);
        eventDate = findViewById(R.id.date);
        eventTitle = findViewById(R.id.title);
        eventDescription = findViewById(R.id.description);
        deleteAllCheckBox = findViewById(R.id.delete_all);

        updateEvent();
    }

    private void updateEvent() {
        databaseEventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                Log.e(TAG, event.title);
//                eventImageUrl.setText(event.url);
//                eventDate.setText(event.date);
//                eventTitle.setText(event.title);
//                eventDescription.setText(event.description);
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
            startActivity(new Intent(EventUpdateActivity.this, LoginActivity.class));
            finish();
        } else if (!auth.getUid().equals(ADMIN_UID)) {
            startActivity(new Intent(EventUpdateActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void doConfirm(View view) {
        event.setTitle(eventTitle.getText().toString());
        event.setDate(eventDate.getText().toString());
        event.setDescription(eventDescription.getText().toString());
        event.setUrl(eventImageUrl.getText().toString());
        if (deleteAllCheckBox.isChecked()) {
                event.setConfirmed(null);
        }
        databaseEventReference.setValue(event);
        finish();
    }

    public void reloadImage(View view) {
        if (!eventImageUrl.getText().toString().equals("")) {
            Log.d(TAG, eventImageUrl.getText().toString());
            Picasso.get().load(eventImageUrl.getText().toString()).fit().centerCrop().into(eventImage);
        }
    }
}
