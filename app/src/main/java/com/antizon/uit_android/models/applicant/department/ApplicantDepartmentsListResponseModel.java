package com.antizon.uit_android.models.applicant.department;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApplicantDepartmentsListResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<ApplicantDepartmentDataModel> departmentsList;

    public ApplicantDepartmentsListResponseModel() {
    }

    public ApplicantDepartmentsListResponseModel(boolean status, String message, ArrayList<ApplicantDepartmentDataModel> departmentsList) {
        this.status = status;
        this.message = message;
        this.departmentsList = departmentsList;
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

    public ArrayList<ApplicantDepartmentDataModel> getDepartmentsList() {
        return departmentsList;
    }

    public void setDepartmentsList(ArrayList<ApplicantDepartmentDataModel> departmentsList) {
        this.departmentsList = departmentsList;
    }
}
