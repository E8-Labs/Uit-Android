package com.antizon.uit_android.models.applicant.degree;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicantDegreesListResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ApplicantDegreeDataModel> applicantDegreesList;

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

    public List<ApplicantDegreeDataModel> getApplicantDegreesList() {
        return applicantDegreesList;
    }

    public void setApplicantDegreesList(List<ApplicantDegreeDataModel> applicantDegreesList) {
        this.applicantDegreesList = applicantDegreesList;
    }
}
