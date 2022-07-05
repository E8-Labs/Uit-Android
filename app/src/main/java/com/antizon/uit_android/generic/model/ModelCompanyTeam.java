package com.antizon.uit_android.generic.model;

import java.io.Serializable;
import java.util.List;

public class ModelCompanyTeam implements Serializable {

    int id = 0, userId = 0, active_members = 0;
    String name = "", email = "", city = "", state = "", profile_image = "";

    public ModelCompanyTeam() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActive_members() {
        return active_members;
    }

    public void setActive_members(int active_members) {
        this.active_members = active_members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
