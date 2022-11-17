package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetInboxResponse{

	@SerializedName("data")
	private List<InboxModel> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(List<InboxModel> data){
		this.data = data;
	}

	public List<InboxModel> getData(){
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