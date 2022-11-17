package com.antizon.uit_android.models.community;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityFeedsResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<CommunityPostDataModel> feedsList;

    public CommunityFeedsResponseModel() {
    }

    public CommunityFeedsResponseModel(boolean status, String message, List<CommunityPostDataModel> feedsList) {
        this.status = status;
        this.message = message;
        this.feedsList = feedsList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommunityPostDataModel> getFeedsList() {
        return feedsList;
    }

    public void setFeedsList(List<CommunityPostDataModel> feedsList) {
        this.feedsList = feedsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.feedsList);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.feedsList = source.createTypedArrayList(CommunityPostDataModel.CREATOR);
    }

    protected CommunityFeedsResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.feedsList = in.createTypedArrayList(CommunityPostDataModel.CREATOR);
    }

    public static final Parcelable.Creator<CommunityFeedsResponseModel> CREATOR = new Parcelable.Creator<CommunityFeedsResponseModel>() {
        @Override
        public CommunityFeedsResponseModel createFromParcel(Parcel source) {
            return new CommunityFeedsResponseModel(source);
        }

        @Override
        public CommunityFeedsResponseModel[] newArray(int size) {
            return new CommunityFeedsResponseModel[size];
        }
    };
}
