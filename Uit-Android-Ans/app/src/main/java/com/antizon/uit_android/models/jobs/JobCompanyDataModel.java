package com.antizon.uit_android.models.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class JobCompanyDataModel implements Parcelable {
    @SerializedName("website")
    private String website;
    @SerializedName("address")
    private String address;
    @SerializedName("role")
    private int role;
    @SerializedName("city")
    private String city;
    @SerializedName("bio")
    private String bio;
    @SerializedName("account_status")
    private int accountStatus;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("profile_banner")
    private String profileBanner;
    @SerializedName("phone")
    private String phone;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("dob")
    private String dob;
    @SerializedName("greenhouse_status")
    private int greenhouseStatus;
    @SerializedName("name")
    private String name;
    @SerializedName("application_status")
    private int applicationStatus;
    @SerializedName("id")
    private int id;
    @SerializedName("state")
    private String state;
    @SerializedName("job_title")
    private String jobTitle;
    @SerializedName("greenhouse_user_id")
    private String greenhouseUserId;
    @SerializedName("email")
    private String email;
    @SerializedName("greenhouse_access_token")
    private String greenhouseAccessToken;

    public JobCompanyDataModel() {
    }

    public JobCompanyDataModel(String website, String address, int role, String city, String bio, int accountStatus, String profileImage, String profileBanner, String phone, int userId, String dob, int greenhouseStatus, String name, int applicationStatus, int id, String state, String jobTitle, String greenhouseUserId, String email, String greenhouseAccessToken) {
        this.website = website;
        this.address = address;
        this.role = role;
        this.city = city;
        this.bio = bio;
        this.accountStatus = accountStatus;
        this.profileImage = profileImage;
        this.profileBanner = profileBanner;
        this.phone = phone;
        this.userId = userId;
        this.dob = dob;
        this.greenhouseStatus = greenhouseStatus;
        this.name = name;
        this.applicationStatus = applicationStatus;
        this.id = id;
        this.state = state;
        this.jobTitle = jobTitle;
        this.greenhouseUserId = greenhouseUserId;
        this.email = email;
        this.greenhouseAccessToken = greenhouseAccessToken;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileBanner() {
        return profileBanner;
    }

    public void setProfileBanner(String profileBanner) {
        this.profileBanner = profileBanner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getGreenhouseStatus() {
        return greenhouseStatus;
    }

    public void setGreenhouseStatus(int greenhouseStatus) {
        this.greenhouseStatus = greenhouseStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(int applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getGreenhouseUserId() {
        return greenhouseUserId;
    }

    public void setGreenhouseUserId(String greenhouseUserId) {
        this.greenhouseUserId = greenhouseUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGreenhouseAccessToken() {
        return greenhouseAccessToken;
    }

    public void setGreenhouseAccessToken(String greenhouseAccessToken) {
        this.greenhouseAccessToken = greenhouseAccessToken;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.website);
        dest.writeString(this.address);
        dest.writeInt(this.role);
        dest.writeString(this.city);
        dest.writeString(this.bio);
        dest.writeInt(this.accountStatus);
        dest.writeString(this.profileImage);
        dest.writeString(this.profileBanner);
        dest.writeString(this.phone);
        dest.writeInt(this.userId);
        dest.writeString(this.dob);
        dest.writeInt(this.greenhouseStatus);
        dest.writeString(this.name);
        dest.writeInt(this.applicationStatus);
        dest.writeInt(this.id);
        dest.writeString(this.state);
        dest.writeString(this.jobTitle);
        dest.writeString(this.greenhouseUserId);
        dest.writeString(this.email);
        dest.writeString(this.greenhouseAccessToken);
    }

    public void readFromParcel(Parcel source) {
        this.website = source.readString();
        this.address = source.readString();
        this.role = source.readInt();
        this.city = source.readString();
        this.bio = source.readString();
        this.accountStatus = source.readInt();
        this.profileImage = source.readString();
        this.profileBanner = source.readString();
        this.phone = source.readString();
        this.userId = source.readInt();
        this.dob = source.readString();
        this.greenhouseStatus = source.readInt();
        this.name = source.readString();
        this.applicationStatus = source.readInt();
        this.id = source.readInt();
        this.state = source.readString();
        this.jobTitle = source.readString();
        this.greenhouseUserId = source.readString();
        this.email = source.readString();
        this.greenhouseAccessToken = source.readString();
    }

    protected JobCompanyDataModel(Parcel in) {
        this.website = in.readString();
        this.address = in.readString();
        this.role = in.readInt();
        this.city = in.readString();
        this.bio = in.readString();
        this.accountStatus = in.readInt();
        this.profileImage = in.readString();
        this.profileBanner = in.readString();
        this.phone = in.readString();
        this.userId = in.readInt();
        this.dob = in.readString();
        this.greenhouseStatus = in.readInt();
        this.name = in.readString();
        this.applicationStatus = in.readInt();
        this.id = in.readInt();
        this.state = in.readString();
        this.jobTitle = in.readString();
        this.greenhouseUserId = in.readString();
        this.email = in.readString();
        this.greenhouseAccessToken = in.readString();
    }

    public static final Parcelable.Creator<JobCompanyDataModel> CREATOR = new Parcelable.Creator<JobCompanyDataModel>() {
        @Override
        public JobCompanyDataModel createFromParcel(Parcel source) {
            return new JobCompanyDataModel(source);
        }

        @Override
        public JobCompanyDataModel[] newArray(int size) {
            return new JobCompanyDataModel[size];
        }
    };
}
