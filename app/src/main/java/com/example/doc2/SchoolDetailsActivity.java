package com.example.doc2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SchoolDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        // Retrieve data from the intent
        String schoolName = getIntent().getStringExtra("schoolName");
        double schoolLatitude = getIntent().getDoubleExtra("schoolLatitude", 0.0);
        double schoolLongitude = getIntent().getDoubleExtra("schoolLongitude", 0.0);

        // Set up views
        TextView nameTextView = findViewById(R.id.school_name);
        TextView latitudeTextView = findViewById(R.id.school_latitude);
        TextView longitudeTextView = findViewById(R.id.school_longitude);

        // Set data to views
        nameTextView.setText("School Name: " + schoolName);
        latitudeTextView.setText("Latitude: " + schoolLatitude);
        longitudeTextView.setText("Longitude: " + schoolLongitude);
    }
}
