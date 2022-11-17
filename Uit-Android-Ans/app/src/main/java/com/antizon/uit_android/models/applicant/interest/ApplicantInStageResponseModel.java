package com.antizon.uit_android.models.applicant.interest;

import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ApplicantInStageResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<ModelCompanySize> companySizesList;

    public ApplicantInStageResponseModel() {
    }

    public ApplicantInStageResponseModel(boolean status, String message, ArrayList<ModelCompanySize> companySizesList) {
        this.status = status;
        this.message = message;
        this.companySizesList = companySizesList;
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

    public ArrayList<ModelCompanySize> getCompanySizesList() {
        return companySizesList;
    }

    public void setCompanySizesList(ArrayList<ModelCompanySize> companySizesList) {
        this.companySizesList = companySizesList;
    }
}
