package com.antizon.uit_android.notifications.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationChat implements Parcelable {
    @SerializedName("chat_type")
    private int chatType;

    @SerializedName("last_message_date")
    private String lastMessageDate;

    @SerializedName("last_message")
    private String lastMessage;

    @SerializedName("id")
    private String id;

    @SerializedName("users")
    private List<GenericApplicantDataModel> users;

    public NotificationChat() {
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GenericApplicantDataModel> getUsers() {
        return users;
    }

    public void setUsers(List<GenericApplicantDataModel> users) {
        this.users = users;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.chatType);
        dest.writeString(this.lastMessageDate);
        dest.writeString(this.lastMessage);
        dest.writeString(this.id);
        dest.writeTypedList(this.users);
    }

    public void readFromParcel(Parcel source) {
        this.chatType = source.readInt();
        this.lastMessageDate = source.readString();
        this.lastMessage = source.readString();
        this.id = source.readString();
        this.users = source.createTypedArrayList(GenericApplicantDataModel.CREATOR);
    }

    protected NotificationChat(Parcel in) {
        this.chatType = in.readInt();
        this.lastMessageDate = in.readString();
        this.lastMessage = in.readString();
        this.id = in.readString();
        this.users = in.createTypedArrayList(GenericApplicantDataModel.CREATOR);
    }

    public static final Parcelable.Creator<NotificationChat> CREATOR = new Parcelable.Creator<NotificationChat>() {
        @Override
        public NotificationChat createFromParcel(Parcel source) {
            return new NotificationChat(source);
        }

        @Override
        public NotificationChat[] newArray(int size) {
            return new NotificationChat[size];
        }
    };
}
