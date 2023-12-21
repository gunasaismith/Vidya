package com.example.doc2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class DetailedInformationActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_information);

        firestore = FirebaseFirestore.getInstance();

        // Retrieve the Requirement object from the intent
        Requirement requirement = (Requirement) getIntent().getSerializableExtra("requirement");
        final String documentId = getIntent().getStringExtra("documentId");

        // Display detailed information in the UI
        TextView schoolNameTextView = findViewById(R.id.schoolNameTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        TextView locationLinkTextView = findViewById(R.id.locationLinkTextView);
        TextView topicTextView = findViewById(R.id.topicTextView);

        if (requirement != null) {
            schoolNameTextView.setText("School Name: " + requirement.getSchoolName());
            addressTextView.setText("Address: " + requirement.getAddress());
            phoneNumberTextView.setText("Phone Number: " + requirement.getPhoneNumber());
            locationLinkTextView.setText("Location Link: " + requirement.getLocationLink());
            topicTextView.setText("Topic: " + requirement.getTopic());
        }

        // Apply Button
        Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Apply button click
                applyVolunteer(documentId);
            }
        });
    }

    private void applyVolunteer(String documentId) {
        // Retrieve data from EditText fields
        EditText nameEditText = findViewById(R.id.volunteerNameEditText);
        EditText numberEditText = findViewById(R.id.volunteerNumberEditText);
        EditText qualificationEditText = findViewById(R.id.volunteerQualificationEditText);

        String volunteerName = nameEditText.getText().toString().trim();
        String volunteerNumber = numberEditText.getText().toString().trim();
        String volunteerQualification = qualificationEditText.getText().toString().trim();

        // Create a map to store volunteer details
        Map<String, Object> volunteerMap = new HashMap<>();
        volunteerMap.put("volunteerName", volunteerName);
        volunteerMap.put("volunteerNumber", volunteerNumber);
        volunteerMap.put("volunteerQualification", volunteerQualification);

        // Store volunteer details in the VolunteerApply subcollection
        firestore.collection("requirements")
                .document(documentId)
                .collection("VolunteerApply")
                .add(volunteerMap)
                .addOnSuccessListener(documentReference -> {
                    // Handle successful volunteer application
                    showSuccessToast();
                })
                .addOnFailureListener(e -> {
                    // Handle failure to store volunteer details
                    // You can show an error message or log the error
                });
    }

    private void showSuccessToast() {
        // Display a success toast message
        Toast.makeText(this, "Volunteer application successful!", Toast.LENGTH_SHORT).show();
    }
}
