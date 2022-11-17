package com.antizon.uit_android.models.company.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyProfileDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_image")
    private String profile_image;
    @SerializedName("profile_banner")
    private String profile_banner;
    @SerializedName("website")
    private String website;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("phone")
    private String phone;
    @SerializedName("account_status")
    private int account_status;
    @SerializedName("address")
    private String address;
    @SerializedName("bio")
    private String bio;
    @SerializedName("dob")
    private String dob;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("role")
    private int role;
    @SerializedName("application_status")
    private int application_status;
    @SerializedName("dei_statement")
    private CompanyDeiStatementModel companyDeiStatement;
    @SerializedName("user_sizes")
    private List<NameIdModel> userSizesList;
    @SerializedName("user_stages")
    private List<NameIdModel> userStagesList;
    @SerializedName("demographic_info")
    private CompanyDemoGraphicInfoModel demoGraphicInfoModel;
    @SerializedName("industries")
    private List<NameIdModel> industries;
    @SerializedName("team_members")
    private List<GenericApplicantDataModel> companyTeamMembers;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("unread_notifications")
    private int unread_notifications;
    @SerializedName("unread_messages")
    private int unread_messages;
    @SerializedName("greenhouse_access_token")
    private String greenhouse_access_token;
    @SerializedName("greenhouse_user_id")
    private String greenhouse_user_id;
    @SerializedName("greenhouse_status")
    private int greenhouse_status;

    public CompanyProfileDataModel() {
    }

    public CompanyProfileDataModel(int id, String email, String name, String profile_image, String profile_banner, String website, String city, String state, String phone, int account_status, String address, String bio, String dob, String job_title, int role, int application_status, CompanyDeiStatementModel companyDeiStatement, List<NameIdModel> userSizesList, List<NameIdModel> userStagesList, CompanyDemoGraphicInfoModel demoGraphicInfoModel, List<NameIdModel> industries, List<GenericApplicantDataModel> companyTeamMembers, int user_id, int unread_notifications, int unread_messages, String greenhouse_access_token, String greenhouse_user_id, int greenhouse_status) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile_image = profile_image;
        this.profile_banner = profile_banner;
        this.website = website;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.account_status = account_status;
        this.address = address;
        this.bio = bio;
        this.dob = dob;
        this.job_title = job_title;
        this.role = role;
        this.application_status = application_status;
        this.companyDeiStatement = companyDeiStatement;
        this.userSizesList = userSizesList;
        this.userStagesList = userStagesList;
        this.demoGraphicInfoModel = demoGraphicInfoModel;
        this.industries = industries;
        this.companyTeamMembers = companyTeamMembers;
        this.user_id = user_id;
        this.unread_notifications = unread_notifications;
        this.unread_messages = unread_messages;
        this.greenhouse_access_token = greenhouse_access_token;
        this.greenhouse_user_id = greenhouse_user_id;
        this.greenhouse_status = greenhouse_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getProfile_banner() {
        return profile_banner;
    }

    public void setProfile_banner(String profile_banner) {
        this.profile_banner = profile_banner;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAccount_status() {
        return account_status;
    }

    public void setAccount_status(int account_status) {
        this.account_status = account_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getApplication_status() {
        return application_status;
    }

    public void setApplication_status(int application_status) {
        this.application_status = application_status;
    }

    public CompanyDeiStatementModel getCompanyDeiStatement() {
        return companyDeiStatement;
    }

    public void setCompanyDeiStatement(CompanyDeiStatementModel companyDeiStatement) {
        this.companyDeiStatement = companyDeiStatement;
    }

    public List<NameIdModel> getUserSizesList() {
        return userSizesList;
    }

    public void setUserSizesList(List<NameIdModel> userSizesList) {
        this.userSizesList = userSizesList;
    }

    public List<NameIdModel> getUserStagesList() {
        return userStagesList;
    }

    public void setUserStagesList(List<NameIdModel> userStagesList) {
        this.userStagesList = userStagesList;
    }

    public CompanyDemoGraphicInfoModel getDemoGraphicInfoModel() {
        return demoGraphicInfoModel;
    }

    public void setDemoGraphicInfoModel(CompanyDemoGraphicInfoModel demoGraphicInfoModel) {
        this.demoGraphicInfoModel = demoGraphicInfoModel;
    }

    public List<NameIdModel> getIndustries() {
        return industries;
    }

    public void setIndustries(List<NameIdModel> industries) {
        this.industries = industries;
    }

    public List<GenericApplicantDataModel> getCompanyTeamMembers() {
        return companyTeamMembers;
    }

    public void setCompanyTeamMembers(List<GenericApplicantDataModel> companyTeamMembers) {
        this.companyTeamMembers = companyTeamMembers;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUnread_notifications() {
        return unread_notifications;
    }

    public void setUnread_notifications(int unread_notifications) {
        this.unread_notifications = unread_notifications;
    }

    public int getUnread_messages() {
        return unread_messages;
    }

    public void setUnread_messages(int unread_messages) {
        this.unread_messages = unread_messages;
    }

    public String getGreenhouse_access_token() {
        return greenhouse_access_token;
    }

    public void setGreenhouse_access_token(String greenhouse_access_token) {
        this.greenhouse_access_token = greenhouse_access_token;
    }

    public String getGreenhouse_user_id() {
        return greenhouse_user_id;
    }

    public void setGreenhouse_user_id(String greenhouse_user_id) {
        this.greenhouse_user_id = greenhouse_user_id;
    }

    public int getGreenhouse_status() {
        return greenhouse_status;
    }

    public void setGreenhouse_status(int greenhouse_status) {
        this.greenhouse_status = greenhouse_status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.profile_image);
        dest.writeString(this.profile_banner);
        dest.writeString(this.website);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.phone);
        dest.writeInt(this.account_status);
        dest.writeString(this.address);
        dest.writeString(this.bio);
        dest.writeString(this.dob);
        dest.writeString(this.job_title);
        dest.writeInt(this.role);
        dest.writeInt(this.application_status);
        dest.writeParcelable(this.companyDeiStatement, flags);
        dest.writeTypedList(this.userSizesList);
        dest.writeTypedList(this.userStagesList);
        dest.writeParcelable(this.demoGraphicInfoModel, flags);
        dest.writeTypedList(this.industries);
        dest.writeTypedList(this.companyTeamMembers);
        dest.writeInt(this.user_id);
        dest.writeInt(this.unread_notifications);
        dest.writeInt(this.unread_messages);
        dest.writeString(this.greenhouse_access_token);
        dest.writeString(this.greenhouse_user_id);
        dest.writeInt(this.greenhouse_status);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.email = source.readString();
        this.name = source.readString();
        this.profile_image = source.readString();
        this.profile_banner = source.readString();
        this.website = source.readString();
        this.city = source.readString();
        this.state = source.readString();
        this.phone = source.readString();
        this.account_status = source.readInt();
        this.address = source.readString();
        this.bio = source.readString();
        this.dob = source.readString();
        this.job_title = source.readString();
        this.role = source.readInt();
        this.application_status = source.readInt();
        this.companyDeiStatement = source.readParcelable(CompanyDeiStatementModel.class.getClassLoader());
        this.userSizesList = source.createTypedArrayList(NameIdModel.CREATOR);
        this.userStagesList = source.createTypedArrayList(NameIdModel.CREATOR);
        this.demoGraphicInfoModel = source.readParcelable(CompanyDemoGraphicInfoModel.class.getClassLoader());
        this.industries = source.createTypedArrayList(NameIdModel.CREATOR);
        this.companyTeamMembers = source.createTypedArrayList(GenericApplicantDataModel.CREATOR);
        this.user_id = source.readInt();
        this.unread_notifications = source.readInt();
        this.unread_messages = source.readInt();
        this.greenhouse_access_token = source.readString();
        this.greenhouse_user_id = source.readString();
        this.greenhouse_status = source.readInt();
    }

    protected CompanyProfileDataModel(Parcel in) {
        this.id = in.readInt();
        this.email = in.readString();
        this.name = in.readString();
        this.profile_image = in.readString();
        this.profile_banner = in.readString();
        this.website = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.phone = in.readString();
        this.account_status = in.readInt();
        this.address = in.readString();
        this.bio = in.readString();
        this.dob = in.readString();
        this.job_title = in.readString();
        this.role = in.readInt();
        this.application_status = in.readInt();
        this.companyDeiStatement = in.readParcelable(CompanyDeiStatementModel.class.getClassLoader());
        this.userSizesList = in.createTypedArrayList(NameIdModel.CREATOR);
        this.userStagesList = in.createTypedArrayList(NameIdModel.CREATOR);
        this.demoGraphicInfoModel = in.readParcelable(CompanyDemoGraphicInfoModel.class.getClassLoader());
        this.industries = in.createTypedArrayList(NameIdModel.CREATOR);
        this.companyTeamMembers = in.createTypedArrayList(GenericApplicantDataModel.CREATOR);
        this.user_id = in.readInt();
        this.unread_notifications = in.readInt();
        this.unread_messages = in.readInt();
        this.greenhouse_access_token = in.readString();
        this.greenhouse_user_id = in.readString();
        this.greenhouse_status = in.readInt();
    }

    public static final Creator<CompanyProfileDataModel> CREATOR = new Creator<CompanyProfileDataModel>() {
        @Override
        public CompanyProfileDataModel createFromParcel(Parcel source) {
            return new CompanyProfileDataModel(source);
        }

        @Override
        public CompanyProfileDataModel[] newArray(int size) {
            return new CompanyProfileDataModel[size];
        }
    };
}
