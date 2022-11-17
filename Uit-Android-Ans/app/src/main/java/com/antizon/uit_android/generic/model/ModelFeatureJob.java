package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ModelFeatureJob implements Parcelable {

    int id = 0, location_status = 0, employment_type = 0, job_status = 0, min_salary = 0,
            max_salary = 0, role = 0, user_id = 0, total_applications = 0;
    String name = "";
    String email = "", city = "", state = "", profile_image = "", job_title = "",
            created_at = "", reason_to_close = "";
    ModelApplicantDepartment industryDataModel;
    ModelUser userDataModel;
    ModelUser companyDataModel;
    List<ModelUser> applicationModel;

    protected ModelFeatureJob(Parcel in) {
        id = in.readInt();
        location_status = in.readInt();
        employment_type = in.readInt();
        job_status = in.readInt();
        min_salary = in.readInt();
        max_salary = in.readInt();
        role = in.readInt();
        user_id = in.readInt();
        total_applications = in.readInt();
        name = in.readString();
        email = in.readString();
        city = in.readString();
        state = in.readString();
        profile_image = in.readString();
        job_title = in.readString();
        created_at = in.readString();
        reason_to_close = in.readString();
        industryDataModel = in.readParcelable(ModelApplicantDepartment.class.getClassLoader());
        profileImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(location_status);
        dest.writeInt(employment_type);
        dest.writeInt(job_status);
        dest.writeInt(min_salary);
        dest.writeInt(max_salary);
        dest.writeInt(role);
        dest.writeInt(user_id);
        dest.writeInt(total_applications);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(profile_image);
        dest.writeString(job_title);
        dest.writeString(created_at);
        dest.writeString(reason_to_close);
        dest.writeParcelable(industryDataModel, flags);
        dest.writeString(profileImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelFeatureJob> CREATOR = new Creator<ModelFeatureJob>() {
        @Override
        public ModelFeatureJob createFromParcel(Parcel in) {
            return new ModelFeatureJob(in);
        }

        @Override
        public ModelFeatureJob[] newArray(int size) {
            return new ModelFeatureJob[size];
        }
    };

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    String profileImage;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ModelFeatureJob() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRole() {
        return role;
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getReason_to_close() {
        return reason_to_close;
    }

    public void setReason_to_close(String reason_to_close) {
        this.reason_to_close = reason_to_close;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ModelUser getCompanyDataModel() {
        return companyDataModel;
    }

    public void setCompanyDataModel(ModelUser companyDataModel) {
        this.companyDataModel = companyDataModel;
    }

    public List<ModelUser> getApplicationModel() {
        return applicationModel;
    }

    public void setApplicationModel(List<ModelUser> applicationModel) {
        this.applicationModel = applicationModel;
    }

    public void setUserDataModel(ModelUser userDataModel) {
        this.userDataModel = userDataModel;
    }
}
