package com.antizon.uit_android.models.applicant.department;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApplicantDepartmentDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    boolean isSelected;

    public ApplicantDepartmentDataModel() {
    }

    public ApplicantDepartmentDataModel(int id, String name, String created_at, String updated_at, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.created_at = source.readString();
        this.updated_at = source.readString();
        this.isSelected = source.readByte() != 0;
    }

    protected ApplicantDepartmentDataModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ApplicantDepartmentDataModel> CREATOR = new Creator<ApplicantDepartmentDataModel>() {
        @Override
        public ApplicantDepartmentDataModel createFromParcel(Parcel source) {
            return new ApplicantDepartmentDataModel(source);
        }

        @Override
        public ApplicantDepartmentDataModel[] newArray(int size) {
            return new ApplicantDepartmentDataModel[size];
        }
    };
}
