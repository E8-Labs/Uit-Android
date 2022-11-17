package com.antizon.uit_android.models.company.signup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanySignupResponseDataModel implements Parcelable {
    @SerializedName("profile")
    private CompanyProfileDataModel profileDataModel;
    @SerializedName("access_token")
    private CompanyAccessTokenModel accessTokenModel;

    public CompanySignupResponseDataModel() {
    }

    public CompanySignupResponseDataModel(CompanyProfileDataModel profileDataModel, CompanyAccessTokenModel accessTokenModel) {
        this.profileDataModel = profileDataModel;
        this.accessTokenModel = accessTokenModel;
    }

    public CompanyProfileDataModel getProfileDataModel() {
        return profileDataModel;
    }

    public void setProfileDataModel(CompanyProfileDataModel profileDataModel) {
        this.profileDataModel = profileDataModel;
    }

    public CompanyAccessTokenModel getAccessTokenModel() {
        return accessTokenModel;
    }

    public void setAccessTokenModel(CompanyAccessTokenModel accessTokenModel) {
        this.accessTokenModel = accessTokenModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.profileDataModel, flags);
        dest.writeParcelable(this.accessTokenModel, flags);
    }

    public void readFromParcel(Parcel source) {
        this.profileDataModel = source.readParcelable(CompanyProfileDataModel.class.getClassLoader());
        this.accessTokenModel = source.readParcelable(CompanyAccessTokenModel.class.getClassLoader());
    }

    protected CompanySignupResponseDataModel(Parcel in) {
        this.profileDataModel = in.readParcelable(CompanyProfileDataModel.class.getClassLoader());
        this.accessTokenModel = in.readParcelable(CompanyAccessTokenModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<CompanySignupResponseDataModel> CREATOR = new Parcelable.Creator<CompanySignupResponseDataModel>() {
        @Override
        public CompanySignupResponseDataModel createFromParcel(Parcel source) {
            return new CompanySignupResponseDataModel(source);
        }

        @Override
        public CompanySignupResponseDataModel[] newArray(int size) {
            return new CompanySignupResponseDataModel[size];
        }
    };
}
