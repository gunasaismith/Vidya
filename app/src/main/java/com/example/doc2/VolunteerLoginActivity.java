package com.example.doc2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VolunteerLoginActivity extends AppCompatActivity {
    TextInputEditText editTextVolunteerUsername, editTextVolunteerPassword;
    Button buttonVolunteerLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), VolunteerMain.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteerlogin);

        mAuth = FirebaseAuth.getInstance();
        editTextVolunteerUsername = findViewById(R.id.username_volunteer);
        editTextVolunteerPassword = findViewById(R.id.password_volunteer);
        buttonVolunteerLogin = findViewById(R.id.loginbtn_volunteer);
        progressBar = findViewById(R.id.progressbar_volunteer);

        textView = findViewById(R.id.signupbtn_volunteer);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the Signup (Register) activity
                Intent intent = new Intent(getApplicationContext(), VolunteerRegister.class);
                startActivity(intent);
                finish(); // Optionally, you can finish the VolunteerLoginActivity to prevent going back to it from the Signup page
            }
        });

        buttonVolunteerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String volunteerUsername, volunteerPassword;
                volunteerUsername = String.valueOf(editTextVolunteerUsername.getText());
                volunteerPassword = String.valueOf(editTextVolunteerPassword.getText());
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(volunteerUsername)) {
                    Toast.makeText(VolunteerLoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(volunteerPassword)) {
                    Toast.makeText(VolunteerLoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(volunteerUsername, volunteerPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(VolunteerLoginActivity.this, "Authentication Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), VolunteerMain.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign-in fails, display a message to the user.
                                    Toast.makeText(VolunteerLoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
