package com.example.doc2;

// Volunteer.java
public class Volunteer {
    private String volunteerUsername;
    private String volunteerName;
    private String volunteerRole;

    // Default constructor required for Firestore
    public Volunteer() {
    }

    public Volunteer(String volunteerUsername, String volunteerName) {
        this.volunteerUsername = volunteerUsername;
        this.volunteerName = volunteerName;
        this.volunteerRole = "volunteer";
    }

    public String getVolunteerUsername() {
        return volunteerUsername;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public String getVolunteerRole() {
        return volunteerRole;
    }
}
