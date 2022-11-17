package com.antizon.uit_android.models.admin;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAdminApplicantsResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<GenericApplicantDataModel> applicantsList;

    public GetAdminApplicantsResponseModel() {
    }

    public GetAdminApplicantsResponseModel(boolean status, String message, List<GenericApplicantDataModel> applicantsList) {
        this.status = status;
        this.message = message;
        this.applicantsList = applicantsList;
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

    public List<GenericApplicantDataModel> getApplicantsList() {
        return applicantsList;
    }

    public void setApplicantsList(List<GenericApplicantDataModel> applicantsList) {
        this.applicantsList = applicantsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.applicantsList);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.applicantsList = source.createTypedArrayList(GenericApplicantDataModel.CREATOR);
    }

    protected GetAdminApplicantsResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.applicantsList = in.createTypedArrayList(GenericApplicantDataModel.CREATOR);
    }

    public static final Creator<GetAdminApplicantsResponseModel> CREATOR = new Creator<GetAdminApplicantsResponseModel>() {
        @Override
        public GetAdminApplicantsResponseModel createFromParcel(Parcel source) {
            return new GetAdminApplicantsResponseModel(source);
        }

        @Override
        public GetAdminApplicantsResponseModel[] newArray(int size) {
            return new GetAdminApplicantsResponseModel[size];
        }
    };
}
