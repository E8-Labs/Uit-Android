package com.antizon.uit_android.models.applicant.filter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetIndustriesResponse {
    @SerializedName("data")
    private List<IndustryModel> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public void setData(List<IndustryModel> data){
        this.data = data;
    }

    public List<IndustryModel> getData(){
        return data;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public boolean isStatus(){
        return status;
    }
}
