package com.antizon.uit_android.models.applicant.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IndustryModel implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.id = source.readInt();
    }

    public IndustryModel() {
    }

    protected IndustryModel(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<IndustryModel> CREATOR = new Parcelable.Creator<IndustryModel>() {
        @Override
        public IndustryModel createFromParcel(Parcel source) {
            return new IndustryModel(source);
        }

        @Override
        public IndustryModel[] newArray(int size) {
            return new IndustryModel[size];
        }
    };
}