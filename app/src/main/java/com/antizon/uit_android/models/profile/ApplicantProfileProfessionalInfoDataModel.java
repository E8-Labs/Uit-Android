package com.antizon.uit_android.models.profile;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ApplicantProfileProfessionalInfoDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("employment_status")
    private int employment_status;
    @SerializedName("resume")
    private String resume;
    @SerializedName("cover_letter")
    private String cover_letter;
    @SerializedName("user_id")
    private int user_id;

    public ApplicantProfileProfessionalInfoDataModel() {
    }

    public ApplicantProfileProfessionalInfoDataModel(int id, int employment_status, String resume, String cover_letter, int user_id) {
        this.id = id;
        this.employment_status = employment_status;
        this.resume = resume;
        this.cover_letter = cover_letter;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployment_status() {
        return employment_status;
    }

    public void setEmployment_status(int employment_status) {
        this.employment_status = employment_status;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getCover_letter() {
        return cover_letter;
    }

    public void setCover_letter(String cover_letter) {
        this.cover_letter = cover_letter;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.employment_status);
        dest.writeString(this.resume);
        dest.writeString(this.cover_letter);
        dest.writeInt(this.user_id);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.employment_status = source.readInt();
        this.resume = source.readString();
        this.cover_letter = source.readString();
        this.user_id = source.readInt();
    }

    protected ApplicantProfileProfessionalInfoDataModel(Parcel in) {
        this.id = in.readInt();
        this.employment_status = in.readInt();
        this.resume = in.readString();
        this.cover_letter = in.readString();
        this.user_id = in.readInt();
    }

    public static final Parcelable.Creator<ApplicantProfileProfessionalInfoDataModel> CREATOR = new Parcelable.Creator<ApplicantProfileProfessionalInfoDataModel>() {
        @Override
        public ApplicantProfileProfessionalInfoDataModel createFromParcel(Parcel source) {
            return new ApplicantProfileProfessionalInfoDataModel(source);
        }

        @Override
        public ApplicantProfileProfessionalInfoDataModel[] newArray(int size) {
            return new ApplicantProfileProfessionalInfoDataModel[size];
        }
    };
}
