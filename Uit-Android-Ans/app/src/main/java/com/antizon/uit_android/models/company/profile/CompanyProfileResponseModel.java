package com.antizon.uit_android.models.company.profile;

import com.google.gson.annotations.SerializedName;

public class CompanyProfileResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CompanyProfileDataModel dataModel;

    public CompanyProfileResponseModel() {
    }

    public CompanyProfileResponseModel(boolean status, String message, CompanyProfileDataModel dataModel) {
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

    public CompanyProfileDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(CompanyProfileDataModel dataModel) {
        this.dataModel = dataModel;
    }
}
