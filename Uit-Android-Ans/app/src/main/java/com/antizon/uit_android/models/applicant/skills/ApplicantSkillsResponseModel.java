package com.antizon.uit_android.models.applicant.skills;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ApplicantSkillsResponseModel implements Parcelable {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<SkillDataModel> skillsList;

    public ApplicantSkillsResponseModel() {
    }

    public ApplicantSkillsResponseModel(boolean status, String message, ArrayList<SkillDataModel> skillsList) {
        this.status = status;
        this.message = message;
        this.skillsList = skillsList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SkillDataModel> getSkillsList() {
        return skillsList;
    }

    public void setSkillsList(ArrayList<SkillDataModel> skillsList) {
        this.skillsList = skillsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.skillsList);
    }

    public void readFromParcel(Parcel source) {
        this.status = source.readByte() != 0;
        this.message = source.readString();
        this.skillsList = source.createTypedArrayList(SkillDataModel.CREATOR);
    }

    protected ApplicantSkillsResponseModel(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.skillsList = in.createTypedArrayList(SkillDataModel.CREATOR);
    }

    public static final Parcelable.Creator<ApplicantSkillsResponseModel> CREATOR = new Parcelable.Creator<ApplicantSkillsResponseModel>() {
        @Override
        public ApplicantSkillsResponseModel createFromParcel(Parcel source) {
            return new ApplicantSkillsResponseModel(source);
        }

        @Override
        public ApplicantSkillsResponseModel[] newArray(int size) {
            return new ApplicantSkillsResponseModel[size];
        }
    };
}
