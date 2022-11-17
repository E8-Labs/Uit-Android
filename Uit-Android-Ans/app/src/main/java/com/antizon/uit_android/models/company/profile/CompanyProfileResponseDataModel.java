package com.antizon.uit_android.models.company.profile;

import com.antizon.uit_android.models.company.signup.CompanyAccessTokenModel;
import com.google.gson.annotations.SerializedName;

public class CompanyProfileResponseDataModel {
    @SerializedName("profile")
    private CompanyProfileDataModel profileDataModel;
    @SerializedName("access_token")
    private CompanyAccessTokenModel tokenModel;

    public CompanyProfileResponseDataModel() {
    }

    public CompanyProfileResponseDataModel(CompanyProfileDataModel profileDataModel, CompanyAccessTokenModel tokenModel) {
        this.profileDataModel = profileDataModel;
        this.tokenModel = tokenModel;
    }

    public CompanyProfileDataModel getProfileDataModel() {
        return profileDataModel;
    }

    public void setProfileDataModel(CompanyProfileDataModel profileDataModel) {
        this.profileDataModel = profileDataModel;
    }

    public CompanyAccessTokenModel getTokenModel() {
        return tokenModel;
    }

    public void setTokenModel(CompanyAccessTokenModel tokenModel) {
        this.tokenModel = tokenModel;
    }

}
