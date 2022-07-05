package com.antizon.uit_android.models.home;

import com.google.gson.annotations.SerializedName;

public class AdminHomeResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private AdminHomeDataModel adminHomeDataModel;

    public AdminHomeResponseModel() {
    }

    public AdminHomeResponseModel(boolean status, String message, AdminHomeDataModel adminHomeDataModel) {
        this.status = status;
        this.message = message;
        this.adminHomeDataModel = adminHomeDataModel;
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

    public AdminHomeDataModel getAdminHomeDataModel() {
        return adminHomeDataModel;
    }

    public void setAdminHomeDataModel(AdminHomeDataModel adminHomeDataModel) {
        this.adminHomeDataModel = adminHomeDataModel;
    }
}
