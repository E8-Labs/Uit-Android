package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ModelApplicantRace implements Parcelable {

    int id;
    String name="";
    boolean isSelected = false;

    public ModelApplicantRace() {
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
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.isSelected = source.readByte() != 0;
    }

    protected ModelApplicantRace(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ModelApplicantRace> CREATOR = new Creator<ModelApplicantRace>() {
        @Override
        public ModelApplicantRace createFromParcel(Parcel source) {
            return new ModelApplicantRace(source);
        }

        @Override
        public ModelApplicantRace[] newArray(int size) {
            return new ModelApplicantRace[size];
        }
    };
}
