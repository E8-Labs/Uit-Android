package com.antizon.uit_android.models.community;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.UitUserModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommunityPostDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("post_title")
    private String post_title;
    @SerializedName("post_description")
    private String post_description;
    @SerializedName("post_url")
    private String post_url;
    @SerializedName("video_url")
    private String video_url;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("user")
    private UitUserModel userModel;
    @SerializedName("image_path")
    private String image_path;
    @SerializedName("channels")
    private List<ChannelDataModel> channelsList;
    @SerializedName("likes")
    private int likes;
    @SerializedName("comments")
    private int comments;
    @SerializedName("is_liked")
    private boolean is_liked;
    @SerializedName("engagement_text")
    private String engagement_text;
    @SerializedName("engaged_by")
    private List<UitUserModel> engagedPeople;

    public CommunityPostDataModel() {
    }

    public CommunityPostDataModel(int id, String post_title, String post_description, String post_url, String video_url, String thumbnail, int user_id, UitUserModel userModel, String image_path, List<ChannelDataModel> channelsList, int likes, int comments, boolean is_liked, String engagement_text, List<UitUserModel> engagedPeople) {
        this.id = id;
        this.post_title = post_title;
        this.post_description = post_description;
        this.post_url = post_url;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
        this.user_id = user_id;
        this.userModel = userModel;
        this.image_path = image_path;
        this.channelsList = channelsList;
        this.likes = likes;
        this.comments = comments;
        this.is_liked = is_liked;
        this.engagement_text = engagement_text;
        this.engagedPeople = engagedPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public List<ChannelDataModel> getChannelsList() {
        return channelsList;
    }

    public void setChannelsList(List<ChannelDataModel> channelsList) {
        this.channelsList = channelsList;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public String getEngagement_text() {
        return engagement_text;
    }

    public void setEngagement_text(String engagement_text) {
        this.engagement_text = engagement_text;
    }

    public List<UitUserModel> getEngagedPeople() {
        return engagedPeople;
    }

    public void setEngagedPeople(List<UitUserModel> engagedPeople) {
        this.engagedPeople = engagedPeople;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.post_title);
        dest.writeString(this.post_description);
        dest.writeString(this.post_url);
        dest.writeString(this.video_url);
        dest.writeString(this.thumbnail);
        dest.writeInt(this.user_id);
        dest.writeParcelable(this.userModel, flags);
        dest.writeString(this.image_path);
        dest.writeTypedList(this.channelsList);
        dest.writeInt(this.likes);
        dest.writeInt(this.comments);
        dest.writeByte(this.is_liked ? (byte) 1 : (byte) 0);
        dest.writeString(this.engagement_text);
        dest.writeTypedList(this.engagedPeople);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.post_title = source.readString();
        this.post_description = source.readString();
        this.post_url = source.readString();
        this.video_url = source.readString();
        this.thumbnail = source.readString();
        this.user_id = source.readInt();
        this.userModel = source.readParcelable(UitUserModel.class.getClassLoader());
        this.image_path = source.readString();
        this.channelsList = source.createTypedArrayList(ChannelDataModel.CREATOR);
        this.likes = source.readInt();
        this.comments = source.readInt();
        this.is_liked = source.readByte() != 0;
        this.engagement_text = source.readString();
        this.engagedPeople = source.createTypedArrayList(UitUserModel.CREATOR);
    }

    protected CommunityPostDataModel(Parcel in) {
        this.id = in.readInt();
        this.post_title = in.readString();
        this.post_description = in.readString();
        this.post_url = in.readString();
        this.video_url = in.readString();
        this.thumbnail = in.readString();
        this.user_id = in.readInt();
        this.userModel = in.readParcelable(UitUserModel.class.getClassLoader());
        this.image_path = in.readString();
        this.channelsList = in.createTypedArrayList(ChannelDataModel.CREATOR);
        this.likes = in.readInt();
        this.comments = in.readInt();
        this.is_liked = in.readByte() != 0;
        this.engagement_text = in.readString();
        this.engagedPeople = in.createTypedArrayList(UitUserModel.CREATOR);
    }

    public static final Creator<CommunityPostDataModel> CREATOR = new Creator<CommunityPostDataModel>() {
        @Override
        public CommunityPostDataModel createFromParcel(Parcel source) {
            return new CommunityPostDataModel(source);
        }

        @Override
        public CommunityPostDataModel[] newArray(int size) {
            return new CommunityPostDataModel[size];
        }
    };
}
