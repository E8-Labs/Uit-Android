package com.antizon.uit_android.uit_members.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.uit_admin.adapters.AdminUitMembersAdapter;
import com.antizon.uit_android.uit_members.welcome.UitTeamMemberProfileActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UitTeamMemberListFragment extends Fragment implements AdminUitMembersAdapter.AdminUitMembersAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;

    RoundedImageView userProfile;
    TextView text_logout;
    RecyclerView recyclerview_uitTeamMembers;
    List<GenericApplicantDataModel> uitMembersList;
    AdminUitMembersAdapter adminUitMembersAdapter;

    View rootView;

    EditText edSearch;
    RelativeLayout layout_noApplicants;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_uit_team_member_list, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        initViews(rootView);

        return rootView;

    }

    private void initViews(View rootView) {
        sessionManagement = new SessionManagement(context);
        userProfile = rootView.findViewById(R.id.userProfile);
        text_logout = rootView.findViewById(R.id.text_logout);
        recyclerview_uitTeamMembers = rootView.findViewById(R.id.recyclerview_uitTeamMembers);
        edSearch = rootView.findViewById(R.id.edSearch);
        layout_noApplicants = rootView.findViewById(R.id.layout_noApplicants);

        Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), userProfile);

        text_logout.setOnClickListener(v -> doYouWantToLogout());

        requestForUitMembersList("Bearer " + sessionManagement.getToken(), "1");

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<GenericApplicantDataModel> filteredList = new ArrayList<>();
        for (GenericApplicantDataModel item : uitMembersList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.size() == 0){
            recyclerview_uitTeamMembers.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerview_uitTeamMembers.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            adminUitMembersAdapter.filterList(filteredList);
        }
    }

    private void doYouWantToLogout(){

        AlertDialog reportPostPopup;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        final View customLayout =  LayoutInflater.from(context).inflate(R.layout.popup_yes_no, null);
        builder.setView(customLayout);
        String sure = "Are you sure you want to logout ?";

        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_cancel = customLayout.findViewById(R.id.text_No);
        TextView text_sure = customLayout.findViewById(R.id.text_sure);

        text_sure.setText(sure);

        reportPostPopup = builder.create();
        reportPostPopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        reportPostPopup.show();
        reportPostPopup.setCancelable(false);
        btn_cancel.setOnClickListener(v -> reportPostPopup.dismiss());

        btn_yes.setOnClickListener(v -> {
            // Clear SharedPref and send to login
            sessionManagement.logoutUser();
            reportPostPopup.dismiss();
            requireActivity().finishAffinity();
            Intent intent = new Intent(context, SignInActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

        });
    }

    public void requestForUitMembersList(String authToken, String account_status){
        Call<GetAdminApplicantsResponseModel> call = service.getUitMembersList(authToken, account_status,"0");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Response<GetAdminApplicantsResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        recyclerview_uitTeamMembers.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);

                        uitMembersList = new ArrayList<>();
                        uitMembersList = response.body().getApplicantsList();

                        // Remove logged in uit team member account

                        for (int i = 0; i < uitMembersList.size(); i++) {
                            GenericApplicantDataModel memberDataModel = uitMembersList.get(i);
                            if (memberDataModel !=null){
                                if (sessionManagement.getKeyId().equals(String.valueOf(memberDataModel.getUser_id()))){
                                        uitMembersList.remove(i);
                                        break;
                                }
                            }
                        }

                        showUitMembersRecyclerView(recyclerview_uitTeamMembers, uitMembersList);
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), t.getMessage());
            }
        });
    }

    private void showUitMembersRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> uitMembersList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminUitMembersAdapter = new AdminUitMembersAdapter(context, uitMembersList, 1, this);
        recyclerView.setAdapter(adminUitMembersAdapter);
    }


    @Override
    public void onItemClick(int position) {
        GenericApplicantDataModel applicantDataModel = uitMembersList.get(position);
        if (applicantDataModel !=null){
            Intent intent = new Intent(context, UitTeamMemberProfileActivity.class);
            intent.putExtra("applicantDataModel", applicantDataModel);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        }
    }

    @Override
    public void onAcceptClicked(int position) {

    }

    @Override
    public void onDeclineClicked(int position) {

    }

    @Override
    public void onMessageClicked(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+uitMembersList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(uitMembersList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", uitMembersList.get(position).getProfile_image());
        intent.putExtra("second_user_name", uitMembersList.get(position).getName());
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}