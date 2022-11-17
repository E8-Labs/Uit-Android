package com.antizon.uit_android.models.jobs;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleJobDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("job_title")
    private String jobTitle;
    @SerializedName("industry")
    private NameIdModel industry;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("location_status")
    private int locationStatus;
    @SerializedName("employment_type")
    private int employmentType;
    @SerializedName("job_status")
    private int jobStatus;
    @SerializedName("min_salary")
    private int minSalary;
    @SerializedName("max_salary")
    private int maxSalary;
    @SerializedName("user")
    private GenericApplicantDataModel user;
    @SerializedName("company")
    private GenericApplicantDataModel company;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("reason_to_close")
    private String reasonToClose;
    @SerializedName("applications")
    private List<GenericApplicantDataModel> applicantsList;
    @SerializedName("total_applications")
    private int totalApplications;
    @SerializedName("match")
    private double match;

    public SingleJobDataModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public NameIdModel getIndustry() {
        return industry;
    }

    public void setIndustry(NameIdModel industry) {
        this.industry = industry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(int locationStatus) {
        this.locationStatus = locationStatus;
    }

    public int getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(int employmentType) {
        this.employmentType = employmentType;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public GenericApplicantDataModel getUser() {
        return user;
    }

    public void setUser(GenericApplicantDataModel user) {
        this.user = user;
    }

    public GenericApplicantDataModel getCompany() {
        return company;
    }

    public void setCompany(GenericApplicantDataModel company) {
        this.company = company;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReasonToClose() {
        return reasonToClose;
    }

    public void setReasonToClose(String reasonToClose) {
        this.reasonToClose = reasonToClose;
    }

    public List<GenericApplicantDataModel> getApplicantsList() {
        return applicantsList;
    }

    public void setApplicantsList(List<GenericApplicantDataModel> applicantsList) {
        this.applicantsList = applicantsList;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public double getMatch() {
        return match;
    }

    public void setMatch(double match) {
        this.match = match;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.jobTitle);
        dest.writeParcelable(this.industry, flags);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeInt(this.locationStatus);
        dest.writeInt(this.employmentType);
        dest.writeInt(this.jobStatus);
        dest.writeInt(this.minSalary);
        dest.writeInt(this.maxSalary);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.company, flags);
        dest.writeString(this.createdAt);
        dest.writeString(this.reasonToClose);
        dest.writeTypedList(this.applicantsList);
        dest.writeInt(this.totalApplications);
        dest.writeDouble(this.match);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.jobTitle = source.readString();
        this.industry = source.readParcelable(NameIdModel.class.getClassLoader());
        this.city = source.readString();
        this.state = source.readString();
        this.locationStatus = source.readInt();
        this.employmentType = source.readInt();
        this.jobStatus = source.readInt();
        this.minSalary = source.readInt();
        this.maxSalary = source.readInt();
        this.user = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.company = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.createdAt = source.readString();
        this.reasonToClose = source.readString();
        this.applicantsList = source.createTypedArrayList(GenericApplicantDataModel.CREATOR);
        this.totalApplications = source.readInt();
        this.match = source.readDouble();
    }

    protected SingleJobDataModel(Parcel in) {
        this.id = in.readInt();
        this.jobTitle = in.readString();
        this.industry = in.readParcelable(NameIdModel.class.getClassLoader());
        this.city = in.readString();
        this.state = in.readString();
        this.locationStatus = in.readInt();
        this.employmentType = in.readInt();
        this.jobStatus = in.readInt();
        this.minSalary = in.readInt();
        this.maxSalary = in.readInt();
        this.user = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.company = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.createdAt = in.readString();
        this.reasonToClose = in.readString();
        this.applicantsList = in.createTypedArrayList(GenericApplicantDataModel.CREATOR);
        this.totalApplications = in.readInt();
        this.match = in.readDouble();
    }

    public static final Parcelable.Creator<SingleJobDataModel> CREATOR = new Parcelable.Creator<SingleJobDataModel>() {
        @Override
        public SingleJobDataModel createFromParcel(Parcel source) {
            return new SingleJobDataModel(source);
        }

        @Override
        public SingleJobDataModel[] newArray(int size) {
            return new SingleJobDataModel[size];
        }
    };
}
