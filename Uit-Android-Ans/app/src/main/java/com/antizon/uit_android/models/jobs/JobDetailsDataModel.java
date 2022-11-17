package com.antizon.uit_android.models.jobs;

import android.os.Parcel;
import android.os.Parcelable;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JobDetailsDataModel implements Parcelable {
    @SerializedName("degree_required")
    private int degreeRequired;

    @SerializedName("employment_type")
    private int employmentType;

    @SerializedName("featured")
    private int featured;

    @SerializedName("city")
    private String city;

    @SerializedName("saved")
    private boolean saved;

    @SerializedName("applied")
    private boolean applied;

    @SerializedName("roles")
    private String roles;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("industry")
    private NameIdModel industry;

    @SerializedName("language")
    private NameIdModel language;

    @SerializedName("vaccination_required")
    private int vaccinationRequired;

    @SerializedName("experience")
    private List<JobExperienceDataModel> experience;

    @SerializedName("skills")
    private List<NameIdModel> skills;

    @SerializedName("responsibilities")
    private String responsibilities;

    @SerializedName("flagged")
    private boolean flagged;

    @SerializedName("min_salary")
    private int minSalary;

    @SerializedName("company")
    private JobCompanyDataModel company;

    @SerializedName("id")
    private int id;

    @SerializedName("state")
    private String state;

    @SerializedName("department")
    private NameIdModel department;

    @SerializedName("lang")
    private double lang;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("lat")
    private double lat;

    @SerializedName("location_status")
    private int locationStatus;

    @SerializedName("company_id")
    private int companyId;

    @SerializedName("degree")
    private NameIdModel degree;

    @SerializedName("match")
    private double match;

    @SerializedName("job_status")
    private int jobStatus;

    @SerializedName("max_salary")
    private int maxSalary;

    @SerializedName("total_applications")
    private int totalApplications;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("user")
    private GenericApplicantDataModel user;

    @SerializedName("language_required")
    private int languageRequired;

    @SerializedName("applications")
    private List<GenericApplicantDataModel> applicantsList;

    public JobDetailsDataModel() {
    }

    public JobDetailsDataModel(int degreeRequired, int employmentType, int featured, String city, boolean saved, boolean applied, String roles, String createdAt, NameIdModel industry, NameIdModel language, int vaccinationRequired, List<JobExperienceDataModel> experience, List<NameIdModel> skills, String responsibilities, boolean flagged, int minSalary, JobCompanyDataModel company, int id, String state, NameIdModel department, double lang, String deadline, String jobTitle, double lat, int locationStatus, int companyId, NameIdModel degree, double match, int jobStatus, int maxSalary, int totalApplications, int userId, GenericApplicantDataModel user, int languageRequired, List<GenericApplicantDataModel> applicantsList) {
        this.degreeRequired = degreeRequired;
        this.employmentType = employmentType;
        this.featured = featured;
        this.city = city;
        this.saved = saved;
        this.applied = applied;
        this.roles = roles;
        this.createdAt = createdAt;
        this.industry = industry;
        this.language = language;
        this.vaccinationRequired = vaccinationRequired;
        this.experience = experience;
        this.skills = skills;
        this.responsibilities = responsibilities;
        this.flagged = flagged;
        this.minSalary = minSalary;
        this.company = company;
        this.id = id;
        this.state = state;
        this.department = department;
        this.lang = lang;
        this.deadline = deadline;
        this.jobTitle = jobTitle;
        this.lat = lat;
        this.locationStatus = locationStatus;
        this.companyId = companyId;
        this.degree = degree;
        this.match = match;
        this.jobStatus = jobStatus;
        this.maxSalary = maxSalary;
        this.totalApplications = totalApplications;
        this.userId = userId;
        this.user = user;
        this.languageRequired = languageRequired;
        this.applicantsList = applicantsList;
    }

    public int getDegreeRequired() {
        return degreeRequired;
    }

    public void setDegreeRequired(int degreeRequired) {
        this.degreeRequired = degreeRequired;
    }

    public int getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(int employmentType) {
        this.employmentType = employmentType;
    }

    public int getFeatured() {
        return featured;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public NameIdModel getIndustry() {
        return industry;
    }

    public void setIndustry(NameIdModel industry) {
        this.industry = industry;
    }

    public NameIdModel getLanguage() {
        return language;
    }

    public void setLanguage(NameIdModel language) {
        this.language = language;
    }

    public int getVaccinationRequired() {
        return vaccinationRequired;
    }

    public void setVaccinationRequired(int vaccinationRequired) {
        this.vaccinationRequired = vaccinationRequired;
    }

    public List<JobExperienceDataModel> getExperience() {
        return experience;
    }

    public void setExperience(List<JobExperienceDataModel> experience) {
        this.experience = experience;
    }

    public List<NameIdModel> getSkills() {
        return skills;
    }

    public void setSkills(List<NameIdModel> skills) {
        this.skills = skills;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public JobCompanyDataModel getCompany() {
        return company;
    }

    public void setCompany(JobCompanyDataModel company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public NameIdModel getDepartment() {
        return department;
    }

    public void setDepartment(NameIdModel department) {
        this.department = department;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(int locationStatus) {
        this.locationStatus = locationStatus;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public NameIdModel getDegree() {
        return degree;
    }

    public void setDegree(NameIdModel degree) {
        this.degree = degree;
    }

    public double getMatch() {
        return match;
    }

    public void setMatch(double match) {
        this.match = match;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public GenericApplicantDataModel getUser() {
        return user;
    }

    public void setUser(GenericApplicantDataModel user) {
        this.user = user;
    }

    public int getLanguageRequired() {
        return languageRequired;
    }

    public void setLanguageRequired(int languageRequired) {
        this.languageRequired = languageRequired;
    }

    public List<GenericApplicantDataModel> getApplicantsList() {
        return applicantsList;
    }

    public void setApplicantsList(List<GenericApplicantDataModel> applicantsList) {
        this.applicantsList = applicantsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.degreeRequired);
        dest.writeInt(this.employmentType);
        dest.writeInt(this.featured);
        dest.writeString(this.city);
        dest.writeByte(this.saved ? (byte) 1 : (byte) 0);
        dest.writeByte(this.applied ? (byte) 1 : (byte) 0);
        dest.writeString(this.roles);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.industry, flags);
        dest.writeParcelable(this.language, flags);
        dest.writeInt(this.vaccinationRequired);
        dest.writeTypedList(this.experience);
        dest.writeTypedList(this.skills);
        dest.writeString(this.responsibilities);
        dest.writeByte(this.flagged ? (byte) 1 : (byte) 0);
        dest.writeInt(this.minSalary);
        dest.writeParcelable(this.company, flags);
        dest.writeInt(this.id);
        dest.writeString(this.state);
        dest.writeParcelable(this.department, flags);
        dest.writeDouble(this.lang);
        dest.writeString(this.deadline);
        dest.writeString(this.jobTitle);
        dest.writeDouble(this.lat);
        dest.writeInt(this.locationStatus);
        dest.writeInt(this.companyId);
        dest.writeParcelable(this.degree, flags);
        dest.writeDouble(this.match);
        dest.writeInt(this.jobStatus);
        dest.writeInt(this.maxSalary);
        dest.writeInt(this.totalApplications);
        dest.writeInt(this.userId);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.languageRequired);
        dest.writeTypedList(this.applicantsList);
    }

    public void readFromParcel(Parcel source) {
        this.degreeRequired = source.readInt();
        this.employmentType = source.readInt();
        this.featured = source.readInt();
        this.city = source.readString();
        this.saved = source.readByte() != 0;
        this.applied = source.readByte() != 0;
        this.roles = source.readString();
        this.createdAt = source.readString();
        this.industry = source.readParcelable(NameIdModel.class.getClassLoader());
        this.language = source.readParcelable(NameIdModel.class.getClassLoader());
        this.vaccinationRequired = source.readInt();
        this.experience = source.createTypedArrayList(JobExperienceDataModel.CREATOR);
        this.skills = source.createTypedArrayList(NameIdModel.CREATOR);
        this.responsibilities = source.readString();
        this.flagged = source.readByte() != 0;
        this.minSalary = source.readInt();
        this.company = source.readParcelable(JobCompanyDataModel.class.getClassLoader());
        this.id = source.readInt();
        this.state = source.readString();
        this.department = source.readParcelable(NameIdModel.class.getClassLoader());
        this.lang = source.readDouble();
        this.deadline = source.readString();
        this.jobTitle = source.readString();
        this.lat = source.readDouble();
        this.locationStatus = source.readInt();
        this.companyId = source.readInt();
        this.degree = source.readParcelable(NameIdModel.class.getClassLoader());
        this.match = source.readDouble();
        this.jobStatus = source.readInt();
        this.maxSalary = source.readInt();
        this.totalApplications = source.readInt();
        this.userId = source.readInt();
        this.user = source.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.languageRequired = source.readInt();
        this.applicantsList = source.createTypedArrayList(GenericApplicantDataModel.CREATOR);
    }

    protected JobDetailsDataModel(Parcel in) {
        this.degreeRequired = in.readInt();
        this.employmentType = in.readInt();
        this.featured = in.readInt();
        this.city = in.readString();
        this.saved = in.readByte() != 0;
        this.applied = in.readByte() != 0;
        this.roles = in.readString();
        this.createdAt = in.readString();
        this.industry = in.readParcelable(NameIdModel.class.getClassLoader());
        this.language = in.readParcelable(NameIdModel.class.getClassLoader());
        this.vaccinationRequired = in.readInt();
        this.experience = in.createTypedArrayList(JobExperienceDataModel.CREATOR);
        this.skills = in.createTypedArrayList(NameIdModel.CREATOR);
        this.responsibilities = in.readString();
        this.flagged = in.readByte() != 0;
        this.minSalary = in.readInt();
        this.company = in.readParcelable(JobCompanyDataModel.class.getClassLoader());
        this.id = in.readInt();
        this.state = in.readString();
        this.department = in.readParcelable(NameIdModel.class.getClassLoader());
        this.lang = in.readDouble();
        this.deadline = in.readString();
        this.jobTitle = in.readString();
        this.lat = in.readDouble();
        this.locationStatus = in.readInt();
        this.companyId = in.readInt();
        this.degree = in.readParcelable(NameIdModel.class.getClassLoader());
        this.match = in.readDouble();
        this.jobStatus = in.readInt();
        this.maxSalary = in.readInt();
        this.totalApplications = in.readInt();
        this.userId = in.readInt();
        this.user = in.readParcelable(GenericApplicantDataModel.class.getClassLoader());
        this.languageRequired = in.readInt();
        this.applicantsList = in.createTypedArrayList(GenericApplicantDataModel.CREATOR);
    }

    public static final Creator<JobDetailsDataModel> CREATOR = new Creator<JobDetailsDataModel>() {
        @Override
        public JobDetailsDataModel createFromParcel(Parcel source) {
            return new JobDetailsDataModel(source);
        }

        @Override
        public JobDetailsDataModel[] newArray(int size) {
            return new JobDetailsDataModel[size];
        }
    };
}
