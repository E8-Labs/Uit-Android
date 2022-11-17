package com.antizon.uit_android.models.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.profile.NameIdModel;
import com.google.gson.annotations.SerializedName;

public class JobExperienceDataModel implements Parcelable {
    @SerializedName("job_id")
    private int jobId;
    @SerializedName("requirement_status")
    private int requirementStatus;
    @SerializedName("industry")
    private NameIdModel industry;
    @SerializedName("id")
    private int id;
    @SerializedName("years")
    private int years;

    public JobExperienceDataModel(int jobId, int requirementStatus, NameIdModel industry, int id, int years) {
        this.jobId = jobId;
        this.requirementStatus = requirementStatus;
        this.industry = industry;
        this.id = id;
        this.years = years;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getRequirementStatus() {
        return requirementStatus;
    }

    public void setRequirementStatus(int requirementStatus) {
        this.requirementStatus = requirementStatus;
    }

    public NameIdModel getIndustry() {
        return industry;
    }

    public void setIndustry(NameIdModel industry) {
        this.industry = industry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.jobId);
        dest.writeInt(this.requirementStatus);
        dest.writeParcelable(this.industry, flags);
        dest.writeInt(this.id);
        dest.writeInt(this.years);
    }

    public void readFromParcel(Parcel source) {
        this.jobId = source.readInt();
        this.requirementStatus = source.readInt();
        this.industry = source.readParcelable(NameIdModel.class.getClassLoader());
        this.id = source.readInt();
        this.years = source.readInt();
    }

    protected JobExperienceDataModel(Parcel in) {
        this.jobId = in.readInt();
        this.requirementStatus = in.readInt();
        this.industry = in.readParcelable(NameIdModel.class.getClassLoader());
        this.id = in.readInt();
        this.years = in.readInt();
    }

    public static final Parcelable.Creator<JobExperienceDataModel> CREATOR = new Parcelable.Creator<JobExperienceDataModel>() {
        @Override
        public JobExperienceDataModel createFromParcel(Parcel source) {
            return new JobExperienceDataModel(source);
        }

        @Override
        public JobExperienceDataModel[] newArray(int size) {
            return new JobExperienceDataModel[size];
        }
    };
}
