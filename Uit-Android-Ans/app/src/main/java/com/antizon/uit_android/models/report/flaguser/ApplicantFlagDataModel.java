package com.antizon.uit_android.models.report.flaguser;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.google.gson.annotations.SerializedName;

public class ApplicantFlagDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("flagged")
    private GenericApplicantDataModel flaggedPerson;
    @SerializedName("flagged_by")
    private GenericApplicantDataModel flaggedBy;
    @SerializedName("comment")
    private String comment;
    @SerializedName("created_at")
    private String created_at;

    public ApplicantFlagDataModel() {
    }

    public ApplicantFlagDataModel(int id, GenericApplicantDataModel flaggedPerson, GenericApplicantDataModel flaggedBy, String comment, String created_at) {
        this.id = id;
        this.flaggedPerson = flaggedPerson;
        this.flaggedBy = flaggedBy;
        this.comment = comment;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GenericApplicantDataModel getFlaggedPerson() {
        return flaggedPerson;
    }

    public void setFlaggedPerson(GenericApplicantDataModel flaggedPerson) {
        this.flaggedPerson = flaggedPerson;
    }

    public GenericApplicantDataModel getFlaggedBy() {
        return flaggedBy;
    }

    public void setFlaggedBy(GenericApplicantDataModel flaggedBy) {
        this.flaggedBy = flaggedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.flaggedPerson, flags);
        dest.writeParcelable(this.flaggedBy, flags);
        dest.writeString(this.comment);
        dest.writeString(this.created_at);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.flaggedPerson = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.flaggedBy = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.comment = source.readString();
        this.created_at = source.readString();
    }

    protected ApplicantFlagDataModel(Parcel in) {
        this.id = in.readInt();
        this.flaggedPerson = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.flaggedBy = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.comment = in.readString();
        this.created_at = in.readString();
    }

    public static final Parcelable.Creator<ApplicantFlagDataModel> CREATOR = new Parcelable.Creator<ApplicantFlagDataModel>() {
        @Override
        public ApplicantFlagDataModel createFromParcel(Parcel source) {
            return new ApplicantFlagDataModel(source);
        }

        @Override
        public ApplicantFlagDataModel[] newArray(int size) {
            return new ApplicantFlagDataModel[size];
        }
    };
}
