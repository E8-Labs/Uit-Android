package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelApplicantJobs implements Parcelable {

    int id;
    String name;
    boolean isSelected = false;

    public ModelApplicantJobs() {
    }

    public ModelApplicantJobs(int id, String name, boolean isSelected) {
        this.id = id;
        this.name = name;
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

    protected ModelApplicantJobs(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ModelApplicantJobs> CREATOR = new Creator<ModelApplicantJobs>() {
        @Override
        public ModelApplicantJobs createFromParcel(Parcel source) {
            return new ModelApplicantJobs(source);
        }

        @Override
        public ModelApplicantJobs[] newArray(int size) {
            return new ModelApplicantJobs[size];
        }
    };
}
