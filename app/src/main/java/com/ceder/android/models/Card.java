package com.ceder.android.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Card {

    private String name;
    private String position;
    private String company;
    private String phone;
    private String email;
    private String addLine1;
    private String addLine2;
    private String addLine3;
    private String linkedin;

    public Card() {

    }

    public Card(String name, String position, String company, String phone, String email, String addLine1, String addLine2, String addLine3, String linkedin) {
        this.name = name;
        this.position = position;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.addLine1 = addLine1;
        this.addLine2 = addLine2;
        this.addLine3 = addLine3;
        this.linkedin = linkedin;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getAddLine3() {
        return addLine3;
    }

    public void setAddLine3(String addLine3) {
        this.addLine3 = addLine3;
    }
}
