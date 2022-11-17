package com.antizon.uit_android.models.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AdminHomeDataModel implements Parcelable {
    @SerializedName("companies")
    private int companies;
    @SerializedName("applicants")
    private int applicants;
    @SerializedName("uit_members")
    private int uit_members;
    @SerializedName("recruiters")
    private int recruiters;
    @SerializedName("jobs")
    private int jobs;
    @SerializedName("community")
    private int community;

    public AdminHomeDataModel() {
    }

    public AdminHomeDataModel(int companies, int applicants, int uit_members, int recruiters, int jobs, int community) {
        this.companies = companies;
        this.applicants = applicants;
        this.uit_members = uit_members;
        this.recruiters = recruiters;
        this.jobs = jobs;
        this.community = community;
    }

    public int getCompanies() {
        return companies;
    }

    public void setCompanies(int companies) {
        this.companies = companies;
    }

    public int getApplicants() {
        return applicants;
    }

    public void setApplicants(int applicants) {
        this.applicants = applicants;
    }

    public int getUit_members() {
        return uit_members;
    }

    public void setUit_members(int uit_members) {
        this.uit_members = uit_members;
    }

    public int getRecruiters() {
        return recruiters;
    }

    public void setRecruiters(int recruiters) {
        this.recruiters = recruiters;
    }

    public int getJobs() {
        return jobs;
    }

    public void setJobs(int jobs) {
        this.jobs = jobs;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.companies);
        dest.writeInt(this.applicants);
        dest.writeInt(this.uit_members);
        dest.writeInt(this.recruiters);
        dest.writeInt(this.jobs);
        dest.writeInt(this.community);
    }

    public void readFromParcel(Parcel source) {
        this.companies = source.readInt();
        this.applicants = source.readInt();
        this.uit_members = source.readInt();
        this.recruiters = source.readInt();
        this.jobs = source.readInt();
        this.community = source.readInt();
    }

    protected AdminHomeDataModel(Parcel in) {
        this.companies = in.readInt();
        this.applicants = in.readInt();
        this.uit_members = in.readInt();
        this.recruiters = in.readInt();
        this.jobs = in.readInt();
        this.community = in.readInt();
    }

    public static final Parcelable.Creator<AdminHomeDataModel> CREATOR = new Parcelable.Creator<AdminHomeDataModel>() {
        @Override
        public AdminHomeDataModel createFromParcel(Parcel source) {
            return new AdminHomeDataModel(source);
        }

        @Override
        public AdminHomeDataModel[] newArray(int size) {
            return new AdminHomeDataModel[size];
        }
    };
}
