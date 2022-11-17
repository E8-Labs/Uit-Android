package com.antizon.uit_android.generic.model;

import java.io.Serializable;

public class ModelFlaggedJob implements Serializable {

    int id=0,role=0,user_id=0,location_status=0,employment_type=0,job_status=0,min_salary=0,max_salary=0,total_applications=0;
    String name="", profile_image="", job_title="",greenhouse_access_token="",comment="",created_at="",city="",state="",reason_to_close="";

    ModelUser flaggedDataModel;

    public ModelFlaggedJob() {
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

    public String getReason_to_close() {
        return reason_to_close;
    }

    public void setReason_to_close(String reason_to_close) {
        this.reason_to_close = reason_to_close;
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

    public int getTotal_applications() {
        return total_applications;
    }

    public void setTotal_applications(int total_applications) {
        this.total_applications = total_applications;
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

    public int getLocation_status() {
        return location_status;
    }

    public void setLocation_status(int location_status) {
        this.location_status = location_status;
    }

    public int getEmployment_type() {
        return employment_type;
    }

    public void setEmployment_type(int employment_type) {
        this.employment_type = employment_type;
    }

    public int getJob_status() {
        return job_status;
    }

    public void setJob_status(int job_status) {
        this.job_status = job_status;
    }

    public int getMin_salary() {
        return min_salary;
    }

    public void setMin_salary(int min_salary) {
        this.min_salary = min_salary;
    }

    public int getMax_salary() {
        return max_salary;
    }

    public void setMax_salary(int max_salary) {
        this.max_salary = max_salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
