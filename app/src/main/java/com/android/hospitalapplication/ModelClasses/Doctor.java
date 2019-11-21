package com.android.hospitalapplication.ModelClasses;

/**
 * Created by Gaurav on 20-12-2017.
 */

public class Doctor{
    private String name,gender,type,age,address, phone;

    private String doctor_reg_id,speciality;
    public Doctor(){

    }

    public Doctor(String name, String gender, String type, String age, String address, String phone, String doctor_reg_id, String speciality) {
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.doctor_reg_id = doctor_reg_id;
        this.speciality = speciality;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
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

    public String getDoctor_reg_id() {
        return doctor_reg_id;
    }

    public void setDoctor_reg_id(String doctor_reg_id) {
        this.doctor_reg_id = doctor_reg_id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
