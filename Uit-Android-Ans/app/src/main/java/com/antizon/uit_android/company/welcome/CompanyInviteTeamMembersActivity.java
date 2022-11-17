package com.antizon.uit_android.company.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.CompanyInviteAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.invite.InviteMemberModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.CustomSwipeHelper;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyInviteTeamMembersActivity extends AppCompatActivity implements CompanyInviteAdapter.CompanyInviteAdapterCallBack {
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    GetDataService service;

    TextView btnSkip;
    RelativeLayout btnBack, btn_sendInvite;
    RoundedImageView image_companyProfile;
    ImageView btnAddMember;
    RecyclerView recyclerview_members;
    List<InviteMemberModel> membersList;
    CompanyInviteAdapter companyInviteAdapter;

    AlertDialog deleteDialog;
    int updatedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_invite_team_member);
        Utilities.setWhiteBars(CompanyInviteTeamMembersActivity.this);
        context = CompanyInviteTeamMembersActivity.this;

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        initViews();

    }

    private void initViews() {
        membersList = new ArrayList<>();

        btnAddMember = findViewById(R.id.btnAddMember);
        btnSkip = findViewById(R.id.btnSkip);
        btnBack = findViewById(R.id.btnBack);
        btn_sendInvite = findViewById(R.id.btn_sendInvite);
        image_companyProfile = findViewById(R.id.image_companyProfile);
        recyclerview_members = findViewById(R.id.recyclerview_members);

        Utilities.loadImage(context, sessionManagement.getProfileImage(), image_companyProfile);

        btnBack.setOnClickListener(view -> onBackPressed());

        btn_sendInvite.setOnClickListener(v -> {
            if (membersList.size() == 0) {
                CustomCookieToast.showRequiredToast(CompanyInviteTeamMembersActivity.this, "Please add minimum one team member");
            } else {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForSendInvites(membersList);
            }

        });

        btnAddMember.setOnClickListener(v -> {
            Intent addEducationIntent = new Intent(context, ActivityAddTeamInviteMember.class);
            addEducationIntent.putExtra("from", "companyCreateInvite");
            addEducationIntent.putExtra("type", "add");
            onTeamMemberAddedLauncher.launch(addEducationIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnSkip.setOnClickListener(v -> openNextScreen());

        showTeamMembersRecyclerview(recyclerview_members, membersList);

        new CustomSwipeHelper(this, recyclerview_members) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new CustomSwipeHelper.UnderlayButton(context,
                        "delete",
                        R.drawable.delete_job_ic,
                        Color.parseColor("#00FFFFFF"),
                        pos -> showDeleteTeamMemberPopup(pos)
                ));
            }

        };
    }


    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteTeamMemberPopup(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyInviteTeamMembersActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(CompanyInviteTeamMembersActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String deleteTest = "Delete Team Member?";
        String areYouSure = "Are you sure you want to delete this team member";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);

        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            membersList.remove(position);
            companyInviteAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void showTeamMembersRecyclerview(RecyclerView recyclerView, List<InviteMemberModel> membersList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyInviteAdapter = new CompanyInviteAdapter(context, membersList , this);
        recyclerView.setAdapter(companyInviteAdapter);
    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyInviteTeamMembersActivity.this, CompanySummaryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onEditBtnClicked(int position) {
        updatedPosition = position;
        Intent addEducationIntent = new Intent(context,  ActivityAddTeamInviteMember.class);
        addEducationIntent.putExtra("from", "companyCreateInvite");
        addEducationIntent.putExtra("type", "edit");
        addEducationIntent.putExtra("inviteMemberModel", membersList.get(position));
        onUpdateTeamMemberLauncher.launch(addEducationIntent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onUpdateTeamMemberLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                InviteMemberModel inviteMemberModel = intent.getParcelableExtra("inviteMemberModel");
                membersList.set(updatedPosition, inviteMemberModel);
                companyInviteAdapter.notifyDataSetChanged();
            }
        }
    });

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
                        openNextScreen();
                    }else {
                        CustomCookieToast.showFailureToast(CompanyInviteTeamMembersActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyInviteTeamMembersActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyInviteTeamMembersActivity.this, "Failure!", t.getMessage());
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onTeamMemberAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                InviteMemberModel inviteMemberModel = intent.getParcelableExtra("inviteMemberModel");
                membersList.add(inviteMemberModel);
                companyInviteAdapter.notifyDataSetChanged();
            }
        }
    });
}