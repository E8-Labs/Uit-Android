package com.antizon.uit_android.models.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApplicantPorfessionalExperienceRefrenceModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("phone")
    private String phone;
    @SerializedName("experience_id")
    private int experience_id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    public ApplicantPorfessionalExperienceRefrenceModel() {
    }

    public ApplicantPorfessionalExperienceRefrenceModel(int id, String name, String job_title, String phone, int experience_id, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.job_title = job_title;
        this.phone = phone;
        this.experience_id = experience_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperience_id() {
        return experience_id;
    }

    public void setExperience_id(int experience_id) {
        this.experience_id = experience_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.job_title);
        dest.writeString(this.phone);
        dest.writeInt(this.experience_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.job_title = source.readString();
        this.phone = source.readString();
        this.experience_id = source.readInt();
        this.created_at = source.readString();
        this.updated_at = source.readString();
    }

    protected ApplicantPorfessionalExperienceRefrenceModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.job_title = in.readString();
        this.phone = in.readString();
        this.experience_id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<ApplicantPorfessionalExperienceRefrenceModel> CREATOR = new Parcelable.Creator<ApplicantPorfessionalExperienceRefrenceModel>() {
        @Override
        public ApplicantPorfessionalExperienceRefrenceModel createFromParcel(Parcel source) {
            return new ApplicantPorfessionalExperienceRefrenceModel(source);
        }

        @Override
        public ApplicantPorfessionalExperienceRefrenceModel[] newArray(int size) {
            return new ApplicantPorfessionalExperienceRefrenceModel[size];
        }
    };
}
