package com.example.doc2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VolunteerMain extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutButton, applyTileButton;
    TextView volunteerDetailsTextView;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteermain);

        auth = FirebaseAuth.getInstance();
        LinearLayout applyTileButton = findViewById(R.id.applyTile);
        logoutButton = findViewById(R.id.logout);
        volunteerDetailsTextView = findViewById(R.id.volunteer_user_details);
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            // Redirect to VolunteerLoginActivity or perform necessary actions
        } else {
            // Retrieve the volunteer's name from Firestore
            db.collection("volunteers").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String volunteerName = documentSnapshot.getString("name");
                            if (volunteerName != null && !volunteerName.isEmpty()) {
                                volunteerDetailsTextView.setText("Hello, " + volunteerName + "!");
                            } else {
                                volunteerDetailsTextView.setText(user.getEmail());
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure to retrieve volunteer's name
                        volunteerDetailsTextView.setText(user.getEmail());
                    });
        }

        applyTileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the Apply Tile
                // You can navigate to another activity or perform any action here
                Toast.makeText(VolunteerMain.this, "Apply Tile Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AvailablePostsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}
