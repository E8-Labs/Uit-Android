package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.invite.InviteMemberModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddTeamInviteMember extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    GetDataService service;

    RelativeLayout btnBack;
    TextView btnSave;
    EditText editText_memberName, editText_memberEmail;

    String from, type;
    InviteMemberModel inviteMemberModel;

    List<InviteMemberModel> membersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_invite_member);
        Utilities.setWhiteBars(ActivityAddTeamInviteMember.this);
        context = ActivityAddTeamInviteMember.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        from = getIntent().getStringExtra("from");

        btnBack = findViewById(R.id.btnBack);
        editText_memberName = findViewById(R.id.editText_memberName);
        editText_memberEmail = findViewById(R.id.editText_memberEmail);
        btnSave = findViewById(R.id.btnSave);
        btnBack.setOnClickListener(v -> onBackPressed());



        if (from.equals("companyCreateInvite")){
            type = getIntent().getStringExtra("type");
            btnSave.setText(context.getString(R.string.add));
            if (type.equals("edit")) {
                inviteMemberModel = getIntent().getParcelableExtra("inviteMemberModel");
                editText_memberName.setText(inviteMemberModel.getMemberName());
                editText_memberEmail.setText(inviteMemberModel.getMemberEmail());
            }
        }else {
            btnSave.setText(context.getString(R.string.sendInvitation));
        }






        btnSave.setOnClickListener(v -> {
            String name = editText_memberName.getText().toString();
            String email = editText_memberEmail.getText().toString();

            if (name.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityAddTeamInviteMember.this, "Please enter name of team member");
            } else if (email.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityAddTeamInviteMember.this, "Please enter email of team member");
            } else if (!Utilities.isValidEmail(email)) {
                CustomCookieToast.showRequiredToast(ActivityAddTeamInviteMember.this, "Please enter valid email of team member");
            } else {
                Utilities.hideKeyboard(v, context);
                if (from.equals("companyCreateInvite")){
                    Intent intent = new Intent();
                    intent.putExtra("inviteMemberModel", new InviteMemberModel(name, email));
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
                }else {
                    membersList = new ArrayList<>();
                    membersList.add(new InviteMemberModel(name, email));
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    requestForSendInvites(membersList);
                }
            }

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }


    private void requestForSendInvites(List<InviteMemberModel> membersList) {

        JsonArray membersArray = new JsonArray();
        for (int i = 0; i < membersList.size(); i++) {
            JsonObject memberObject = new JsonObject();
            memberObject.addProperty("name", membersList.get(i).getMemberName());
            memberObject.addProperty("email", membersList.get(i).getMemberEmail());
            membersArray.add(memberObject);
        }

        JsonObject mainObject = new JsonObject();
        mainObject.add("invitations", membersArray);

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Call<MainResponseModel> call = service.sendCompanyInvites("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
                    }else {
                        CustomCookieToast.showFailureToast(ActivityAddTeamInviteMember.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityAddTeamInviteMember.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ActivityAddTeamInviteMember.this, "Failure!", t.getMessage());
            }
        });
    }
}