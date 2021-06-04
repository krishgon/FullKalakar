package com.krishapps.kalakar.customClasses;

import java.io.Serializable;

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

    public Service[] getServices() {
        return services;
    }

    public void setServices(Service[] services) {
        this.services = services;
    }

    private String name;
    private String userName;
    private String city;
    private Float rating;
    private String skill;
    private Integer customerServed;
    private Service[] services;


    public Artist(String name, String userName, String city, Float rating, String skill) {
        this.name = name;
        this.userName = userName;
        this.city = city;
        this.rating = rating;
        this.skill = skill;
    }
    //TODO: do something about the profile pic
}
