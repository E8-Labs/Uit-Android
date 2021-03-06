package com.antizon.uit_android.models.company.signup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanyProfileDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_banner")
    private String profile_banner;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("website")
    private String website;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("phone")
    private String phone;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("account_status")
    private int account_status;
    @SerializedName("address")
    private String address;
    @SerializedName("bio")
    private String bio;
    @SerializedName("dob")
    private String dob;
    @SerializedName("role")
    private int role;
    @SerializedName("application_status")
    private int application_status;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("greenhouse_access_token")
    private String greenhouse_access_token;
    @SerializedName("greenhouse_user_id")
    private String greenhouse_user_id;
    @SerializedName("greenhouse_status")
    private int greenhouse_status;

    public CompanyProfileDataModel() {
    }

    public CompanyProfileDataModel(int id, String email, String name, String profile_banner, String profile_image, String website, String city, String state, String phone, String job_title, int account_status, String address, String bio, String dob, int role, int application_status, int user_id, String greenhouse_access_token, String greenhouse_user_id, int greenhouse_status) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile_banner = profile_banner;
        this.profile_image = profile_image;
        this.website = website;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.job_title = job_title;
        this.account_status = account_status;
        this.address = address;
        this.bio = bio;
        this.dob = dob;
        this.role = role;
        this.application_status = application_status;
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_banner() {
        return profile_banner;
    }

    public void setProfile_banner(String profile_banner) {
        this.profile_banner = profile_banner;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public int getAccount_status() {
        return account_status;
    }

    public void setAccount_status(int account_status) {
        this.account_status = account_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getApplication_status() {
        return application_status;
    }

    public void setApplication_status(int application_status) {
        this.application_status = application_status;
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
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.profile_banner);
        dest.writeString(this.profile_image);
        dest.writeString(this.website);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.phone);
        dest.writeString(this.job_title);
        dest.writeInt(this.account_status);
        dest.writeString(this.address);
        dest.writeString(this.bio);
        dest.writeString(this.dob);
        dest.writeInt(this.role);
        dest.writeInt(this.application_status);
        dest.writeInt(this.user_id);
        dest.writeString(this.greenhouse_access_token);
        dest.writeString(this.greenhouse_user_id);
        dest.writeInt(this.greenhouse_status);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.email = source.readString();
        this.name = source.readString();
        this.profile_banner = source.readString();
        this.profile_image = source.readString();
        this.website = source.readString();
        this.city = source.readString();
        this.state = source.readString();
        this.phone = source.readString();
        this.job_title = source.readString();
        this.account_status = source.readInt();
        this.address = source.readString();
        this.bio = source.readString();
        this.dob = source.readString();
        this.role = source.readInt();
        this.application_status = source.readInt();
        this.user_id = source.readInt();
        this.greenhouse_access_token = source.readString();
        this.greenhouse_user_id = source.readString();
        this.greenhouse_status = source.readInt();
    }

    protected CompanyProfileDataModel(Parcel in) {
        this.id = in.readInt();
        this.email = in.readString();
        this.name = in.readString();
        this.profile_banner = in.readString();
        this.profile_image = in.readString();
        this.website = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.phone = in.readString();
        this.job_title = in.readString();
        this.account_status = in.readInt();
        this.address = in.readString();
        this.bio = in.readString();
        this.dob = in.readString();
        this.role = in.readInt();
        this.application_status = in.readInt();
        this.user_id = in.readInt();
        this.greenhouse_access_token = in.readString();
        this.greenhouse_user_id = in.readString();
        this.greenhouse_status = in.readInt();
    }

    public static final Parcelable.Creator<CompanyProfileDataModel> CREATOR = new Parcelable.Creator<CompanyProfileDataModel>() {
        @Override
        public CompanyProfileDataModel createFromParcel(Parcel source) {
            return new CompanyProfileDataModel(source);
        }

        @Override
        public CompanyProfileDataModel[] newArray(int size) {
            return new CompanyProfileDataModel[size];
        }
    };
}
