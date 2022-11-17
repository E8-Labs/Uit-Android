package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

public class SendMessageResponse {

	@SerializedName("data")
	private MessageModel data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(MessageModel data){
		this.data = data;
	}

	public MessageModel getData(){
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