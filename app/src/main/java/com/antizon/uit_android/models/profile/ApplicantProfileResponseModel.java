package com.antizon.uit_android.models.profile;

import com.google.gson.annotations.SerializedName;

public class ApplicantProfileResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ProfileResponseDataModel data;

    public ApplicantProfileResponseModel() {
    }

    public ApplicantProfileResponseModel(boolean status, String message, ProfileResponseDataModel data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public ProfileResponseDataModel getData() {
        return data;
    }

    public void setData(ProfileResponseDataModel data) {
        this.data = data;
    }
}
