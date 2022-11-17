package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.profile.NameIdModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelRecruiterFindCompany implements Serializable, Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("active_members")
    private int active_members;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("industries")
    private List<NameIdModel> industriesList;
    @SerializedName("greenhouse_access_token")
    private String greenhouse_access_token;
    @SerializedName("greenhouse_user_id")
    private String greenhouse_user_id;
    @SerializedName("greenhouse_status")
    private int greenhouse_status;

    public ModelRecruiterFindCompany() {
    }

    public ModelRecruiterFindCompany(int id, String name, String email, String city, String state, String job_title, int active_members, String profile_image, int user_id, List<NameIdModel> industriesList, String greenhouse_access_token, String greenhouse_user_id, int greenhouse_status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.state = state;
        this.job_title = job_title;
        this.active_members = active_members;
        this.profile_image = profile_image;
        this.user_id = user_id;
        this.industriesList = industriesList;
        this.greenhouse_access_token = greenhouse_access_token;
        this.greenhouse_user_id = greenhouse_user_id;
        this.greenhouse_status = greenhouse_status;
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

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public int getActive_members() {
        return active_members;
    }

    public void setActive_members(int active_members) {
        this.active_members = active_members;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<NameIdModel> getIndustriesList() {
        return industriesList;
    }

    public void setIndustriesList(List<NameIdModel> industriesList) {
        this.industriesList = industriesList;
    }

    public String getGreenhouse_access_token() {
        return greenhouse_access_token;
    }

    public void setGreenhouse_access_token(String greenhouse_access_token) {
        this.greenhouse_access_token = greenhouse_access_token;
    }

    public String getGreenhouse_user_id() {
        return greenhouse_user_id;
    }

    public void setGreenhouse_user_id(String greenhouse_user_id) {
        this.greenhouse_user_id = greenhouse_user_id;
    }

    public int getGreenhouse_status() {
        return greenhouse_status;
    }

    public void setGreenhouse_status(int greenhouse_status) {
        this.greenhouse_status = greenhouse_status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.job_title);
        dest.writeInt(this.active_members);
        dest.writeString(this.profile_image);
        dest.writeInt(this.user_id);
        dest.writeTypedList(this.industriesList);
        dest.writeString(this.greenhouse_access_token);
        dest.writeString(this.greenhouse_user_id);
        dest.writeInt(this.greenhouse_status);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.email = source.readString();
        this.city = source.readString();
        this.state = source.readString();
        this.job_title = source.readString();
        this.active_members = source.readInt();
        this.profile_image = source.readString();
        this.user_id = source.readInt();
        this.industriesList = source.createTypedArrayList(NameIdModel.CREATOR);
        this.greenhouse_access_token = source.readString();
        this.greenhouse_user_id = source.readString();
        this.greenhouse_status = source.readInt();
    }

    protected ModelRecruiterFindCompany(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.job_title = in.readString();
        this.active_members = in.readInt();
        this.profile_image = in.readString();
        this.user_id = in.readInt();
        this.industriesList = in.createTypedArrayList(NameIdModel.CREATOR);
        this.greenhouse_access_token = in.readString();
        this.greenhouse_user_id = in.readString();
        this.greenhouse_status = in.readInt();
    }

    public static final Creator<ModelRecruiterFindCompany> CREATOR = new Creator<ModelRecruiterFindCompany>() {
        @Override
        public ModelRecruiterFindCompany createFromParcel(Parcel source) {
            return new ModelRecruiterFindCompany(source);
        }

        @Override
        public ModelRecruiterFindCompany[] newArray(int size) {
            return new ModelRecruiterFindCompany[size];
        }
    };
}
