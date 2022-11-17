package com.antizon.uit_android.models.applicant.home;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ApplicantHomeResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ApplicantHomeResponseDataModel applicantHomeResponseDataModel;

    public ApplicantHomeResponseModel() {
    }

    public ApplicantHomeResponseModel(boolean status, String message, ApplicantHomeResponseDataModel applicantHomeResponseDataModel) {
        this.status = status;
        this.message = message;
        this.applicantHomeResponseDataModel = applicantHomeResponseDataModel;
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

    public ApplicantHomeResponseDataModel getApplicantHomeResponseDataModel() {
        return applicantHomeResponseDataModel;
    }

    public void setApplicantHomeResponseDataModel(ApplicantHomeResponseDataModel applicantHomeResponseDataModel) {
        this.applicantHomeResponseDataModel = applicantHomeResponseDataModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeParcelable(this.applicantHomeResponseDataModel, flags);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.applicantHomeResponseDataModel = source.readParcelable(ApplicantHomeResponseDataModel.class.getClassLoader());
    }

    protected ApplicantHomeResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.applicantHomeResponseDataModel = in.readParcelable(ApplicantHomeResponseDataModel.class.getClassLoader());
    }

    public static final Creator<ApplicantHomeResponseModel> CREATOR = new Creator<ApplicantHomeResponseModel>() {
        @Override
        public ApplicantHomeResponseModel createFromParcel(Parcel source) {
            return new ApplicantHomeResponseModel(source);
        }

        @Override
        public ApplicantHomeResponseModel[] newArray(int size) {
            return new ApplicantHomeResponseModel[size];
        }
    };
}
