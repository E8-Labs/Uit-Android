package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReferenceModel implements Parcelable {

    String nameOfReference,selectJob,phone;

    public ReferenceModel() {
    }

    protected ReferenceModel(Parcel in) {
        nameOfReference = in.readString();
        selectJob = in.readString();
        phone = in.readString();
    }

    public static final Creator<ReferenceModel> CREATOR = new Creator<ReferenceModel>() {
        @Override
        public ReferenceModel createFromParcel(Parcel in) {
            return new ReferenceModel(in);
        }

        @Override
        public ReferenceModel[] newArray(int size) {
            return new ReferenceModel[size];
        }
    };

    public String getNameOfReference() {
        return nameOfReference;
    }

    public void setNameOfReference(String nameOfReference) {
        this.nameOfReference = nameOfReference;
    }

    public String getSelectJob() {
        return selectJob;
    }

    public void setSelectJob(String selectJob) {
        this.selectJob = selectJob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static Creator<ReferenceModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nameOfReference);
        parcel.writeString(selectJob);
        parcel.writeString(phone);
    }
}
