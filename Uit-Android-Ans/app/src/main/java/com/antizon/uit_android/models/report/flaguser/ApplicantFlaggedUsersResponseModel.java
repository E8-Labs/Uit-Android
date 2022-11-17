package com.antizon.uit_android.models.report.flaguser;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicantFlaggedUsersResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ApplicantFlagDataModel> flaggedApplicantsList;

    public ApplicantFlaggedUsersResponseModel(boolean status, String message, List<ApplicantFlagDataModel> flaggedApplicantsList) {
        this.status = status;
        this.message = message;
        this.flaggedApplicantsList = flaggedApplicantsList;
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

    public List<ApplicantFlagDataModel> getFlaggedApplicantsList() {
        return flaggedApplicantsList;
    }

    public void setFlaggedApplicantsList(List<ApplicantFlagDataModel> flaggedApplicantsList) {
        this.flaggedApplicantsList = flaggedApplicantsList;
    }
}
