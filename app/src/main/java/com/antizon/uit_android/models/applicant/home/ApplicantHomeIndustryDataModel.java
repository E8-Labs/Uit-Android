package com.antizon.uit_android.models.applicant.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApplicantHomeIndustryDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public ApplicantHomeIndustryDataModel() {
    }

    public ApplicantHomeIndustryDataModel(int id, String name) {
        this.id = id;
        this.name = name;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
    }

    protected ApplicantHomeIndustryDataModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ApplicantHomeIndustryDataModel> CREATOR = new Parcelable.Creator<ApplicantHomeIndustryDataModel>() {
        @Override
        public ApplicantHomeIndustryDataModel createFromParcel(Parcel source) {
            return new ApplicantHomeIndustryDataModel(source);
        }

        @Override
        public ApplicantHomeIndustryDataModel[] newArray(int size) {
            return new ApplicantHomeIndustryDataModel[size];
        }
    };
}
