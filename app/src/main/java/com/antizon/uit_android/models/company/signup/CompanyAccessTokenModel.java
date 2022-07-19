package com.antizon.uit_android.models.company.signup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanyAccessTokenModel implements Parcelable {
    @SerializedName("token")
    private String token;

    public CompanyAccessTokenModel() {
    }

    public CompanyAccessTokenModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
    }

    public void readFromParcel(Parcel source) {
        this.token = source.readString();
    }

    protected CompanyAccessTokenModel(Parcel in) {
        this.token = in.readString();
    }

    public static final Parcelable.Creator<CompanyAccessTokenModel> CREATOR = new Parcelable.Creator<CompanyAccessTokenModel>() {
        @Override
        public CompanyAccessTokenModel createFromParcel(Parcel source) {
            return new CompanyAccessTokenModel(source);
        }

        @Override
        public CompanyAccessTokenModel[] newArray(int size) {
            return new CompanyAccessTokenModel[size];
        }
    };
}
