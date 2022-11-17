package com.antizon.uit_android.models.chat;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.google.gson.annotations.SerializedName;

public class ChatUserModel {

    @SerializedName("parent")
    private GenericApplicantDataModel parentDataModel;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("role")
    private int role;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("name")
    private String name;

    @SerializedName("unread_messages")
    private String unreadMessages;

    @SerializedName("id")
    private int id;

    public GenericApplicantDataModel getParentDataModel() {
        return parentDataModel;
    }

    public void setParentDataModel(GenericApplicantDataModel parentDataModel) {
        this.parentDataModel = parentDataModel;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(String unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}