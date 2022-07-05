package com.antizon.uit_android.uit_members.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.home.AdminHomeDataModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.uit_admin.welcome.ListedJobsActivity;
import com.antizon.uit_android.uit_members.welcome.AllCompaniesActivity;
import com.antizon.uit_android.uit_members.welcome.ActivityApplicants;
import com.antizon.uit_android.uit_members.welcome.ActivityMembers;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MemberHomeFragment extends Fragment {
    Context context;
    GetDataService service;

    MaterialCardView cardViewMember, cardViewCompanies,companiesCardView,cardViewJob;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView userProfile;
    TextView text_companiesCount, text_membersCount, text_applicantsCount, text_jobsCount;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_home, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        initViews(view);
        initialize();
        setListener();

        getAdminHomeData("Bearer " + sessionManagement.getToken());
        return view;
    }

    void initViews(View rootView) {
        cardViewCompanies = rootView.findViewById(R.id.cardViewCompanies);
        companiesCardView = rootView.findViewById(R.id.companiesCardView);
        cardViewMember = rootView.findViewById(R.id.cardViewMember);
        cardViewJob = rootView.findViewById(R.id.cardViewJob);
        userProfile = rootView.findViewById(R.id.userProfile);
        text_companiesCount = rootView.findViewById(R.id.text_companiesCount);
        text_membersCount = rootView.findViewById(R.id.text_membersCount);
        text_applicantsCount = rootView.findViewById(R.id.text_applicantsCount);
        text_jobsCount = rootView.findViewById(R.id.text_jobsCount);

    }

    void initialize() {
        progressDialog = new ProgressDialog(getContext());
        sessionManagement = new SessionManagement(getContext());

        Utilities.loadImage(context, sessionManagement.getProfileImage(), userProfile);
    }

    private void getAdminHomeData(String token){
        Call<AdminHomeResponseModel> call = service.getAdminHome(token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AdminHomeResponseModel> call, @NonNull Response<AdminHomeResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        AdminHomeDataModel homeDataModel = response.body().getAdminHomeDataModel();

                        String companies = homeDataModel.getCompanies() + " ";
                        String members = homeDataModel.getRecruiters() + " ";
                        String applicants = homeDataModel.getApplicants() + " ";
                        String jobs = homeDataModel.getJobs() + " ";
                        text_companiesCount.setText(companies);
                        text_membersCount.setText(members);
                        text_applicantsCount.setText(applicants);
                        text_jobsCount.setText(jobs);


                    } else {
                        CustomCookieToast.showFailureToast( requireActivity(), "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast( requireActivity(), "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdminHomeResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast( requireActivity(), "Failure!",  t.getMessage());
            }
        });
    }

    void setListener() {

        companiesCardView.setOnClickListener(v -> moveToNextScreen(AllCompaniesActivity.class));

        cardViewCompanies.setOnClickListener(v -> moveToNextScreen(ActivityApplicants.class));

        cardViewMember.setOnClickListener(v -> moveToNextScreen(ActivityMembers.class));

        cardViewJob.setOnClickListener(v -> moveToNextScreen(ListedJobsActivity.class));

    }

    private void moveToNextScreen(Class<?> className){
        Intent intent = new Intent(context, className);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

}