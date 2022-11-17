package com.antizon.uit_android.recruiter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.generic.model.ModelRecruiterFindCompany;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCompaniesResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ModelRecruiterFindCompany> companyList;

    public GetCompaniesResponseModel() {
    }
    public GetCompaniesResponseModel(boolean status, String message, List<ModelRecruiterFindCompany> companyList) {
        this.status = status;
        this.message = message;
        this.companyList = companyList;
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

    public List<ModelRecruiterFindCompany> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<ModelRecruiterFindCompany> companyList) {
        this.companyList = companyList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.companyList);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.companyList = source.createTypedArrayList(ModelRecruiterFindCompany.CREATOR);
    }

    protected GetCompaniesResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.companyList = in.createTypedArrayList(ModelRecruiterFindCompany.CREATOR);
    }

    public static final Parcelable.Creator<GetCompaniesResponseModel> CREATOR = new Parcelable.Creator<GetCompaniesResponseModel>() {
        @Override
        public GetCompaniesResponseModel createFromParcel(Parcel source) {
            return new GetCompaniesResponseModel(source);
        }

        @Override
        public GetCompaniesResponseModel[] newArray(int size) {
            return new GetCompaniesResponseModel[size];
        }
    };
}
