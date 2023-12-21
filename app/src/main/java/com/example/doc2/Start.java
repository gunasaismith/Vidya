package com.example.doc2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    // Handle click for Volunteer Tile
    public void onVolunteerTileClick(View view) {
        Toast.makeText(this, "Volunteer Tile Clicked", Toast.LENGTH_SHORT).show();
        // Redirect to VolunteerRegister activity when Volunteer Tile is clicked
        Intent intent = new Intent(this, VolunteerRegister.class);
        startActivity(intent);
    }

    // Handle click for School Tile
    public void onSchoolTileClick(View view) {
        Toast.makeText(this, "School Tile Clicked", Toast.LENGTH_SHORT).show();
        // Redirect to Register activity when School Tile is clicked
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
