package com.antizon.uit_android.models.applicant.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicantHomeResponseDataModel implements Parcelable {
    @SerializedName("featured")
    private List<ApplicantHomeJobDataModel> featuredJobList;
    @SerializedName("recommended")
    private List<ApplicantHomeJobDataModel> recommendedJobsList;
    @SerializedName("latest")
    private List<ApplicantHomeJobDataModel> latestJobsList;
    public ApplicantHomeResponseDataModel() {
    }

    public ApplicantHomeResponseDataModel(List<ApplicantHomeJobDataModel> featuredJobList, List<ApplicantHomeJobDataModel> latestJobsList, List<ApplicantHomeJobDataModel> recommendedJobsList) {
        this.featuredJobList = featuredJobList;
        this.latestJobsList = latestJobsList;
        this.recommendedJobsList = recommendedJobsList;
    }

    public List<ApplicantHomeJobDataModel> getFeaturedJobList() {
        return featuredJobList;
    }

    public void setFeaturedJobList(List<ApplicantHomeJobDataModel> featuredJobList) {
        this.featuredJobList = featuredJobList;
    }

    public List<ApplicantHomeJobDataModel> getLatestJobsList() {
        return latestJobsList;
    }

    public void setLatestJobsList(List<ApplicantHomeJobDataModel> latestJobsList) {
        this.latestJobsList = latestJobsList;
    }

    public List<ApplicantHomeJobDataModel> getRecommendedJobsList() {
        return recommendedJobsList;
    }

    public void setRecommendedJobsList(List<ApplicantHomeJobDataModel> recommendedJobsList) {
        this.recommendedJobsList = recommendedJobsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.featuredJobList);
        dest.writeTypedList(this.latestJobsList);
        dest.writeTypedList(this.recommendedJobsList);
    }

    public void readFromParcel(Parcel source) {
        this.featuredJobList = source.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
        this.latestJobsList = source.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
        this.recommendedJobsList = source.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
    }

    protected ApplicantHomeResponseDataModel(Parcel in) {
        this.featuredJobList = in.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
        this.latestJobsList = in.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
        this.recommendedJobsList = in.createTypedArrayList(ApplicantHomeJobDataModel.CREATOR);
    }

    public static final Parcelable.Creator<ApplicantHomeResponseDataModel> CREATOR = new Parcelable.Creator<ApplicantHomeResponseDataModel>() {
        @Override
        public ApplicantHomeResponseDataModel createFromParcel(Parcel source) {
            return new ApplicantHomeResponseDataModel(source);
        }

        @Override
        public ApplicantHomeResponseDataModel[] newArray(int size) {
            return new ApplicantHomeResponseDataModel[size];
        }
    };
}
