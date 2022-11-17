package com.antizon.uit_android.models.community;

import com.google.gson.annotations.SerializedName;

public class MainResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;

    public MainResponseModel() {
    }

    public MainResponseModel(boolean status, String message) {
        this.status = status;
        this.message = message;
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
}
