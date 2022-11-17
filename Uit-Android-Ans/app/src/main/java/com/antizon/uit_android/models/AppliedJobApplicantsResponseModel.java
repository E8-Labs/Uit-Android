package com.antizon.uit_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppliedJobApplicantsResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<AppliedJobApplicantDataModel> applicantsLists;

    public AppliedJobApplicantsResponseModel(boolean status, String message, List<AppliedJobApplicantDataModel> applicantsLists) {
        this.status = status;
        this.message = message;
        this.applicantsLists = applicantsLists;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AppliedJobApplicantDataModel> getApplicantsLists() {
        return applicantsLists;
    }

    public void setApplicantsLists(List<AppliedJobApplicantDataModel> applicantsLists) {
        this.applicantsLists = applicantsLists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.applicantsLists);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.applicantsLists = source.createTypedArrayList(AppliedJobApplicantDataModel.CREATOR);
    }

    protected AppliedJobApplicantsResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.applicantsLists = in.createTypedArrayList(AppliedJobApplicantDataModel.CREATOR);
    }

    public static final Parcelable.Creator<AppliedJobApplicantsResponseModel> CREATOR = new Parcelable.Creator<AppliedJobApplicantsResponseModel>() {
        @Override
        public AppliedJobApplicantsResponseModel createFromParcel(Parcel source) {
            return new AppliedJobApplicantsResponseModel(source);
        }

        @Override
        public AppliedJobApplicantsResponseModel[] newArray(int size) {
            return new AppliedJobApplicantsResponseModel[size];
        }
    };
}
