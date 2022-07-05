package com.antizon.uit_android.models.applicant.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GenericUserDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("role")
    private int role;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("greenhouse_access_token")
    private String greenhouse_access_token;

    public GenericUserDataModel() {
    }

    public GenericUserDataModel(int id, String name, int role, String profile_image, String job_title, int user_id, String greenhouse_access_token) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.profile_image = profile_image;
        this.job_title = job_title;
        this.user_id = user_id;
        this.greenhouse_access_token = greenhouse_access_token;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGreenhouse_access_token() {
        return greenhouse_access_token;
    }

    public void setGreenhouse_access_token(String greenhouse_access_token) {
        this.greenhouse_access_token = greenhouse_access_token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.role);
        dest.writeString(this.profile_image);
        dest.writeString(this.job_title);
        dest.writeInt(this.user_id);
        dest.writeString(this.greenhouse_access_token);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.role = source.readInt();
        this.profile_image = source.readString();
        this.job_title = source.readString();
        this.user_id = source.readInt();
        this.greenhouse_access_token = source.readString();
    }

    protected GenericUserDataModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.role = in.readInt();
        this.profile_image = in.readString();
        this.job_title = in.readString();
        this.user_id = in.readInt();
        this.greenhouse_access_token = in.readString();
    }

    public static final Parcelable.Creator<GenericUserDataModel> CREATOR = new Parcelable.Creator<GenericUserDataModel>() {
        @Override
        public GenericUserDataModel createFromParcel(Parcel source) {
            return new GenericUserDataModel(source);
        }

        @Override
        public GenericUserDataModel[] newArray(int size) {
            return new GenericUserDataModel[size];
        }
    };
}
