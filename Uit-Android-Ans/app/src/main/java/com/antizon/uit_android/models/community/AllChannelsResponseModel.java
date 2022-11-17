package com.antizon.uit_android.models.community;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllChannelsResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<ChannelDataModel> channelsList;

    public AllChannelsResponseModel() {
    }

    public AllChannelsResponseModel(boolean status, String message, List<ChannelDataModel> channelsList) {
        this.status = status;
        this.message = message;
        this.channelsList = channelsList;
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

    public List<ChannelDataModel> getChannelsList() {
        return channelsList;
    }

    public void setChannelsList(List<ChannelDataModel> channelsList) {
        this.channelsList = channelsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.channelsList);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.channelsList = source.createTypedArrayList(ChannelDataModel.CREATOR);
    }

    protected AllChannelsResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.channelsList = in.createTypedArrayList(ChannelDataModel.CREATOR);
    }

    public static final Parcelable.Creator<AllChannelsResponseModel> CREATOR = new Parcelable.Creator<AllChannelsResponseModel>() {
        @Override
        public AllChannelsResponseModel createFromParcel(Parcel source) {
            return new AllChannelsResponseModel(source);
        }

        @Override
        public AllChannelsResponseModel[] newArray(int size) {
            return new AllChannelsResponseModel[size];
        }
    };
}
