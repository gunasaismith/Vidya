package com.example.doc2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class AvailablePostsActivity extends AppCompatActivity {

    private ListView postsListView;
    private ArrayAdapter<Requirement> adapter;
    private List<Requirement> postsList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_posts);

        db = FirebaseFirestore.getInstance();

        postsListView = findViewById(R.id.postsListView);
        postsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postsList);
        postsListView.setAdapter(adapter);

        // Set item click listener
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected post's information
                Requirement selectedRequirement = postsList.get(position);

                // Start a new activity with detailed information and document ID
                showDetailedInformation(selectedRequirement.getDocumentId(), selectedRequirement);
            }
        });

        loadAvailablePosts();
    }

    private void loadAvailablePosts() {
        db.collection("requirements")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Requirement requirement = documentSnapshot.toObject(Requirement.class);

                            if (requirement.getSchoolName() != null && requirement.getAddress() != null) {
                                // Store both Requirement and Document ID
                                String documentId = documentSnapshot.getId();
                                requirement.setDocumentId(documentId); // Assuming you have a setter for documentId in your Requirement class
                                postsList.add(requirement);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AvailablePostsActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AvailablePostsActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDetailedInformation(String documentId, Requirement requirement) {
        // Start a new activity with detailed information and document ID
        Intent intent = new Intent(this, DetailedInformationActivity.class);
        intent.putExtra("requirement", requirement);
        intent.putExtra("documentId", documentId);
        startActivity(intent);
    }
}
