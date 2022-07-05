package com.antizon.uit_android.generic.model;

import java.io.Serializable;

public class ModelFlaggedUser implements Serializable {

    int id=0,role=0,user_id=0;
    String name="", profile_image="", job_title="",greenhouse_access_token="",comment="",created_at="";

    ModelUser flaggedDataModel;

    public ModelFlaggedUser() {
    }
    public int getId() {
        return id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public String getGreenhouse_access_token() {
        return greenhouse_access_token;
    }

    public void setGreenhouse_access_token(String greenhouse_access_token) {
        this.greenhouse_access_token = greenhouse_access_token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ModelUser getFlaggedDataModel() {
        return flaggedDataModel;
    }

    public void setFlaggedDataModel(ModelUser flaggedDataModel) {
        this.flaggedDataModel = flaggedDataModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
