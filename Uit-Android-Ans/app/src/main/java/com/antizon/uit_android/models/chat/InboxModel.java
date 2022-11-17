package com.antizon.uit_android.models.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InboxModel {

	@SerializedName("chat_type")
	private int chatType;

	@SerializedName("last_message_date")
	private String lastMessageDate;

	@SerializedName("last_message")
	private String lastMessage;

	@SerializedName("id")
	private String id;

	@SerializedName("users")
	private List<InboxUser> users;

	public void setChatType(int chatType){
		this.chatType = chatType;
	}

	public int getChatType(){
		return chatType;
	}

	public void setLastMessageDate(String lastMessageDate){
		this.lastMessageDate = lastMessageDate;
	}

	public String getLastMessageDate(){
		return lastMessageDate;
	}

	public void setLastMessage(String lastMessage){
		this.lastMessage = lastMessage;
	}

	public String getLastMessage(){
		return lastMessage;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUsers(List<InboxUser> users){
		this.users = users;
	}

	public List<InboxUser> getUsers(){
		return users;
	}
}