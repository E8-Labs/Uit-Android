package com.antizon.uit_android.models.jobs;

import com.google.gson.annotations.SerializedName;

public class JobDetailsResponseModel {
    @SerializedName("data")
    private JobDetailsDataModel data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public JobDetailsDataModel getData() {
        return data;
    }

    public void setData(JobDetailsDataModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
