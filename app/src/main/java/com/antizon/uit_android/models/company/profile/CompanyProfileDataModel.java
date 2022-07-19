package com.antizon.uit_android.models.company.profile;

import com.antizon.uit_android.models.profile.NameIdModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyProfileDataModel {
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

    public CompanyProfileDataModel(int id, String email, String name, String profile_image, String profile_banner, String website, String city, String state, String phone, int account_status, String address, String bio, String dob, String job_title, int role, int application_status, CompanyDeiStatementModel companyDeiStatement, List<NameIdModel> userSizesList, List<NameIdModel> userStagesList, CompanyDemoGraphicInfoModel demoGraphicInfoModel, List<NameIdModel> industries, int unread_notifications, int unread_messages, String greenhouse_access_token, String greenhouse_user_id, int greenhouse_status) {
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

}
