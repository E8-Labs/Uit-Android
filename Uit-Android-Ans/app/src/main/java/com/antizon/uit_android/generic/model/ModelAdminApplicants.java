package com.antizon.uit_android.generic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ModelAdminApplicants implements Parcelable {

    int id = 0, user_id = 0;
    String name = "", email = "", city = "", state = "", profile_image = "",job_title="", professional_info="", industries="";

    ModelApplicantDepartment industryDataModel;
    ModelUser modelUser;

    public ModelAdminApplicants() {
    }

    protected ModelAdminApplicants(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        name = in.readString();
        email = in.readString();
        city = in.readString();
        state = in.readString();
        profile_image = in.readString();
        job_title = in.readString();
        professional_info = in.readString();
        industries = in.readString();
        industryDataModel = in.readParcelable(ModelApplicantDepartment.class.getClassLoader());
        modelUser = in.readParcelable(ModelUser.class.getClassLoader());
    }

    public static final Creator<ModelAdminApplicants> CREATOR = new Creator<ModelAdminApplicants>() {
        @Override
        public ModelAdminApplicants createFromParcel(Parcel in) {
            return new ModelAdminApplicants(in);
        }

        @Override
        public ModelAdminApplicants[] newArray(int size) {
            return new ModelAdminApplicants[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModelUser getModelUser() {
        return modelUser;
    }

    public void setModelUser(ModelUser modelUser) {
        this.modelUser = modelUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ModelApplicantDepartment getIndustryDataModel() {
        return industryDataModel;
    }

    public void setIndustryDataModel(ModelApplicantDepartment industryDataModel) {
        this.industryDataModel = industryDataModel;
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

    public String getProfessional_info() {
        return professional_info;
    }

    public void setProfessional_info(String professional_info) {
        this.professional_info = professional_info;
    }

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
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

    public void setJob_title(String job_title) {
        this.job_title = job_title;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(user_id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(profile_image);
        parcel.writeString(job_title);
        parcel.writeString(professional_info);
        parcel.writeString(industries);
        parcel.writeParcelable(industryDataModel, i);
        parcel.writeParcelable(modelUser, i);
    }
}
