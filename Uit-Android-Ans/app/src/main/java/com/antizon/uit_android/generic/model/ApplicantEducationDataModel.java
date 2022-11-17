package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;

public class ApplicantEducationDataModel implements Parcelable {
    private ApplicantDegreeDataModel applicantDegreeData;
    private String fieldOfStudy, schoolName, startDate, endDate;

    public ApplicantEducationDataModel() {
    }

    public ApplicantEducationDataModel(ApplicantDegreeDataModel applicantDegreeData, String fieldOfStudy, String schoolName, String startDate, String endDate) {
        this.applicantDegreeData = applicantDegreeData;
        this.fieldOfStudy = fieldOfStudy;
        this.schoolName = schoolName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ApplicantDegreeDataModel getApplicantDegreeData() {
        return applicantDegreeData;
    }

    public void setApplicantDegreeData(ApplicantDegreeDataModel applicantDegreeData) {
        this.applicantDegreeData = applicantDegreeData;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.applicantDegreeData, flags);
        dest.writeString(this.fieldOfStudy);
        dest.writeString(this.schoolName);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
    }

    public void readFromParcel(Parcel source) {
        this.applicantDegreeData = source.readParcelable(ApplicantDegreeDataModel.class.getClassLoader());
        this.fieldOfStudy = source.readString();
        this.schoolName = source.readString();
        this.startDate = source.readString();
        this.endDate = source.readString();
    }

    protected ApplicantEducationDataModel(Parcel in) {
        this.applicantDegreeData = in.readParcelable(ApplicantDegreeDataModel.class.getClassLoader());
        this.fieldOfStudy = in.readString();
        this.schoolName = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
    }

    public static final Parcelable.Creator<ApplicantEducationDataModel> CREATOR = new Parcelable.Creator<ApplicantEducationDataModel>() {
        @Override
        public ApplicantEducationDataModel createFromParcel(Parcel source) {
            return new ApplicantEducationDataModel(source);
        }

        @Override
        public ApplicantEducationDataModel[] newArray(int size) {
            return new ApplicantEducationDataModel[size];
        }
    };
}
