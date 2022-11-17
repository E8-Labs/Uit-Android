package com.antizon.uit_android.company.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.adapters.company.CompanyInvitedTeamMembersAdapter;
import com.antizon.uit_android.company.welcome.ActivityAddTeamInviteMember;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.invites.CompanyInvitedTeamMemberModel;
import com.antizon.uit_android.models.invites.CompanyInvitesResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.activities.NotificationsActivity;
import com.antizon.uit_android.recruiter.adapters.CompanyTeamMembersAdapter;
import com.antizon.uit_android.recruiter.welcome.CompanyTeamMemberProfileActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.CustomSwipeHelper;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyTeamFragment extends Fragment implements CompanyTeamMembersAdapter.CompanyTeamMembersAdapterCallBack {

    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    TabLayout tab_layout;
    RecyclerView recyclerview_companyTeamMembers;
    List<GenericApplicantDataModel> companyTeamMembersList;
    CompanyTeamMembersAdapter companyTeamMembersAdapter;

    List<CompanyInvitedTeamMemberModel> invitedTeamMembersList;
    CompanyInvitedTeamMembersAdapter companyInvitedTeamMembersAdapter;
    EditText edSearch;
    RoundedImageView user_ProfileImage;
    ImageView btn_inviteMember, dashboardNotification;

    AlertDialog deleteDialog;

    String type = "accepted";
    View rootView;

    CustomSwipeHelper customSwipeHelper;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_company_team, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");

        btn_inviteMember = rootView.findViewById(R.id.btn_inviteMember);
        user_ProfileImage = rootView.findViewById(R.id.user_ProfileImage);
        edSearch = rootView.findViewById(R.id.edSearch);
        tab_layout = rootView.findViewById(R.id.tab_layout);
        recyclerview_companyTeamMembers = rootView.findViewById(R.id.recyclerview_companyTeamMembers);
        dashboardNotification = rootView.findViewById(R.id.dashboardNotification);

        tab_layout.addTab(tab_layout.newTab().setText("Approved"));
        tab_layout.addTab(tab_layout.newTab().setText("Pending"));
        tab_layout.addTab(tab_layout.newTab().setText("Invited"));

        setMarginBetweenTabs(tab_layout);

        Utilities.loadImage(context, sessionManagement.getProfileImage(), user_ProfileImage);
        progressDialog.show();
        requestForCompanyTeamMembersList("Bearer " + sessionManagement.getToken(), "1", type);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                progressDialog.show();
                if (position == 0){
                    type = "accepted";
                    requestForCompanyTeamMembersList("Bearer " + sessionManagement.getToken(),"1", type);
                }else if (position == 1){
                    type = "pending";
                    requestForCompanyTeamMembersList("Bearer " + sessionManagement.getToken(),"4", type);
                }else if (position == 2){
                    type = "invited";
                    requestForCompanyInvitedMembersList("Bearer " + sessionManagement.getToken());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        customSwipeHelper = new CustomSwipeHelper(context, recyclerview_companyTeamMembers) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new CustomSwipeHelper.UnderlayButton(context,
                        "delete",
                        R.drawable.delete_job_ic,
                        Color.parseColor("#00FFFFFF"),
                        pos -> showAcceptRejectCompanyRequestPopup(pos, String.valueOf(companyTeamMembersList.get(pos).getUser_id()), "decline")
                ));
            }
        };
        customSwipeHelper.attachSwipe();

        btn_inviteMember.setOnClickListener(v -> {
            Intent addEducationIntent = new Intent(context, ActivityAddTeamInviteMember.class);
            addEducationIntent.putExtra("from", "companyInvite");
            onTeamMemberAddedLauncher.launch(addEducationIntent);
            requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        dashboardNotification.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationsActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        if (sessionManagement.getKeyUnreadNotifications().equals("0")){
            dashboardNotification.setImageResource(R.drawable.notification_all_read_ic);
        }else {
            dashboardNotification.setImageResource(R.drawable.notifications_ic);
        }

        return rootView;
    }


    public void requestForCompanyTeamMembersList(String authToken, String account_status, String type){
        Call<GetAdminApplicantsResponseModel> call = service.getCompanyMembersList(authToken, account_status);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Response<GetAdminApplicantsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        companyTeamMembersList = new ArrayList<>();
                        invitedTeamMembersList = new ArrayList<>();

                        companyTeamMembersList = response.body().getApplicantsList();
                        showCompanyTeamMembersRecyclerView(recyclerview_companyTeamMembers, companyTeamMembersList, type);
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(requireActivity(), t.getMessage());
            }
        });
    }

    public void requestForCompanyInvitedMembersList(String authToken){
        Call<CompanyInvitesResponseModel> call = service.getCompanyInvitedMembersList(authToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CompanyInvitesResponseModel> call, @NonNull Response<CompanyInvitesResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        companyTeamMembersList = new ArrayList<>();
                        invitedTeamMembersList = new ArrayList<>();

                        invitedTeamMembersList = response.body().getInvitedTeamMembersList();
                        showCompanyTeamInvitedMembersRecyclerView(recyclerview_companyTeamMembers, invitedTeamMembersList);
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<CompanyInvitesResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(requireActivity(), t.getMessage());
            }
        });
    }


    private void showCompanyTeamMembersRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> uitMembersList, String type){
        customSwipeHelper.setViewSwipeEnabled(type.equals("accepted"));

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyTeamMembersAdapter = new CompanyTeamMembersAdapter(context, uitMembersList, type,this);
        recyclerView.setAdapter(companyTeamMembersAdapter);
    }

    private void showCompanyTeamInvitedMembersRecyclerView(RecyclerView recyclerView, List<CompanyInvitedTeamMemberModel> invitedTeamMembersList){
        customSwipeHelper.setViewSwipeEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyInvitedTeamMembersAdapter = new CompanyInvitedTeamMembersAdapter(context,invitedTeamMembersList);
        recyclerView.setAdapter(companyInvitedTeamMembersAdapter);
    }

    private void setMarginBetweenTabs(TabLayout tabLayout) {
        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabs.getChildCount() - 1; i++) {
            View tab = tabs.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.setMarginEnd(50);
            tab.setLayoutParams(layoutParams);
            tabLayout.requestLayout();
        }
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, CompanyTeamMemberProfileActivity.class);
        intent.putExtra("applicantDataModel", companyTeamMembersList.get(position));
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onMessageClicked(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+companyTeamMembersList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(companyTeamMembersList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", companyTeamMembersList.get(position).getProfile_image());
        intent.putExtra("second_user_name", companyTeamMembersList.get(position).getName());
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onAcceptClicked(int position) {
        showAcceptRejectCompanyRequestPopup(position, String.valueOf(companyTeamMembersList.get(position).getUser_id()), "approve");
    }

    @Override
    public void onDeclineClicked(int position) {
        showAcceptRejectCompanyRequestPopup(position, String.valueOf(companyTeamMembersList.get(position).getUser_id()), "decline");
    }


    @SuppressLint("NotifyDataSetChanged")
    private void showAcceptRejectCompanyRequestPopup(int position, String companyId, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        if (type.equals("approve")){
            headerText =   "Approve Uit Member";
            areYouSure = "Are you sure you want to approve this uit member";
        }else if (type.equals("decline")){
            headerText =  "Decline Uit Member";
            areYouSure = "Are you sure you want to decline this uit member";
        }else {
            headerText =  "Delete Team Member";
            areYouSure = "Are you sure you want to delete this team member";
        }

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            requestForApproveCompany("Bearer " + sessionManagement.getToken(), companyId, type);
            companyTeamMembersList.remove(position);
            companyTeamMembersAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }



    private void requestForApproveCompany(String authToken, String companyId, String type) {
        Call<MainResponseModel> call;
        if (type.equals("approve")){
            call = service.approveCompanyMember(authToken, companyId);
        }else if (type.equals("decline")){
            call = service.rejectCompanyMember(authToken, companyId);
        }else {
            call = service.deleteUser(authToken, companyId);
        }
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (!status){
                            CustomCookieToast.showFailureToast(requireActivity(), response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(requireActivity(), response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), t.getLocalizedMessage());
            }
        });
    }

    ActivityResultLauncher<Intent> onTeamMemberAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                if (type.equals("invited")){
                    requestForCompanyInvitedMembersList("Bearer " + sessionManagement.getToken());
                }
            }
        }
    });

}