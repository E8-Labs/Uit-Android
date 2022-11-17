package com.antizon.uit_android.models.applicant;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.home.GenericUserDataModel;
import com.antizon.uit_android.models.profile.ApplicantProfileProfessionalInfoDataModel;
import com.google.gson.annotations.SerializedName;

public class GenericApplicantDataModel implements Parcelable {
     @SerializedName("id")
     private int id;
     @SerializedName("user_id")
     private int user_id;
     @SerializedName("role")
     private int role;
     @SerializedName("name")
     private String name;
     @SerializedName("email")
     private String email;
     @SerializedName("phone")
     private String phone;
     @SerializedName("city")
     private String city;
     @SerializedName("state")
     private String state;
     @SerializedName("job_title")
     private String job_title;
     @SerializedName("profile_image")
     private String profile_image;
     @SerializedName("parent")
     private GenericUserDataModel parentDataModel;
     @SerializedName("professional_info")
     private ApplicantProfileProfessionalInfoDataModel professional_info;

     public GenericApplicantDataModel() {
     }

     public GenericApplicantDataModel(int id, int user_id, int role, String name, String email, String phone, String city, String state, String job_title, String profile_image, GenericUserDataModel parentDataModel, ApplicantProfileProfessionalInfoDataModel professional_info) {
          this.id = id;
          this.user_id = user_id;
          this.role = role;
          this.name = name;
          this.email = email;
          this.phone = phone;
          this.city = city;
          this.state = state;
          this.job_title = job_title;
          this.profile_image = profile_image;
          this.parentDataModel = parentDataModel;
          this.professional_info = professional_info;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public int getUser_id() {
          return user_id;
     }

     public void setUser_id(int user_id) {
          this.user_id = user_id;
     }

     public int getRole() {
          return role;
     }

     public void setRole(int role) {
          this.role = role;
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

     public String getPhone() {
          return phone;
     }

     public void setPhone(String phone) {
          this.phone = phone;
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

     public String getJob_title() {
          return job_title;
     }

     public void setJob_title(String job_title) {
          this.job_title = job_title;
     }

     public String getProfile_image() {
          return profile_image;
     }

     public void setProfile_image(String profile_image) {
          this.profile_image = profile_image;
     }

     public GenericUserDataModel getParentDataModel() {
          return parentDataModel;
     }

     public void setParentDataModel(GenericUserDataModel parentDataModel) {
          this.parentDataModel = parentDataModel;
     }

     public ApplicantProfileProfessionalInfoDataModel getProfessional_info() {
          return professional_info;
     }

     public void setProfessional_info(ApplicantProfileProfessionalInfoDataModel professional_info) {
          this.professional_info = professional_info;
     }


     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(this.id);
          dest.writeInt(this.user_id);
          dest.writeInt(this.role);
          dest.writeString(this.name);
          dest.writeString(this.email);
          dest.writeString(this.phone);
          dest.writeString(this.city);
          dest.writeString(this.state);
          dest.writeString(this.job_title);
          dest.writeString(this.profile_image);
          dest.writeParcelable(this.parentDataModel, flags);
          dest.writeParcelable(this.professional_info, flags);
     }

     public void readFromParcel(Parcel source) {
          this.id = source.readInt();
          this.user_id = source.readInt();
          this.role = source.readInt();
          this.name = source.readString();
          this.email = source.readString();
          this.phone = source.readString();
          this.city = source.readString();
          this.state = source.readString();
          this.job_title = source.readString();
          this.profile_image = source.readString();
          this.parentDataModel = source.readParcelable(GenericUserDataModel.class.getClassLoader());
          this.professional_info = source.readParcelable(ApplicantProfileProfessionalInfoDataModel.class.getClassLoader());
     }

     protected GenericApplicantDataModel(Parcel in) {
          this.id = in.readInt();
          this.user_id = in.readInt();
          this.role = in.readInt();
          this.name = in.readString();
          this.email = in.readString();
          this.phone = in.readString();
          this.city = in.readString();
          this.state = in.readString();
          this.job_title = in.readString();
          this.profile_image = in.readString();
          this.parentDataModel = in.readParcelable(GenericUserDataModel.class.getClassLoader());
          this.professional_info = in.readParcelable(ApplicantProfileProfessionalInfoDataModel.class.getClassLoader());
     }

     public static final Creator<GenericApplicantDataModel> CREATOR = new Creator<GenericApplicantDataModel>() {
          @Override
          public GenericApplicantDataModel createFromParcel(Parcel source) {
               return new GenericApplicantDataModel(source);
          }

          @Override
          public GenericApplicantDataModel[] newArray(int size) {
               return new GenericApplicantDataModel[size];
          }
     };
}
