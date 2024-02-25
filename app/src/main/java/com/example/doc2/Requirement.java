package com.example.doc2;
import com.google.firebase.firestore.GeoPoint;
import java.io.Serializable;


public class Requirement implements Serializable {
    private String schoolName;
    private String address;
    private String phoneNumber;
    private GeoPoint location;
    private String topic;
    private String documentId;
    // Constructors (if you have any)

    // Getter methods
    public String getSchoolName() {
        return schoolName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }


    public String getTopic() {
        return topic;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    // Setter methods
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public void setTopic(String topic) {
        this.topic = topic;
    }
}
