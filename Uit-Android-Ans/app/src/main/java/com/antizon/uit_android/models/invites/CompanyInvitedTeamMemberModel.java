package com.antizon.uit_android.models.invites;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanyInvitedTeamMemberModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    public CompanyInvitedTeamMemberModel(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.email = source.readString();
    }

    protected CompanyInvitedTeamMemberModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<CompanyInvitedTeamMemberModel> CREATOR = new Parcelable.Creator<CompanyInvitedTeamMemberModel>() {
        @Override
        public CompanyInvitedTeamMemberModel createFromParcel(Parcel source) {
            return new CompanyInvitedTeamMemberModel(source);
        }

        @Override
        public CompanyInvitedTeamMemberModel[] newArray(int size) {
            return new CompanyInvitedTeamMemberModel[size];
        }
    };
}
