package com.antizon.uit_android.models.company.invite;

import android.os.Parcel;
import android.os.Parcelable;

public class InviteMemberModel implements Parcelable {
    private String memberName, memberEmail;

    public InviteMemberModel() {
    }

    public InviteMemberModel(String memberName, String memberEmail) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberName);
        dest.writeString(this.memberEmail);
    }

    public void readFromParcel(Parcel source) {
        this.memberName = source.readString();
        this.memberEmail = source.readString();
    }

    protected InviteMemberModel(Parcel in) {
        this.memberName = in.readString();
        this.memberEmail = in.readString();
    }

    public static final Parcelable.Creator<InviteMemberModel> CREATOR = new Parcelable.Creator<InviteMemberModel>() {
        @Override
        public InviteMemberModel createFromParcel(Parcel source) {
            return new InviteMemberModel(source);
        }

        @Override
        public InviteMemberModel[] newArray(int size) {
            return new InviteMemberModel[size];
        }
    };
}
