package com.antizon.uit_android.models.work;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;

import java.util.List;

public class ApplicantWorkExperienceModel implements Parcelable {
    private String companyName, jobTitle, startDate, endDate, responsibilities;
    private ApplicantDegreeDataModel degreeDataModel;
    private List<ApplicantReferenceModel> referencesList;
    private List<SkillDataModel> skillsList;

    public ApplicantWorkExperienceModel() {
    }

    public ApplicantWorkExperienceModel(String companyName, String jobTitle, String startDate, String endDate, String responsibilities, ApplicantDegreeDataModel degreeDataModel, List<ApplicantReferenceModel> referencesList, List<SkillDataModel> skillsList) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.responsibilities = responsibilities;
        this.degreeDataModel = degreeDataModel;
        this.referencesList = referencesList;
        this.skillsList = skillsList;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public ApplicantDegreeDataModel getDegreeDataModel() {
        return degreeDataModel;
    }

    public void setDegreeDataModel(ApplicantDegreeDataModel degreeDataModel) {
        this.degreeDataModel = degreeDataModel;
    }

    public List<ApplicantReferenceModel> getReferencesList() {
        return referencesList;
    }

    public void setReferencesList(List<ApplicantReferenceModel> referencesList) {
        this.referencesList = referencesList;
    }

    public List<SkillDataModel> getSkillsList() {
        return skillsList;
    }

    public void setSkillsList(List<SkillDataModel> skillsList) {
        this.skillsList = skillsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.companyName);
        dest.writeString(this.jobTitle);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.responsibilities);
        dest.writeParcelable(this.degreeDataModel, flags);
        dest.writeTypedList(this.referencesList);
        dest.writeTypedList(this.skillsList);
    }

    public void readFromParcel(Parcel source) {
        this.companyName = source.readString();
        this.jobTitle = source.readString();
        this.startDate = source.readString();
        this.endDate = source.readString();
        this.responsibilities = source.readString();
        this.degreeDataModel = source.readParcelable(ApplicantDegreeDataModel.class.getClassLoader());
        this.referencesList = source.createTypedArrayList(ApplicantReferenceModel.CREATOR);
        this.skillsList = source.createTypedArrayList(SkillDataModel.CREATOR);
    }

    protected ApplicantWorkExperienceModel(Parcel in) {
        this.companyName = in.readString();
        this.jobTitle = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.responsibilities = in.readString();
        this.degreeDataModel = in.readParcelable(ApplicantDegreeDataModel.class.getClassLoader());
        this.referencesList = in.createTypedArrayList(ApplicantReferenceModel.CREATOR);
        this.skillsList = in.createTypedArrayList(SkillDataModel.CREATOR);
    }

    public static final Creator<ApplicantWorkExperienceModel> CREATOR = new Creator<ApplicantWorkExperienceModel>() {
        @Override
        public ApplicantWorkExperienceModel createFromParcel(Parcel source) {
            return new ApplicantWorkExperienceModel(source);
        }

        @Override
        public ApplicantWorkExperienceModel[] newArray(int size) {
            return new ApplicantWorkExperienceModel[size];
        }
    };
}
