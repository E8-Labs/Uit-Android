package com.antizon.uit_android.models.work;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;

public class ApplicantReferenceModel implements Parcelable {
    private String name, countryCode, phoneNumber;
    private ApplicantJobDataModel applicantJobDataModel;

    public ApplicantReferenceModel() {
    }

    public ApplicantReferenceModel(String name, String countryCode, String phoneNumber, ApplicantJobDataModel applicantJobDataModel) {
        this.name = name;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.applicantJobDataModel = applicantJobDataModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ApplicantJobDataModel getApplicantJobDataModel() {
        return applicantJobDataModel;
    }

    public void setApplicantJobDataModel(ApplicantJobDataModel applicantJobDataModel) {
        this.applicantJobDataModel = applicantJobDataModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.countryCode);
        dest.writeString(this.phoneNumber);
        dest.writeParcelable(this.applicantJobDataModel, flags);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.countryCode = source.readString();
        this.phoneNumber = source.readString();
        this.applicantJobDataModel = source.readParcelable(ApplicantJobDataModel.class.getClassLoader());
    }

    protected ApplicantReferenceModel(Parcel in) {
        this.name = in.readString();
        this.countryCode = in.readString();
        this.phoneNumber = in.readString();
        this.applicantJobDataModel = in.readParcelable(ApplicantJobDataModel.class.getClassLoader());
    }

    public static final Creator<ApplicantReferenceModel> CREATOR = new Creator<ApplicantReferenceModel>() {
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
