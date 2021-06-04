package com.krishapps.kalakarbuisness;

import java.io.Serializable;

public class Artist implements Serializable {
    public Artist(String artistID, String name) {
        this.artistID = artistID;
        this.name = name;
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

    private String artistID;
    private String name;
    private String email;
    private String userName;
    private String city;
    private String skill;
}
