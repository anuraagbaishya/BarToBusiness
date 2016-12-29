package com.appex.bartobusiness.models;

import io.realm.RealmObject;

/**
 * Created by anuraag on 12/29/16.
 */

public class RealmCard extends RealmObject {

    private String name;
    private String position;
    private String company;
    private String phone;
    private String email;
    private String addLine1;
    private String addLine2;
    private String addLine3;

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
