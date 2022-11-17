package com.antizon.uit_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.google.gson.annotations.SerializedName;

public class AppliedJobApplicantDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("application_status")
    private int application_status;
    @SerializedName("user")
    private GenericApplicantDataModel genericApplicantDataModel;

    public AppliedJobApplicantDataModel() {
    }

    public AppliedJobApplicantDataModel(int id, int application_status, GenericApplicantDataModel genericApplicantDataModel) {
        this.id = id;
        this.application_status = application_status;
        this.genericApplicantDataModel = genericApplicantDataModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplication_status() {
        return application_status;
    }

    public void setApplication_status(int application_status) {
        this.application_status = application_status;
    }

    public GenericApplicantDataModel getGenericApplicantDataModel() {
        return genericApplicantDataModel;
    }

    public void setGenericApplicantDataModel(GenericApplicantDataModel genericApplicantDataModel) {
        this.genericApplicantDataModel = genericApplicantDataModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.application_status);
        dest.writeParcelable(this.genericApplicantDataModel, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.application_status = source.readInt();
        this.genericApplicantDataModel = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
    }

    protected AppliedJobApplicantDataModel(Parcel in) {
        this.id = in.readInt();
        this.application_status = in.readInt();
        this.genericApplicantDataModel = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppliedJobApplicantDataModel> CREATOR = new Parcelable.Creator<AppliedJobApplicantDataModel>() {
        @Override
        public AppliedJobApplicantDataModel createFromParcel(Parcel source) {
            return new AppliedJobApplicantDataModel(source);
        }

        @Override
        public AppliedJobApplicantDataModel[] newArray(int size) {
            return new AppliedJobApplicantDataModel[size];
        }
    };
}
