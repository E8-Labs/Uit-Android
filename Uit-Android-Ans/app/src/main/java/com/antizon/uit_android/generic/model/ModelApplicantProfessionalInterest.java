package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ModelApplicantProfessionalInterest implements Parcelable {

    int id;
    String name;

    public ModelApplicantProfessionalInterest() {
    }

    protected ModelApplicantProfessionalInterest(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<ModelApplicantProfessionalInterest> CREATOR = new Creator<ModelApplicantProfessionalInterest>() {
        @Override
        public ModelApplicantProfessionalInterest createFromParcel(Parcel in) {
            return new ModelApplicantProfessionalInterest(in);
        }

        @Override
        public ModelApplicantProfessionalInterest[] newArray(int size) {
            return new ModelApplicantProfessionalInterest[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
