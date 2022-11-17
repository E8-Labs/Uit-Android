package com.antizon.uit_android.notifications.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNotificationsResponse {
    @SerializedName("data")
    private List<NotificationModel> data;

    @SerializedName("offset")
    private int offset;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public void setData(List<NotificationModel> data){
        this.data = data;
    }

    public List<NotificationModel> getData(){
        return data;
    }

    public void setOffset(int offset){
        this.offset = offset;
    }

    public int getOffset(){
        return offset;
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
