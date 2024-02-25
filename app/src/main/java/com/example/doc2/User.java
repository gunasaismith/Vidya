package com.example.doc2;

// User.java
public class User {
    private String username;
    private String name;
    private String role;

    // Default constructor required for Firestore
    public User() {
    }

    public User(String username, String name) {
        this.username = username;
        this.name = name;
        this.role = "teacher";
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
