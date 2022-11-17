package com.antizon.uit_android.notifications.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.google.gson.annotations.SerializedName;

public class NotificationChatMessage implements Parcelable {
    @SerializedName("image_height")
    private String imageHeight;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("chat")
    private NotificationChat chat;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("image_width")
    private String imageWidth;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private GenericApplicantDataModel user;

    @SerializedName("chat_id")
    private String chatId;

    public NotificationChatMessage(String imageHeight, String imageUrl, NotificationChat chat, String createdAt, int id, String imageWidth, String message, GenericApplicantDataModel user, String chatId) {
        this.imageHeight = imageHeight;
        this.imageUrl = imageUrl;
        this.chat = chat;
        this.createdAt = createdAt;
        this.id = id;
        this.imageWidth = imageWidth;
        this.message = message;
        this.user = user;
        this.chatId = chatId;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public NotificationChat getChat() {
        return chat;
    }

    public void setChat(NotificationChat chat) {
        this.chat = chat;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GenericApplicantDataModel getUser() {
        return user;
    }

    public void setUser(GenericApplicantDataModel user) {
        this.user = user;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageHeight);
        dest.writeString(this.imageUrl);
        dest.writeParcelable(this.chat, flags);
        dest.writeString(this.createdAt);
        dest.writeInt(this.id);
        dest.writeString(this.imageWidth);
        dest.writeString(this.message);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.chatId);
    }

    public void readFromParcel(Parcel source) {
        this.imageHeight = source.readString();
        this.imageUrl = source.readString();
        this.chat = source.readParcelable(NotificationChat.class.getClassLoader());
        this.createdAt = source.readString();
        this.id = source.readInt();
        this.imageWidth = source.readString();
        this.message = source.readString();
        this.user = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.chatId = source.readString();
    }

    public NotificationChatMessage() {
    }

    protected NotificationChatMessage(Parcel in) {
        this.imageHeight = in.readString();
        this.imageUrl = in.readString();
        this.chat = in.readParcelable(NotificationChat.class.getClassLoader());
        this.createdAt = in.readString();
        this.id = in.readInt();
        this.imageWidth = in.readString();
        this.message = in.readString();
        this.user = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.chatId = in.readString();
    }

    public static final Parcelable.Creator<NotificationChatMessage> CREATOR = new Parcelable.Creator<NotificationChatMessage>() {
        @Override
        public NotificationChatMessage createFromParcel(Parcel source) {
            return new NotificationChatMessage(source);
        }

        @Override
        public NotificationChatMessage[] newArray(int size) {
            return new NotificationChatMessage[size];
        }
    };
}
