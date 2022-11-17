package com.antizon.uit_android.models.invites;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyInvitesResponseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<CompanyInvitedTeamMemberModel> invitedTeamMembersList;

    public CompanyInvitesResponseModel() {
    }

    public CompanyInvitesResponseModel(boolean status, String message, List<CompanyInvitedTeamMemberModel> invitedTeamMembersList) {
        this.status = status;
        this.message = message;
        this.invitedTeamMembersList = invitedTeamMembersList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CompanyInvitedTeamMemberModel> getInvitedTeamMembersList() {
        return invitedTeamMembersList;
    }

    public void setInvitedTeamMembersList(List<CompanyInvitedTeamMemberModel> invitedTeamMembersList) {
        this.invitedTeamMembersList = invitedTeamMembersList;
    }
}
