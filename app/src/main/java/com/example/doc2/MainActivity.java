package com.example.doc2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Check if the user is logged in, if not, redirect to the login page
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Retrieve the user's name from Firestore
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");
                            if (userName != null && !userName.isEmpty()) {
                                // Display the user's name
                                textView.setText("Hello, " + userName + "!");
                            } else {
                                // If the user's name is not available, display the email
                                textView.setText(user.getEmail());
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure to retrieve user's name
                        textView.setText(user.getEmail());
                    });
        }

        // Set click listeners for the tiles
        findViewById(R.id.postTile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click on postTile
                onpostTileClick(view);
            }
        });

        findViewById(R.id.listTile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click on listTile
                onlistTileClick(view);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Define the click handler for postTile
    public void onpostTileClick(View view) {
        // Display a toast message when postTile is clicked
        Toast.makeText(this, "Post Tile Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PostRequirement.class);
        startActivity(intent);
    }

    // Define the click handler for listTile
    public void onlistTileClick(View view) {
        // Display a toast message when postTile is clicked
        Toast.makeText(this, "List Tile Clicked", Toast.LENGTH_SHORT).show();
    }
}
