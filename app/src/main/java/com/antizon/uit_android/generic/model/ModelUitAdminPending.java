package com.antizon.uit_android.generic.model;

import java.io.Serializable;
import java.util.List;

public class ModelUitAdminPending implements Serializable{

    int id = 0, userId = 0, active_members = 0,team_lead=0
            ,programming=0,training=0, men=0, women=0,lgbt=0, erg=0;;
    String name = "", email = "", city = "", state = "", profile_image = "",phone="",bio="",job_title="", deistatement="";

    public ModelUitAdminPending() {
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

    public String getDeistatement() {
        return deistatement;
    }

    public void setDeistatement(String deistatement) {
        this.deistatement = deistatement;
    }

    public void setActive_members(int active_members) {
        this.active_members = active_members;
    }

    public String getName() {
        return name;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getTeam_lead() {
        return team_lead;
    }

    public void setTeam_lead(int team_lead) {
        this.team_lead = team_lead;
    }

    public int getProgramming() {
        return programming;
    }

    public void setProgramming(int programming) {
        this.programming = programming;
    }

    public int getTraining() {
        return training;
    }

    public void setTraining(int training) {
        this.training = training;
    }

    public int getMen() {
        return men;
    }

    public void setMen(int men) {
        this.men = men;
    }

    public int getWomen() {
        return women;
    }

    public void setWomen(int women) {
        this.women = women;
    }

    public int getLgbt() {
        return lgbt;
    }

    public void setLgbt(int lgbt) {
        this.lgbt = lgbt;
    }

    public int getErg() {
        return erg;
    }

    public void setErg(int erg) {
        this.erg = erg;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
