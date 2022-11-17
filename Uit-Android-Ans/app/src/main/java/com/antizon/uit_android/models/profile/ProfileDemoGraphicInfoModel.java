package com.antizon.uit_android.models.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileDemoGraphicInfoModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("races")
    private List<NameIdModel> racesList;
    @SerializedName("gender")
    private int gender;
    @SerializedName("lgbt")
    private int lgbt;
    @SerializedName("veteran")
    private Integer veteran;
    @SerializedName("user_id")
    private int user_id;

    public ProfileDemoGraphicInfoModel(int id, List<NameIdModel> racesList, int gender, int lgbt, Integer veteran, int user_id) {
        this.id = id;
        this.racesList = racesList;
        this.gender = gender;
        this.lgbt = lgbt;
        this.veteran = veteran;
        this.user_id = user_id;
    }

    public ProfileDemoGraphicInfoModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<NameIdModel> getRacesList() {
        return racesList;
    }

    public void setRacesList(List<NameIdModel> racesList) {
        this.racesList = racesList;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getLgbt() {
        return lgbt;
    }

    public void setLgbt(int lgbt) {
        this.lgbt = lgbt;
    }

    public Integer getVeteran() {
        return veteran;
    }

    public void setVeteran(Integer veteran) {
        this.veteran = veteran;
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
        dest.writeTypedList(this.racesList);
        dest.writeInt(this.gender);
        dest.writeInt(this.lgbt);
        dest.writeValue(this.veteran);
        dest.writeInt(this.user_id);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.racesList = source.createTypedArrayList(NameIdModel.CREATOR);
        this.gender = source.readInt();
        this.lgbt = source.readInt();
        this.veteran = (Integer) source.readValue(Integer.class.getClassLoader());
        this.user_id = source.readInt();
    }

    protected ProfileDemoGraphicInfoModel(Parcel in) {
        this.id = in.readInt();
        this.racesList = in.createTypedArrayList(NameIdModel.CREATOR);
        this.gender = in.readInt();
        this.lgbt = in.readInt();
        this.veteran = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = in.readInt();
    }

    public static final Creator<ProfileDemoGraphicInfoModel> CREATOR = new Creator<ProfileDemoGraphicInfoModel>() {
        @Override
        public ProfileDemoGraphicInfoModel createFromParcel(Parcel source) {
            return new ProfileDemoGraphicInfoModel(source);
        }

        @Override
        public ProfileDemoGraphicInfoModel[] newArray(int size) {
            return new ProfileDemoGraphicInfoModel[size];
        }
    };
}
