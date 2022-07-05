package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

public class MessageModel {

	@SerializedName("image_height")
	private String imageHeight;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("image_width")
	private String imageWidth;

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private ChatUserModel user;

	@SerializedName("chat_id")
	private int chatId;

	public void setImageHeight(String imageHeight){
		this.imageHeight = imageHeight;
	}

	public String getImageHeight(){
		return imageHeight;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setImageWidth(String imageWidth){
		this.imageWidth = imageWidth;
	}

	public String getImageWidth(){
		return imageWidth;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUser(ChatUserModel user){
		this.user = user;
	}

	public ChatUserModel getUser(){
		return user;
	}

	public void setChatId(int chatId){
		this.chatId = chatId;
	}

	public int getChatId(){
		return chatId;
	}
}