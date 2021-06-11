package com.krishapps.kalakar.customClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Integer getCustomerServed() {
        return customerServed;
    }

    public void setCustomerServed(Integer customerServed) {
        this.customerServed = customerServed;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    private String name;
    private String userName;
    private String city;
    private Float rating;
    private String skill;
    private Integer customerServed;
    private ArrayList<Service> services;

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    private String documentID;


    public Artist(String name, String userName, String city, Float rating, String skill, String documentID) {
        this.name = name;
        this.userName = userName;
        this.city = city;
        this.rating = rating;
        this.skill = skill;
        this.documentID = documentID;
    }
    //TODO: do something about the profile pic
}
