package com.android.hospitalapplication.ModelClasses;

/**
 * Created by Gaurav on 20-12-2017.
 */

public class User {
    private String name,gender,type,age,address, phone;

    public User() {
    }

    public User(String name, String age, String gender, String address, String type, String phone) {
        this.name = name;
        this.gender = gender;
        this.type = type;
        //this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   /* public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    */public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
