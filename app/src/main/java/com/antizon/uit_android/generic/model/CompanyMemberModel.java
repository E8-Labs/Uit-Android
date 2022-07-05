package com.antizon.uit_android.generic.model;

import java.io.Serializable;
import java.util.List;

public class CompanyMemberModel implements Serializable {

    int id = 0, user_id = 0,active_members=0;
    String name = "", email = "", city = "", state = "", profile_image = "",job_title="", greenhouse_access_token="";

    public CompanyMemberModel() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getActive_members() {
        return active_members;
    }

    public void setActive_members(int active_members) {
        this.active_members = active_members;
    }

    public String getGreenhouse_access_token() {
        return greenhouse_access_token;
    }

    public void setGreenhouse_access_token(String greenhouse_access_token) {
        this.greenhouse_access_token = greenhouse_access_token;
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


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
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
