package com.example.doc2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.text.TextUtils; // Add this import for TextUtils

import java.util.HashMap;
import java.util.Map;

public class PostRequirement extends AppCompatActivity {
    private EditText postRequirement_schoolNameEditText, postRequirement_addressEditText, postRequirement_phoneEditText, postRequirement_locationLinkEditText;
    private Spinner postRequirement_topicSpinner;
    private Button postRequirement_submitBtn;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_requirement);

        db = FirebaseFirestore.getInstance();

        postRequirement_schoolNameEditText = findViewById(R.id.postRequirement_schoolNameEditText);
        postRequirement_addressEditText = findViewById(R.id.postRequirement_addressEditText);
        postRequirement_phoneEditText = findViewById(R.id.postRequirement_phoneEditText);
        postRequirement_locationLinkEditText = findViewById(R.id.postRequirement_locationLinkEditText);
        postRequirement_topicSpinner = findViewById(R.id.postRequirement_topicSpinner);
        postRequirement_submitBtn = findViewById(R.id.postRequirement_submitBtn);

        // Set up the spinner with topics (replace with your actual topics)
        String[] topics = {"Maths", "Science", "Music", "Sports"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, topics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postRequirement_topicSpinner.setAdapter(adapter);

        postRequirement_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequirement();
            }
        });
    }

    private void postRequirement() {
        String schoolName = postRequirement_schoolNameEditText.getText().toString();
        String address = postRequirement_addressEditText.getText().toString();
        String phoneNumber = postRequirement_phoneEditText.getText().toString();
        String locationLink = postRequirement_locationLinkEditText.getText().toString();
        String topic = postRequirement_topicSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(schoolName) || TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(locationLink) || TextUtils.isEmpty(topic)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Requirement object
        Requirement requirement = new Requirement();
        requirement.setSchoolName(schoolName);
        requirement.setAddress(address);
        requirement.setPhoneNumber(phoneNumber);
        requirement.setLocationLink(locationLink);
        requirement.setTopic(topic);

        // Add the requirement to Firestore
        db.collection("requirements")
                .add(requirement)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Get the ID of the newly created document
                        String requirementDocumentId = documentReference.getId();

                        // Create a subcollection within the document for VolunteerApply
                        createVolunteerApplySubcollection(requirementDocumentId);

                        Toast.makeText(PostRequirement.this, "Requirement posted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostRequirement.this, "Failed to post requirement", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createVolunteerApplySubcollection(String requirementDocumentId) {
        // Create a map with initial values for volunteerApplyName and volunteerApplyNumber
        Map<String, Object> volunteerApplyData = new HashMap<>();
        volunteerApplyData.put("volunteerApplyName", "");
        volunteerApplyData.put("volunteerApplyNumber", "");
        volunteerApplyData.put("volunteerApplyQualification", "");

        // Reference to the subcollection within the specified document
        CollectionReference volunteerApplySubcollection = db.collection("requirements")
                .document(requirementDocumentId)
                .collection("VolunteerApply");

        // Add the initial data to the subcollection
        volunteerApplySubcollection.add(volunteerApplyData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Handle success if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure if needed
                    }
                });
    }
}
