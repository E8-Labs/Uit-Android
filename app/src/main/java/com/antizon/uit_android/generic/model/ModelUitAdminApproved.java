package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ModelUitAdminApproved implements Parcelable {

    int id = 0, userId = 0, active_members = 0,team_lead=0
            ,programming=0,training=0, men=0, women=0,lgbt=0, erg=0;
    String name = "", email = "", city = "", state = "", profile_image = "", deistatement="",phone="";

    public ModelUitAdminApproved() {
    }

    protected ModelUitAdminApproved(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        active_members = in.readInt();
        team_lead = in.readInt();
        programming = in.readInt();
        training = in.readInt();
        men = in.readInt();
        women = in.readInt();
        lgbt = in.readInt();
        erg = in.readInt();
        name = in.readString();
        email = in.readString();
        city = in.readString();
        state = in.readString();
        profile_image = in.readString();
        deistatement = in.readString();
        phone = in.readString();
    }


    public static final Creator<ModelUitAdminApproved> CREATOR = new Creator<ModelUitAdminApproved>() {
        @Override
        public ModelUitAdminApproved createFromParcel(Parcel in) {
            return new ModelUitAdminApproved(in);
        }

        @Override
        public ModelUitAdminApproved[] newArray(int size) {
            return new ModelUitAdminApproved[size];
        }
    };

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

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public String getDeistatement() {
        return deistatement;
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

    public void setDeistatement(String deistatement) {
        this.deistatement = deistatement;
    }


    public void setState(String state) {
        this.state = state;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public int getErg() {
        return erg;
    }

    public void setErg(int erg) {
        this.erg = erg;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeInt(active_members);
        parcel.writeInt(team_lead);
        parcel.writeInt(programming);
        parcel.writeInt(training);
        parcel.writeInt(men);
        parcel.writeInt(women);
        parcel.writeInt(lgbt);
        parcel.writeInt(erg);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(profile_image);
        parcel.writeString(deistatement);
        parcel.writeString(phone);
    }
}
