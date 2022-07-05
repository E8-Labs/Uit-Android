package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkExperienceModel implements Parcelable {

    String company , job,startYear,graduationDay,roleText,department;
    public WorkExperienceModel() {
    }
    protected WorkExperienceModel(Parcel in) {
        company = in.readString();
        job = in.readString();
        startYear = in.readString();
        graduationDay = in.readString();
        roleText = in.readString();
        department = in.readString();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getGraduationDay() {
        return graduationDay;
    }

    public void setGraduationDay(String graduationDay) {
        this.graduationDay = graduationDay;
    }

    public String getRoleText() {
        return roleText;
    }

    public void setRoleText(String roleText) {
        this.roleText = roleText;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static Creator<WorkExperienceModel> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<WorkExperienceModel> CREATOR = new Creator<WorkExperienceModel>() {
        @Override
        public WorkExperienceModel createFromParcel(Parcel in) {
            return new WorkExperienceModel(in);
        }

        @Override
        public WorkExperienceModel[] newArray(int size) {
            return new WorkExperienceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(company);
        parcel.writeString(job);
        parcel.writeString(startYear);
        parcel.writeString(graduationDay);
        parcel.writeString(roleText);
        parcel.writeString(department);
    }
}
