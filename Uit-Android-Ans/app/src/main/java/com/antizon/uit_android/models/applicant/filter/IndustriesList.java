package com.antizon.uit_android.models.applicant.filter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class IndustriesList implements Parcelable {
    private List<IndustryModel> data;

    public IndustriesList(List<IndustryModel> data) {
        this.data = data;
    }

    public List<IndustryModel> getData() {
        return data;
    }

    public void setData(List<IndustryModel> data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
    }

    public void readFromParcel(Parcel source) {
        this.data = source.createTypedArrayList(IndustryModel.CREATOR);
    }

    protected IndustriesList(Parcel in) {
        this.data = in.createTypedArrayList(IndustryModel.CREATOR);
    }

    public static final Parcelable.Creator<IndustriesList> CREATOR = new Parcelable.Creator<IndustriesList>() {
        @Override
        public IndustriesList createFromParcel(Parcel source) {
            return new IndustriesList(source);
        }

        @Override
        public IndustriesList[] newArray(int size) {
            return new IndustriesList[size];
        }
    };
}
