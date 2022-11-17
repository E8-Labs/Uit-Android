package com.antizon.uit_android.generic.model;

import java.io.Serializable;

public class ModelCompanyInvite implements Serializable {

    int id;
    String name;
    String email, profile_image;

    public ModelCompanyInvite() {
    }



    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
