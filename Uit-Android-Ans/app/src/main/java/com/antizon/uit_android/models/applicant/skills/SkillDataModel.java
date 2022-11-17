package com.antizon.uit_android.models.applicant.skills;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SkillDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public SkillDataModel() {
    }

    public SkillDataModel(int id, String name) {
        this.id = id;
        this.name = name;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
    }

    protected SkillDataModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<SkillDataModel> CREATOR = new Creator<SkillDataModel>() {
        @Override
        public SkillDataModel createFromParcel(Parcel source) {
            return new SkillDataModel(source);
        }

        @Override
        public SkillDataModel[] newArray(int size) {
            return new SkillDataModel[size];
        }
    };
}
