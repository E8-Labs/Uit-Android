package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelExperience implements Parcelable {

    int id;
    String experience, years, priority, skill;

    public ModelExperience() {
    }

    protected ModelExperience(Parcel in) {
        id = in.readInt();
        experience = in.readString();
        years = in.readString();
        priority = in.readString();
        skill = in.readString();
    }

    public static final Creator<ModelExperience> CREATOR = new Creator<ModelExperience>() {
        @Override
        public ModelExperience createFromParcel(Parcel in) {
            return new ModelExperience(in);
        }

        @Override
        public ModelExperience[] newArray(int size) {
            return new ModelExperience[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(experience);
        parcel.writeString(years);
        parcel.writeString(priority);
        parcel.writeString(skill);
    }
}
