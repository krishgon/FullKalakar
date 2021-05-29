package com.krishapps.kalakar;

public class Artist {
    public Artist(String name, String userName, String city, Float rating, String[] skillSet) {
        this.name = name;
        this.userName = userName;
        this.city = city;
        this.rating = rating;
        this.skillSet = skillSet;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String[] getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(String[] skillSet) {
        this.skillSet = skillSet;
    }

    private String name;
    private String userName;
    private String city;
    private Float rating;
    private String[] skillSet;
    //TODO: do something about the profile pic
}
