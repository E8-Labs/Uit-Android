package com.antizon.uit_android.models.company.signup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanySignupResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CompanySignupResponseDataModel dataModel;

    public CompanySignupResponseModel() {
    }

    public CompanySignupResponseModel(boolean status, String message, CompanySignupResponseDataModel dataModel) {
        this.status = status;
        this.message = message;
        this.dataModel = dataModel;
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

    public CompanySignupResponseDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(CompanySignupResponseDataModel dataModel) {
        this.dataModel = dataModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeParcelable(this.dataModel, flags);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.dataModel = source.readParcelable(CompanySignupResponseDataModel.class.getClassLoader());
    }

    protected CompanySignupResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.dataModel = in.readParcelable(CompanySignupResponseDataModel.class.getClassLoader());
    }

    public static final Creator<CompanySignupResponseModel> CREATOR = new Creator<CompanySignupResponseModel>() {
        @Override
        public CompanySignupResponseModel createFromParcel(Parcel source) {
            return new CompanySignupResponseModel(source);
        }

        @Override
        public CompanySignupResponseModel[] newArray(int size) {
            return new CompanySignupResponseModel[size];
        }
    };
}
