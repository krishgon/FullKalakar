package com.krishapps.kalakarbuisness.CustomClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Artist implements Serializable {
    public Artist(String artistID, String name, String phoneNumber) {
        this.artistID = artistID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArtistID() {
        return artistID;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
    public int getCustomerServed() {
        return customerServed;
    }

    public void setCustomerServed(int customerServed) {
        this.customerServed = customerServed;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String artistID;
    private String name;
    private String phoneNumber;
    private String email;
    private String userName;
    private String city;
    private String skill;
    private Float rating;
    private int customerServed;
    private ArrayList<Service> services;

    public void addServiceToArtist(Service service){
        if(this.services == null){
            this.services = new ArrayList<Service>();
        }
        this.services.add(service);
    }




}
