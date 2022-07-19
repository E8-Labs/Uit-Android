package com.antizon.uit_android.models.company.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanyDemoGraphicInfoModel implements Parcelable {
    @SerializedName("lgbt")
    private int lgbt;

    public CompanyDemoGraphicInfoModel() {
    }

    public CompanyDemoGraphicInfoModel(int lgbt) {
        this.lgbt = lgbt;
    }

    public int getLgbt() {
        return lgbt;
    }

    public void setLgbt(int lgbt) {
        this.lgbt = lgbt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.lgbt);
    }

    public void readFromParcel(Parcel source) {
        this.lgbt = source.readInt();
    }

    protected CompanyDemoGraphicInfoModel(Parcel in) {
        this.lgbt = in.readInt();
    }

    public static final Parcelable.Creator<CompanyDemoGraphicInfoModel> CREATOR = new Parcelable.Creator<CompanyDemoGraphicInfoModel>() {
        @Override
        public CompanyDemoGraphicInfoModel createFromParcel(Parcel source) {
            return new CompanyDemoGraphicInfoModel(source);
        }

        @Override
        public CompanyDemoGraphicInfoModel[] newArray(int size) {
            return new CompanyDemoGraphicInfoModel[size];
        }
    };
}
