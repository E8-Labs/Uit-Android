package com.antizon.uit_android.models.applicant.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicantJobFilterResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ApplicantHomeJobDataModel> jobsList;

    public ApplicantJobFilterResponseModel() {
    }

    public ApplicantJobFilterResponseModel(boolean status, String message, List<ApplicantHomeJobDataModel> jobsList) {
        this.status = status;
        this.message = message;
        this.jobsList = jobsList;
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

    public List<ApplicantHomeJobDataModel> getJobsList() {
        return jobsList;
    }

    public void setJobsList(List<ApplicantHomeJobDataModel> jobsList) {
        this.jobsList = jobsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.jobsList);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.jobsList = source.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
    }

    protected ApplicantJobFilterResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.jobsList = in.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
    }

    public static final Parcelable.Creator<ApplicantJobFilterResponseModel> CREATOR = new Parcelable.Creator<ApplicantJobFilterResponseModel>() {
        @Override
        public ApplicantJobFilterResponseModel createFromParcel(Parcel source) {
            return new ApplicantJobFilterResponseModel(source);
        }

        @Override
        public ApplicantJobFilterResponseModel[] newArray(int size) {
            return new ApplicantJobFilterResponseModel[size];
        }
    };
}
