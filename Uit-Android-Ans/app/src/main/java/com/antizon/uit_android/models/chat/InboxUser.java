package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

public class InboxUser {

	@SerializedName("parent")
	private Parent parent;

	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("role")
	private int role;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("name")
	private String name;

	@SerializedName("unread_messages")
	private int unreadMessages;

	@SerializedName("id")
	private int id;

	public void setParent(Parent parent){
		this.parent = parent;
	}

	public Parent getParent(){
		return parent;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setRole(int role){
		this.role = role;
	}

	public int getRole(){
		return role;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUnreadMessages(int unreadMessages){
		this.unreadMessages = unreadMessages;
	}

	public int getUnreadMessages(){
		return unreadMessages;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}