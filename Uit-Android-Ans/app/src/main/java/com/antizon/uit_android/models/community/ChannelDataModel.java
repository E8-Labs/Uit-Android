package com.antizon.uit_android.models.community;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChannelDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("image_path")
    private String image_path;
    boolean isSelected;

    public ChannelDataModel() {
    }

    public ChannelDataModel(int id, String name, String image_path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.image_path = image_path;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image_path);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.image_path = source.readString();
        this.isSelected = source.readByte() != 0;
    }

    protected ChannelDataModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image_path = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ChannelDataModel> CREATOR = new Creator<ChannelDataModel>() {
        @Override
        public ChannelDataModel createFromParcel(Parcel source) {
            return new ChannelDataModel(source);
        }

        @Override
        public ChannelDataModel[] newArray(int size) {
            return new ChannelDataModel[size];
        }
    };
}
