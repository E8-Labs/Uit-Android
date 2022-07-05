package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ModelAllJobs implements Parcelable {

    int id = 0, role = 0, user_id = 0, location_status = 0, employment_type = 0, job_status = 0,
            min_salary = 0, max_salary = 0, total_applications = 0;
    String name = "", profile_image = "", job_title = "", greenhouse_access_token = "", created_at = "",
            city = "", state = "", reason_to_close = "", match = "", selectedJobStatus="";

    ModelApplicantDepartment industryDataModel;
    ModelUser userDataModel;
    ModelUser companyDataModel;
    ModelUser applicantDataModel;
    List<ModelUser> applicantsList;

    protected ModelAllJobs(Parcel in) {
        id = in.readInt();
        role = in.readInt();
        user_id = in.readInt();
        location_status = in.readInt();
        employment_type = in.readInt();
        job_status = in.readInt();
        min_salary = in.readInt();
        max_salary = in.readInt();
        total_applications = in.readInt();
        name = in.readString();
        profile_image = in.readString();
        job_title = in.readString();
        greenhouse_access_token = in.readString();
        created_at = in.readString();
        city = in.readString();
        state = in.readString();
        reason_to_close = in.readString();
        match = in.readString();
        selectedJobStatus = in.readString();
        industryDataModel = in.readParcelable(ModelApplicantDepartment.class.getClassLoader());
        userDataModel = in.readParcelable(ModelUser.class.getClassLoader());
        companyDataModel = in.readParcelable(ModelUser.class.getClassLoader());
        applicantDataModel = in.readParcelable(ModelUser.class.getClassLoader());
        applicantsList = in.createTypedArrayList(ModelUser.CREATOR);
    }

    public static final Creator<ModelAllJobs> CREATOR = new Creator<ModelAllJobs>() {
        @Override
        public ModelAllJobs createFromParcel(Parcel in) {
            return new ModelAllJobs(in);
        }

        @Override
        public ModelAllJobs[] newArray(int size) {
            return new ModelAllJobs[size];
        }
    };

    public ModelUser getApplicantDataModel() {
        return applicantDataModel;
    }

    public void setApplicantDataModel(ModelUser applicantDataModel) {
        this.applicantDataModel = applicantDataModel;
    }

    public List<ModelUser> getApplicationsList() {
        return applicantsList;
    }

    public void setApplicationsList(List<ModelUser> applicationsList) {
        this.applicantsList = applicationsList;
    }

    public ModelAllJobs() {
    }

    public int getId() {
        return id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getRole() {
        return role;
    }

    public String getReason_to_close() {
        return reason_to_close;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public ModelApplicantDepartment getIndustryDataModel() {
        return industryDataModel;
    }

    public void setIndustryDataModel(ModelApplicantDepartment industryDataModel) {
        this.industryDataModel = industryDataModel;
    }

    public ModelUser getUserDataModel() {
        return userDataModel;
    }

    public void setUserDataModel(ModelUser userDataModel) {
        this.userDataModel = userDataModel;
    }

    public ModelUser getCompanyDataModel() {
        return companyDataModel;
    }

    public void setCompanyDataModel(ModelUser companyDataModel) {
        this.companyDataModel = companyDataModel;
    }

    public void setReason_to_close(String reason_to_close) {
        this.reason_to_close = reason_to_close;
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

    public void setRole(int role) {
        this.role = role;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public int getTotal_applications() {
        return total_applications;
    }

    public void setTotal_applications(int total_applications) {
        this.total_applications = total_applications;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getGreenhouse_access_token() {
        return greenhouse_access_token;
    }

    public void setGreenhouse_access_token(String greenhouse_access_token) {
        this.greenhouse_access_token = greenhouse_access_token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public int getLocation_status() {
        return location_status;
    }

    public void setLocation_status(int location_status) {
        this.location_status = location_status;
    }

    public int getEmployment_type() {
        return employment_type;
    }

    public void setEmployment_type(int employment_type) {
        this.employment_type = employment_type;
    }

    public int getJob_status() {
        return job_status;
    }

    public void setJob_status(int job_status) {
        this.job_status = job_status;
    }

    public int getMin_salary() {
        return min_salary;
    }

    public void setMin_salary(int min_salary) {
        this.min_salary = min_salary;
    }

    public int getMax_salary() {
        return max_salary;
    }

    public void setMax_salary(int max_salary) {
        this.max_salary = max_salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedJobStatus() {
        return selectedJobStatus;
    }

    public void setSelectedJobStatus(String selectedJobStatus) {
        this.selectedJobStatus = selectedJobStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(role);
        parcel.writeInt(user_id);
        parcel.writeInt(location_status);
        parcel.writeInt(employment_type);
        parcel.writeInt(job_status);
        parcel.writeInt(min_salary);
        parcel.writeInt(max_salary);
        parcel.writeInt(total_applications);
        parcel.writeString(name);
        parcel.writeString(profile_image);
        parcel.writeString(job_title);
        parcel.writeString(greenhouse_access_token);
        parcel.writeString(created_at);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(reason_to_close);
        parcel.writeString(match);
        parcel.writeString(selectedJobStatus);
        parcel.writeParcelable(industryDataModel, i);
        parcel.writeParcelable(userDataModel, i);
        parcel.writeParcelable(companyDataModel, i);
        parcel.writeParcelable(applicantDataModel, i);
        parcel.writeTypedList(applicantsList);
    }
}
