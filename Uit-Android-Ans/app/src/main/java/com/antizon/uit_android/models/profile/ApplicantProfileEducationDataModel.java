package com.antizon.uit_android.models.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApplicantProfileEducationDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("field")
    private String field;
    @SerializedName("school_name")
    private String school_name;
    @SerializedName("start_date")
    private String start_date;
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("degree")
    private NameIdModel degree;
    @SerializedName("user_id")
    private int user_id;

    public ApplicantProfileEducationDataModel() {
    }

    public ApplicantProfileEducationDataModel(int id, String field, String school_name, String start_date, String end_date, NameIdModel degree, int user_id) {
        this.id = id;
        this.field = field;
        this.school_name = school_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.degree = degree;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
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

    public NameIdModel getDegree() {
        return degree;
    }

    public void setDegree(NameIdModel degree) {
        this.degree = degree;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.field);
        dest.writeString(this.school_name);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeParcelable(this.degree, flags);
        dest.writeInt(this.user_id);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.field = source.readString();
        this.school_name = source.readString();
        this.start_date = source.readString();
        this.end_date = source.readString();
        this.degree = source.readParcelable(NameIdModel.class.getClassLoader());
        this.user_id = source.readInt();
    }

    protected ApplicantProfileEducationDataModel(Parcel in) {
        this.id = in.readInt();
        this.field = in.readString();
        this.school_name = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.degree = in.readParcelable(NameIdModel.class.getClassLoader());
        this.user_id = in.readInt();
    }

    public static final Parcelable.Creator<ApplicantProfileEducationDataModel> CREATOR = new Parcelable.Creator<ApplicantProfileEducationDataModel>() {
        @Override
        public ApplicantProfileEducationDataModel createFromParcel(Parcel source) {
            return new ApplicantProfileEducationDataModel(source);
        }

        @Override
        public ApplicantProfileEducationDataModel[] newArray(int size) {
            return new ApplicantProfileEducationDataModel[size];
        }
    };
}
