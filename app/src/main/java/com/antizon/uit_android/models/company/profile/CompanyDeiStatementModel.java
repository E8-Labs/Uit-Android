package com.antizon.uit_android.models.company.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanyDeiStatementModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("deistatement")
    private String deistatement;
    @SerializedName("team_lead")
    private int team_lead;
    @SerializedName("erg")
    private int erg;
    @SerializedName("programming")
    private int programming;
    @SerializedName("training")
    private int training;
    @SerializedName("men")
    private int men;
    @SerializedName("women")
    private int women;
    @SerializedName("non_binary")
    private int non_binary;
    @SerializedName("lgbt")
    private int lgbt;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

    public CompanyDeiStatementModel() {
    }

    public CompanyDeiStatementModel(int id, String deistatement, int team_lead, int erg, int programming, int training, int men, int women, int non_binary, int lgbt, int user_id, String created_at, String updated_at) {
        this.id = id;
        this.deistatement = deistatement;
        this.team_lead = team_lead;
        this.erg = erg;
        this.programming = programming;
        this.training = training;
        this.men = men;
        this.women = women;
        this.non_binary = non_binary;
        this.lgbt = lgbt;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeistatement() {
        return deistatement;
    }

    public void setDeistatement(String deistatement) {
        this.deistatement = deistatement;
    }

    public int getTeam_lead() {
        return team_lead;
    }

    public void setTeam_lead(int team_lead) {
        this.team_lead = team_lead;
    }

    public int getErg() {
        return erg;
    }

    public void setErg(int erg) {
        this.erg = erg;
    }

    public int getProgramming() {
        return programming;
    }

    public void setProgramming(int programming) {
        this.programming = programming;
    }

    public int getTraining() {
        return training;
    }

    public void setTraining(int training) {
        this.training = training;
    }

    public int getMen() {
        return men;
    }

    public void setMen(int men) {
        this.men = men;
    }

    public int getWomen() {
        return women;
    }

    public void setWomen(int women) {
        this.women = women;
    }

    public int getNon_binary() {
        return non_binary;
    }

    public void setNon_binary(int non_binary) {
        this.non_binary = non_binary;
    }

    public int getLgbt() {
        return lgbt;
    }

    public void setLgbt(int lgbt) {
        this.lgbt = lgbt;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.deistatement);
        dest.writeInt(this.team_lead);
        dest.writeInt(this.erg);
        dest.writeInt(this.programming);
        dest.writeInt(this.training);
        dest.writeInt(this.men);
        dest.writeInt(this.women);
        dest.writeInt(this.non_binary);
        dest.writeInt(this.lgbt);
        dest.writeInt(this.user_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.deistatement = source.readString();
        this.team_lead = source.readInt();
        this.erg = source.readInt();
        this.programming = source.readInt();
        this.training = source.readInt();
        this.men = source.readInt();
        this.women = source.readInt();
        this.non_binary = source.readInt();
        this.lgbt = source.readInt();
        this.user_id = source.readInt();
        this.created_at = source.readString();
        this.updated_at = source.readString();
    }

    protected CompanyDeiStatementModel(Parcel in) {
        this.id = in.readInt();
        this.deistatement = in.readString();
        this.team_lead = in.readInt();
        this.erg = in.readInt();
        this.programming = in.readInt();
        this.training = in.readInt();
        this.men = in.readInt();
        this.women = in.readInt();
        this.non_binary = in.readInt();
        this.lgbt = in.readInt();
        this.user_id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<CompanyDeiStatementModel> CREATOR = new Parcelable.Creator<CompanyDeiStatementModel>() {
        @Override
        public CompanyDeiStatementModel createFromParcel(Parcel source) {
            return new CompanyDeiStatementModel(source);
        }

        @Override
        public CompanyDeiStatementModel[] newArray(int size) {
            return new CompanyDeiStatementModel[size];
        }
    };
}
