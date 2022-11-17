package com.antizon.uit_android.notifications.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.comments.CommentDataModel;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.models.jobs.JobCompanyDataModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.google.gson.annotations.SerializedName;

public class NotificationModel implements Parcelable {

    @SerializedName("notification_type")
    private int notificationType;

    @SerializedName("flagged_user")
    private String flaggedUser;

    @SerializedName("post")
    private CommunityPostDataModel post;

    @SerializedName("application")
    private String application;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("comment")
    private CommentDataModel comment;

    @SerializedName("id")
    private int id;

    @SerializedName("notifiable_id")
    private int notifiableId;

    @SerializedName("is_seen")
    private boolean isSeen;

    @SerializedName("chat_message")
    private NotificationChatMessage chatMessage;

    @SerializedName("job")
    private SingleJobDataModel job;

    @SerializedName("from_user")
    private GenericApplicantDataModel fromUser;

    public NotificationModel() {
    }

    public NotificationModel(int notificationType, String flaggedUser, CommunityPostDataModel post, String application, String createdAt, CommentDataModel comment, int id, int notifiableId, boolean isSeen, NotificationChatMessage chatMessage, SingleJobDataModel job, GenericApplicantDataModel fromUser) {
        this.notificationType = notificationType;
        this.flaggedUser = flaggedUser;
        this.post = post;
        this.application = application;
        this.createdAt = createdAt;
        this.comment = comment;
        this.id = id;
        this.notifiableId = notifiableId;
        this.isSeen = isSeen;
        this.chatMessage = chatMessage;
        this.job = job;
        this.fromUser = fromUser;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getFlaggedUser() {
        return flaggedUser;
    }

    public void setFlaggedUser(String flaggedUser) {
        this.flaggedUser = flaggedUser;
    }

    public CommunityPostDataModel getPost() {
        return post;
    }

    public void setPost(CommunityPostDataModel post) {
        this.post = post;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CommentDataModel getComment() {
        return comment;
    }

    public void setComment(CommentDataModel comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotifiableId() {
        return notifiableId;
    }

    public void setNotifiableId(int notifiableId) {
        this.notifiableId = notifiableId;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public NotificationChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(NotificationChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public SingleJobDataModel getJob() {
        return job;
    }

    public void setJob(SingleJobDataModel job) {
        this.job = job;
    }

    public GenericApplicantDataModel getFromUser() {
        return fromUser;
    }

    public void setFromUser(GenericApplicantDataModel fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.notificationType);
        dest.writeString(this.flaggedUser);
        dest.writeParcelable(this.post, flags);
        dest.writeString(this.application);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.comment, flags);
        dest.writeInt(this.id);
        dest.writeInt(this.notifiableId);
        dest.writeByte(this.isSeen ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.chatMessage, flags);
        dest.writeParcelable(this.job, flags);
        dest.writeParcelable(this.fromUser, flags);
    }

    public void readFromParcel(Parcel source) {
        this.notificationType = source.readInt();
        this.flaggedUser = source.readString();
        this.post = source.readParcelable(CommunityPostDataModel.class.getClassLoader());
        this.application = source.readString();
        this.createdAt = source.readString();
        this.comment = source.readParcelable(CommentDataModel.class.getClassLoader());
        this.id = source.readInt();
        this.notifiableId = source.readInt();
        this.isSeen = source.readByte() != 0;
        this.chatMessage = source.readParcelable(NotificationChatMessage.class.getClassLoader());
        this.job = source.readParcelable(SingleJobDataModel.class.getClassLoader());
        this.fromUser = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
    }

    protected NotificationModel(Parcel in) {
        this.notificationType = in.readInt();
        this.flaggedUser = in.readString();
        this.post = in.readParcelable(CommunityPostDataModel.class.getClassLoader());
        this.application = in.readString();
        this.createdAt = in.readString();
        this.comment = in.readParcelable(CommentDataModel.class.getClassLoader());
        this.id = in.readInt();
        this.notifiableId = in.readInt();
        this.isSeen = in.readByte() != 0;
        this.chatMessage = in.readParcelable(NotificationChatMessage.class.getClassLoader());
        this.job = in.readParcelable(SingleJobDataModel.class.getClassLoader());
        this.fromUser = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel source) {
            return new NotificationModel(source);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };
}
