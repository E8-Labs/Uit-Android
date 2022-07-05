package com.antizon.uit_android.models.work;

import android.os.Parcel;
import android.os.Parcelable;

public class ApplicantReferenceModel implements Parcelable {
    private String name, jobTitle, countryCode, phoneNumber;

    public ApplicantReferenceModel(String name, String jobTitle, String countryCode, String phoneNumber) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.jobTitle);
        dest.writeString(this.countryCode);
        dest.writeString(this.phoneNumber);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.jobTitle = source.readString();
        this.countryCode = source.readString();
        this.phoneNumber = source.readString();
    }

    protected ApplicantReferenceModel(Parcel in) {
        this.name = in.readString();
        this.jobTitle = in.readString();
        this.countryCode = in.readString();
        this.phoneNumber = in.readString();
    }

    public static final Parcelable.Creator<ApplicantReferenceModel> CREATOR = new Parcelable.Creator<ApplicantReferenceModel>() {
        @Override
        public ApplicantReferenceModel createFromParcel(Parcel source) {
            return new ApplicantReferenceModel(source);
        }

        @Override
        public ApplicantReferenceModel[] newArray(int size) {
            return new ApplicantReferenceModel[size];
        }
    };
}
