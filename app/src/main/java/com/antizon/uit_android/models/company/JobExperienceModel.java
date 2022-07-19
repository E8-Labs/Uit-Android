package com.antizon.uit_android.models.company;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;

public class JobExperienceModel implements Parcelable {
    private ApplicantDegreeDataModel selectedIndustryModel;
    private String totalExperience;
    private boolean isRequired;

    public JobExperienceModel() {
    }

    public JobExperienceModel(ApplicantDegreeDataModel selectedIndustryModel, String totalExperience, boolean isRequired) {
        this.selectedIndustryModel = selectedIndustryModel;
        this.totalExperience = totalExperience;
        this.isRequired = isRequired;
    }

    public ApplicantDegreeDataModel getSelectedIndustryModel() {
        return selectedIndustryModel;
    }

    public void setSelectedIndustryModel(ApplicantDegreeDataModel selectedIndustryModel) {
        this.selectedIndustryModel = selectedIndustryModel;
    }

    public String getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(String totalExperience) {
        this.totalExperience = totalExperience;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.selectedIndustryModel, flags);
        dest.writeString(this.totalExperience);
        dest.writeByte(this.isRequired ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.selectedIndustryModel = source.readParcelable(ApplicantDegreeDataModel.class.getClassLoader());
        this.totalExperience = source.readString();
        this.isRequired = source.readByte() != 0;
    }

    protected JobExperienceModel(Parcel in) {
        this.selectedIndustryModel = in.readParcelable(ApplicantDegreeDataModel.class.getClassLoader());
        this.totalExperience = in.readString();
        this.isRequired = in.readByte() != 0;
    }

    public static final Creator<JobExperienceModel> CREATOR = new Creator<JobExperienceModel>() {
        @Override
        public JobExperienceModel createFromParcel(Parcel source) {
            return new JobExperienceModel(source);
        }

        @Override
        public JobExperienceModel[] newArray(int size) {
            return new JobExperienceModel[size];
        }
    };
}
