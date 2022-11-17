package com.antizon.uit_android.models.applicant.degree;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApplicantDegreeDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("subtitle")
    private String subtitle;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    public ApplicantDegreeDataModel() {
    }

    public ApplicantDegreeDataModel(int id, String name, String subtitle, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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
        dest.writeString(this.subtitle);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.subtitle = source.readString();
        this.created_at = source.readString();
        this.updated_at = source.readString();
    }

    protected ApplicantDegreeDataModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.subtitle = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<ApplicantDegreeDataModel> CREATOR = new Parcelable.Creator<ApplicantDegreeDataModel>() {
        @Override
        public ApplicantDegreeDataModel createFromParcel(Parcel source) {
            return new ApplicantDegreeDataModel(source);
        }

        @Override
        public ApplicantDegreeDataModel[] newArray(int size) {
            return new ApplicantDegreeDataModel[size];
        }
    };
}
