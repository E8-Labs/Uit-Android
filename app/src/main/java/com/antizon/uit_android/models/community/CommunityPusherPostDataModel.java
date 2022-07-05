package com.antizon.uit_android.models.community;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CommunityPusherPostDataModel implements Parcelable {
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("post")
    private CommunityPostDataModel communityPostDataModel;

    public CommunityPusherPostDataModel() {
    }

    public CommunityPusherPostDataModel(int post_id, CommunityPostDataModel communityPostDataModel) {
        this.post_id = post_id;
        this.communityPostDataModel = communityPostDataModel;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public CommunityPostDataModel getCommunityPostDataModel() {
        return communityPostDataModel;
    }

    public void setCommunityPostDataModel(CommunityPostDataModel communityPostDataModel) {
        this.communityPostDataModel = communityPostDataModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.post_id);
        dest.writeParcelable(this.communityPostDataModel, flags);
    }

    public void readFromParcel(Parcel source) {
        this.post_id = source.readInt();
        this.communityPostDataModel = source.readParcelable(CommunityPostDataModel.class.getClassLoader());
    }

    protected CommunityPusherPostDataModel(Parcel in) {
        this.post_id = in.readInt();
        this.communityPostDataModel = in.readParcelable(CommunityPostDataModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<CommunityPusherPostDataModel> CREATOR = new Parcelable.Creator<CommunityPusherPostDataModel>() {
        @Override
        public CommunityPusherPostDataModel createFromParcel(Parcel source) {
            return new CommunityPusherPostDataModel(source);
        }

        @Override
        public CommunityPusherPostDataModel[] newArray(int size) {
            return new CommunityPusherPostDataModel[size];
        }
    };
}
