package com.antizon.uit_android.models.applicant.home;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApplicantHomeJobDataModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("job_title")
    private String job_title;
    @SerializedName("industry")
    private ApplicantHomeIndustryDataModel industryDataModel;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("location_status")
    private int location_status;
    @SerializedName("employment_type")
    private int employment_type;
    @SerializedName("job_status")
    private int job_status;
    @SerializedName("min_salary")
    private int min_salary;
    @SerializedName("max_salary")
    private int max_salary;
    @SerializedName("user")
    private GenericUserDataModel userDataModel;
    @SerializedName("company")
    private GenericUserDataModel companyDataModel;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("reason_to_close")
    private String reason_to_close;
    @SerializedName("applications")
    private List<GenericUserDataModel> applications;
    @SerializedName("total_applications")
    private int total_applications;
    @SerializedName("match")
    private double match;

    public ApplicantHomeJobDataModel() {
    }

    public ApplicantHomeJobDataModel(int id, String job_title, ApplicantHomeIndustryDataModel industryDataModel, String city, String state, int location_status, int employment_type, int job_status, int min_salary, int max_salary, GenericUserDataModel userDataModel, GenericUserDataModel companyDataModel, String created_at, String reason_to_close, List<GenericUserDataModel> applications, int total_applications, int match) {
        this.id = id;
        this.job_title = job_title;
        this.industryDataModel = industryDataModel;
        this.city = city;
        this.state = state;
        this.location_status = location_status;
        this.employment_type = employment_type;
        this.job_status = job_status;
        this.min_salary = min_salary;
        this.max_salary = max_salary;
        this.userDataModel = userDataModel;
        this.companyDataModel = companyDataModel;
        this.created_at = created_at;
        this.reason_to_close = reason_to_close;
        this.applications = applications;
        this.total_applications = total_applications;
        this.match = match;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public ApplicantHomeIndustryDataModel getIndustryDataModel() {
        return industryDataModel;
    }

    public void setIndustryDataModel(ApplicantHomeIndustryDataModel industryDataModel) {
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

    public void setState(String state) {
        this.state = state;
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

    public GenericUserDataModel getUserDataModel() {
        return userDataModel;
    }

    public void setUserDataModel(GenericUserDataModel userDataModel) {
        this.userDataModel = userDataModel;
    }

    public GenericUserDataModel getCompanyDataModel() {
        return companyDataModel;
    }

    public void setCompanyDataModel(GenericUserDataModel companyDataModel) {
        this.companyDataModel = companyDataModel;
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

    public List<GenericUserDataModel> getApplications() {
        return applications;
    }

    public void setApplications(List<GenericUserDataModel> applications) {
        this.applications = applications;
    }

    public int getTotal_applications() {
        return total_applications;
    }

    public void setTotal_applications(int total_applications) {
        this.total_applications = total_applications;
    }

    public double getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.job_title);
        dest.writeParcelable(this.industryDataModel, flags);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeInt(this.location_status);
        dest.writeInt(this.employment_type);
        dest.writeInt(this.job_status);
        dest.writeInt(this.min_salary);
        dest.writeInt(this.max_salary);
        dest.writeParcelable(this.userDataModel, flags);
        dest.writeParcelable(this.companyDataModel, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.reason_to_close);
        dest.writeTypedList(this.applications);
        dest.writeInt(this.total_applications);
        dest.writeDouble(this.match);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.job_title = source.readString();
        this.industryDataModel = source.readParcelable(ApplicantHomeIndustryDataModel.class.getClassLoader());
        this.city = source.readString();
        this.state = source.readString();
        this.location_status = source.readInt();
        this.employment_type = source.readInt();
        this.job_status = source.readInt();
        this.min_salary = source.readInt();
        this.max_salary = source.readInt();
        this.userDataModel = source.readParcelable(GenericUserDataModel.class.getClassLoader());
        this.companyDataModel = source.readParcelable(GenericUserDataModel.class.getClassLoader());
        this.created_at = source.readString();
        this.reason_to_close = source.readString();
        this.applications = source.createTypedArrayList(GenericUserDataModel.CREATOR);
        this.total_applications = source.readInt();
        this.match = source.readInt();
    }

    protected ApplicantHomeJobDataModel(Parcel in) {
        this.id = in.readInt();
        this.job_title = in.readString();
        this.industryDataModel = in.readParcelable(ApplicantHomeIndustryDataModel.class.getClassLoader());
        this.city = in.readString();
        this.state = in.readString();
        this.location_status = in.readInt();
        this.employment_type = in.readInt();
        this.job_status = in.readInt();
        this.min_salary = in.readInt();
        this.max_salary = in.readInt();
        this.userDataModel = in.readParcelable(GenericUserDataModel.class.getClassLoader());
        this.companyDataModel = in.readParcelable(GenericUserDataModel.class.getClassLoader());
        this.created_at = in.readString();
        this.reason_to_close = in.readString();
        this.applications = in.createTypedArrayList(GenericUserDataModel.CREATOR);
        this.total_applications = in.readInt();
        this.match = in.readInt();
    }

    public static final Creator<ApplicantHomeJobDataModel> CREATOR = new Creator<ApplicantHomeJobDataModel>() {
        @Override
        public ApplicantHomeJobDataModel createFromParcel(Parcel source) {
            return new ApplicantHomeJobDataModel(source);
        }

        @Override
        public ApplicantHomeJobDataModel[] newArray(int size) {
            return new ApplicantHomeJobDataModel[size];
        }
    };
}
