package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetConversationResponse {

	@SerializedName("data")
	private List<MessageModel> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(List<MessageModel> data){
		this.data = data;
	}

	public List<MessageModel> getData(){
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