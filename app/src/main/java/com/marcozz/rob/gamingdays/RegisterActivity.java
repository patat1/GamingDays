package com.marcozz.rob.gamingdays;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.marcozz.rob.gamingdays.Constants.USERS;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputNickname;
    private FirebaseAuth auth;

    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputNickname = (EditText) findViewById(R.id.nickname);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference(USERS);
    }

    public void tryRegistration(View view) {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String nickname = inputNickname.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.hint_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.hint_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.alert_password, Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Errore." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.toast_generic_confirmation,
                                    Toast.LENGTH_SHORT).show();
                            createUserNicknameReference(nickname);
                        }
                    }
                });

    }

    private void createUserNicknameReference(String nickname) {
        auth = FirebaseAuth.getInstance();
        if (auth.getUid() != null) {
            mFirebaseDatabase.child(auth.getUid()).setValue(nickname);
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        } else
            Toast.makeText(RegisterActivity.this, R.string.toast_generic_error, Toast.LENGTH_SHORT).show();
    }
}
