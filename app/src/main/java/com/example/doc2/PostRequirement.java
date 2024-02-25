package com.example.doc2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class PostRequirement extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private EditText postRequirement_schoolNameEditText, postRequirement_addressEditText, postRequirement_phoneEditText;
    private Spinner postRequirement_topicSpinner;
    private Button postRequirement_submitBtn;

    private GoogleMap mMap;
    private MapView mMapView;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_requirement);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get references to UI elements
        postRequirement_schoolNameEditText = findViewById(R.id.postRequirement_schoolNameEditText);
        postRequirement_addressEditText = findViewById(R.id.postRequirement_addressEditText);
        postRequirement_phoneEditText = findViewById(R.id.postRequirement_phoneEditText);
        postRequirement_topicSpinner = findViewById(R.id.postRequirement_topicSpinner);
        postRequirement_submitBtn = findViewById(R.id.postRequirement_submitBtn);

        String[] topics = {"Maths", "Science", "Music", "Sports"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, topics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postRequirement_topicSpinner.setAdapter(adapter);

        mMapView = findViewById(R.id.mapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        // Initialize the MapView with the Bundle
        mMapView.onCreate(mapViewBundle);

        // Load the map asynchronously
        mMapView.getMapAsync(this);

        // Set onClickListener for submit button
        postRequirement_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequirement();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void postRequirement() {
        // Get values from UI elements
        String schoolName = postRequirement_schoolNameEditText.getText().toString();
        String address = postRequirement_addressEditText.getText().toString();
        String phoneNumber = postRequirement_phoneEditText.getText().toString();
        String topic = postRequirement_topicSpinner.getSelectedItem().toString();

        if (schoolName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || topic.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle location permission not granted
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Create a Map object with the requirement data
                    Map<String, Object> requirementData = new HashMap<>();
                    requirementData.put("schoolName", schoolName);
                    requirementData.put("address", address);
                    requirementData.put("phoneNumber", phoneNumber);
                    requirementData.put("topic", topic);
                    // Add location data
                    requirementData.put("location", new GeoPoint(location.getLatitude(), location.getLongitude()));

                    // Get a reference to the Firestore collection "requirements"
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference requirementsRef = db.collection("requirements");

                    // Add the requirement data to Firestore
                    requirementsRef.add(requirementData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Get the auto-generated document ID
                                    String documentId = documentReference.getId();
                                    // Update the document with the document ID
                                    documentReference.update("documentId", documentId)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(PostRequirement.this, "Requirement posted successfully", Toast.LENGTH_SHORT).show();
                                                    // Add marker to the map
                                                    addMarkerToMap(location.getLatitude(), location.getLongitude(), schoolName);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PostRequirement.this, "Failed to update document", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostRequirement.this, "Failed to post requirement", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Handle case when location is null
                    Toast.makeText(PostRequirement.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addMarkerToMap(double latitude, double longitude, String schoolName) {
        if (mMap != null) {
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location).title(schoolName));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }
}
