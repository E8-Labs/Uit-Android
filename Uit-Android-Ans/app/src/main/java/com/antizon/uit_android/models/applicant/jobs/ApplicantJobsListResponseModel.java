package com.antizon.uit_android.models.applicant.jobs;


import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ApplicantJobsListResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<ApplicantJobDataModel> applicantJobsList;

    public ApplicantJobsListResponseModel() {
    }

    public ApplicantJobsListResponseModel(boolean status, String message, ArrayList<ApplicantJobDataModel> applicantJobsList) {
        this.status = status;
        this.message = message;
        this.applicantJobsList = applicantJobsList;
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

    public ArrayList<ApplicantJobDataModel> getApplicantJobsList() {
        return applicantJobsList;
    }

    public void setApplicantJobsList(ArrayList<ApplicantJobDataModel> applicantJobsList) {
        this.applicantJobsList = applicantJobsList;
    }
}
