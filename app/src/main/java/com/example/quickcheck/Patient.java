package com.example.quickcheck;

public class Patient {

    private String firstname;
    private String lastname;
    private String id;
    private String dob;
    private String gender;


    public Patient(String firstname, String lastname, String id, String dob, String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
        this.dob = dob;
        this.gender = gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
