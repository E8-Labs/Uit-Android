package com.antizon.uit_android.models.applicant;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;

import java.util.List;

public class ApplicantEducationModelList implements Parcelable {
    List<ApplicantEducationDataModel> educationList;

    public ApplicantEducationModelList(List<ApplicantEducationDataModel> educationList) {
        this.educationList = educationList;
    }

    public List<ApplicantEducationDataModel> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<ApplicantEducationDataModel> educationList) {
        this.educationList = educationList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.educationList);
    }

    public void readFromParcel(Parcel source) {
        this.educationList = source.createTypedArrayList(ApplicantEducationDataModel.CREATOR);
    }

    protected ApplicantEducationModelList(Parcel in) {
        this.educationList = in.createTypedArrayList(ApplicantEducationDataModel.CREATOR);
    }

    public static final Parcelable.Creator<ApplicantEducationModelList> CREATOR = new Parcelable.Creator<ApplicantEducationModelList>() {
        @Override
        public ApplicantEducationModelList createFromParcel(Parcel source) {
            return new ApplicantEducationModelList(source);
        }

        @Override
        public ApplicantEducationModelList[] newArray(int size) {
            return new ApplicantEducationModelList[size];
        }
    };
}
