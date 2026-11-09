
package com.contacthub.model;

import java.io.Serializable;

public class Contact implements Serializable {

    private static final long serialVersionUID = -7035724248758771490L;

    private String name;
    private String phone;
    private String alternatePhone;
    private String email;
    private String address;
    private String category;
    private String companyName;
    private String designation;
    private String birthday;
    private String notes;
    private boolean favourite;

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone=phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone=alternatePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address=address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category=category;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName=companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation=designation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday=birthday;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes=notes;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite=favourite;
    }
}
