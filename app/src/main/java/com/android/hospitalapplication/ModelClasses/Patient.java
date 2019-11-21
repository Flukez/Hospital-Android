package com.android.hospitalapplication.ModelClasses;

/**
 * Created by Gaurav on 20-12-2017.
 */

public class Patient extends User {

    private String bloodGroup;

    public Patient(String name, String age, String gender, String address,String contact, String bloodGroup) {
        super(name, age, gender, address, "Patient", contact);
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
