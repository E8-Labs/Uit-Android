package com.antizon.uit_android.models.applicant.filter;

import android.os.Parcel;
import android.os.Parcelable;

public class MultiSelectionModel implements Parcelable {
    String title;
    int value;
    boolean isSelected;

    public MultiSelectionModel(String title, int value) {
        this.title = title;
        this.value = value;
    }

    public MultiSelectionModel(String title, int value, boolean isSelected) {
        this.title = title;
        this.value = value;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        dest.writeString(this.title);
        dest.writeInt(this.value);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.value = source.readInt();
        this.isSelected = source.readByte() != 0;
    }

    protected MultiSelectionModel(Parcel in) {
        this.title = in.readString();
        this.value = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MultiSelectionModel> CREATOR = new Parcelable.Creator<MultiSelectionModel>() {
        @Override
        public MultiSelectionModel createFromParcel(Parcel source) {
            return new MultiSelectionModel(source);
        }

        @Override
        public MultiSelectionModel[] newArray(int size) {
            return new MultiSelectionModel[size];
        }
    };
}
