package com.antizon.uit_android.models.comments;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.UitUserModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("reply_to")
    private int reply_to;
    @SerializedName("mention_to")
    private String mention_to;
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("user")
    private UitUserModel userModel;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("comments")
    private int comments;
    @SerializedName("likes")
    private int likes;
    @SerializedName("isLiked")
    private boolean isLiked;
    List<CommentDataModel> repliesList;

    boolean isShown;
    boolean alreadyLoaded;

    public CommentDataModel() {
    }

    public CommentDataModel(int id, String comment, int reply_to, String mention_to, int post_id, int user_id, UitUserModel userModel, String created_at, int comments, int likes, boolean isLiked, boolean isShown, List<CommentDataModel> repliesList, boolean alreadyLoaded) {
        this.id = id;
        this.comment = comment;
        this.reply_to = reply_to;
        this.mention_to = mention_to;
        this.post_id = post_id;
        this.user_id = user_id;
        this.userModel = userModel;
        this.created_at = created_at;
        this.comments = comments;
        this.likes = likes;
        this.isLiked = isLiked;
        this.isShown = isShown;
        this.repliesList = repliesList;
        this.alreadyLoaded = alreadyLoaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getReply_to() {
        return reply_to;
    }

    public void setReply_to(int reply_to) {
        this.reply_to = reply_to;
    }

    public String getMention_to() {
        return mention_to;
    }

    public void setMention_to(String mention_to) {
        this.mention_to = mention_to;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public UitUserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UitUserModel userModel) {
        this.userModel = userModel;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public List<CommentDataModel> getRepliesList() {
        return repliesList;
    }

    public void setRepliesList(List<CommentDataModel> repliesList) {
        this.repliesList = repliesList;
    }

    public boolean isAlreadyLoaded() {
        return alreadyLoaded;
    }

    public void setAlreadyLoaded(boolean alreadyLoaded) {
        this.alreadyLoaded = alreadyLoaded;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.comment);
        dest.writeInt(this.reply_to);
        dest.writeString(this.mention_to);
        dest.writeInt(this.post_id);
        dest.writeInt(this.user_id);
        dest.writeParcelable(this.userModel, flags);
        dest.writeString(this.created_at);
        dest.writeInt(this.comments);
        dest.writeInt(this.likes);
        dest.writeByte(this.isLiked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShown ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.repliesList);
        dest.writeByte(this.alreadyLoaded ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.comment = source.readString();
        this.reply_to = source.readInt();
        this.mention_to = source.readString();
        this.post_id = source.readInt();
        this.user_id = source.readInt();
        this.userModel = source.readParcelable(UitUserModel.class.getClassLoader());
        this.created_at = source.readString();
        this.comments = source.readInt();
        this.likes = source.readInt();
        this.isLiked = source.readByte() != 0;
        this.isShown = source.readByte() != 0;
        this.repliesList = source.createTypedArrayList(CommentDataModel.CREATOR);
        this.alreadyLoaded = source.readByte() != 0;
    }

    protected CommentDataModel(Parcel in) {
        this.id = in.readInt();
        this.comment = in.readString();
        this.reply_to = in.readInt();
        this.mention_to = in.readString();
        this.post_id = in.readInt();
        this.user_id = in.readInt();
        this.userModel = in.readParcelable(UitUserModel.class.getClassLoader());
        this.created_at = in.readString();
        this.comments = in.readInt();
        this.likes = in.readInt();
        this.isLiked = in.readByte() != 0;
        this.isShown = in.readByte() != 0;
        this.repliesList = in.createTypedArrayList(CommentDataModel.CREATOR);
        this.alreadyLoaded = in.readByte() != 0;
    }

    public static final Creator<CommentDataModel> CREATOR = new Creator<CommentDataModel>() {
        @Override
        public CommentDataModel createFromParcel(Parcel source) {
            return new CommentDataModel(source);
        }

        @Override
        public CommentDataModel[] newArray(int size) {
            return new CommentDataModel[size];
        }
    };
}
