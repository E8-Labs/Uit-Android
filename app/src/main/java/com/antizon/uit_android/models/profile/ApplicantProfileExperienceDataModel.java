package com.antizon.uit_android.models.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApplicantProfileExperienceDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("company")
    private String company;
    @SerializedName("department")
    private NameIdModel department;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("roles")
    private String roles;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("start_date")
    private String start_date;
    @SerializedName("end_date")
    private String end_date;

    public ApplicantProfileExperienceDataModel(int id, String company, NameIdModel department, String job_title, String roles, int user_id, String start_date, String end_date) {
        this.id = id;
        this.company = company;
        this.department = department;
        this.job_title = job_title;
        this.roles = roles;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public NameIdModel getDepartment() {
        return department;
    }

    public void setDepartment(NameIdModel department) {
        this.department = department;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.company);
        dest.writeParcelable(this.department, flags);
        dest.writeString(this.job_title);
        dest.writeString(this.roles);
        dest.writeInt(this.user_id);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.company = source.readString();
        this.department = source.readParcelable(NameIdModel.class.getClassLoader());
        this.job_title = source.readString();
        this.roles = source.readString();
        this.user_id = source.readInt();
        this.start_date = source.readString();
        this.end_date = source.readString();
    }

    protected ApplicantProfileExperienceDataModel(Parcel in) {
        this.id = in.readInt();
        this.company = in.readString();
        this.department = in.readParcelable(NameIdModel.class.getClassLoader());
        this.job_title = in.readString();
        this.roles = in.readString();
        this.user_id = in.readInt();
        this.start_date = in.readString();
        this.end_date = in.readString();
    }

    public static final Parcelable.Creator<ApplicantProfileExperienceDataModel> CREATOR = new Parcelable.Creator<ApplicantProfileExperienceDataModel>() {
        @Override
        public ApplicantProfileExperienceDataModel createFromParcel(Parcel source) {
            return new ApplicantProfileExperienceDataModel(source);
        }

        @Override
        public ApplicantProfileExperienceDataModel[] newArray(int size) {
            return new ApplicantProfileExperienceDataModel[size];
        }
    };
}
