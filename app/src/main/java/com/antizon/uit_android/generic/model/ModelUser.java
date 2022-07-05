package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ModelUser implements Parcelable {

    int id = 0, role = 0, user_id = 0;

    String name = "", profile_image = "", job_title = "", greenhouse_access_token = "";

    public ModelUser() {
    }

    protected ModelUser(Parcel in) {
        id = in.readInt();
        role = in.readInt();
        user_id = in.readInt();
        name = in.readString();
        profile_image = in.readString();
        job_title = in.readString();
        greenhouse_access_token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(role);
        dest.writeInt(user_id);
        dest.writeString(name);
        dest.writeString(profile_image);
        dest.writeString(job_title);
        dest.writeString(greenhouse_access_token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelUser> CREATOR = new Creator<ModelUser>() {
        @Override
        public ModelUser createFromParcel(Parcel in) {
            return new ModelUser(in);
        }

        @Override
        public ModelUser[] newArray(int size) {
            return new ModelUser[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
