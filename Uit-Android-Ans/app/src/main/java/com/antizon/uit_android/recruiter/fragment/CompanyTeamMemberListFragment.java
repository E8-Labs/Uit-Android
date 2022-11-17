package com.antizon.uit_android.recruiter.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.recruiter.adapters.CompanyTeamMembersAdapter;
import com.antizon.uit_android.recruiter.welcome.CompanyTeamMemberProfileActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyTeamMemberListFragment extends Fragment implements CompanyTeamMembersAdapter.CompanyTeamMembersAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;

    RoundedImageView userProfile;
    RecyclerView recyclerview_companyTeamMembers;
    List<GenericApplicantDataModel> companyMembersList;
    CompanyTeamMembersAdapter companyTeamMembersAdapter;
    EditText edSearch;
    View rootView;

    RelativeLayout layout_noApplicants;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView  = inflater.inflate(R.layout.fragment_company_team_member_list, container, false);

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        sessionManagement = new SessionManagement(context);
        userProfile = rootView.findViewById(R.id.userProfile);
        recyclerview_companyTeamMembers = rootView.findViewById(R.id.recyclerview_companyTeamMembers);
        edSearch = rootView.findViewById(R.id.edSearch);
        layout_noApplicants = rootView.findViewById(R.id.layout_noApplicants);


        Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), userProfile);

        requestForCompanyTeamMembersList("Bearer " + sessionManagement.getToken(), "1");


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
        for (GenericApplicantDataModel item : companyMembersList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.size() == 0){
            recyclerview_companyTeamMembers.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerview_companyTeamMembers.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            companyTeamMembersAdapter.filterList(filteredList);
        }

    }

    public void requestForCompanyTeamMembersList(String authToken, String account_status){
        Call<GetAdminApplicantsResponseModel> call = service.getCompanyMembersList(authToken, account_status);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Response<GetAdminApplicantsResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        recyclerview_companyTeamMembers.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);

                        companyMembersList = new ArrayList<>();
                        companyMembersList = response.body().getApplicantsList();

                        // Remove logged in uit team member account

                        for (int i = 0; i < companyMembersList.size(); i++) {
                            GenericApplicantDataModel memberDataModel = companyMembersList.get(i);
                            if (memberDataModel !=null){
                                if (sessionManagement.getKeyId().equals(String.valueOf(memberDataModel.getUser_id()))){
                                    companyMembersList.remove(i);
                                    break;
                                }
                            }
                        }


                        showCompanyTeamMembersRecyclerView(recyclerview_companyTeamMembers, companyMembersList);
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

    private void showCompanyTeamMembersRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> uitMembersList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyTeamMembersAdapter = new CompanyTeamMembersAdapter(context, uitMembersList,"accepted", this);
        recyclerView.setAdapter(companyTeamMembersAdapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, CompanyTeamMemberProfileActivity.class);
        intent.putExtra("applicantDataModel", companyMembersList.get(position));
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onMessageClicked(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+companyMembersList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(companyMembersList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", companyMembersList.get(position).getProfile_image());
        intent.putExtra("second_user_name", companyMembersList.get(position).getName());
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onAcceptClicked(int position) {

    }

    @Override
    public void onDeclineClicked(int position) {

    }
}