package com.antizon.uit_android.models.jobs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobsListResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<SingleJobDataModel> jobsList;

    public JobsListResponseModel() {
    }

    public JobsListResponseModel(boolean status, String message, List<SingleJobDataModel> jobsList) {
        this.status = status;
        this.message = message;
        this.jobsList = jobsList;
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

    public List<SingleJobDataModel> getJobsList() {
        return jobsList;
    }

    public void setJobsList(List<SingleJobDataModel> jobsList) {
        this.jobsList = jobsList;
    }
}
