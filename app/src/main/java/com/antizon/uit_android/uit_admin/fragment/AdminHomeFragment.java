package com.antizon.uit_android.uit_admin.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.antizon.uit_android.R;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.antizon.uit_android.generic.activities.CommunityVideoOpen;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.home.AdminHomeDataModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.uit_admin.welcome.AdminApplicants;
import com.antizon.uit_android.uit_admin.welcome.AdminMembersActivity;
import com.antizon.uit_android.uit_admin.welcome.ListedJobsActivity;
import com.antizon.uit_android.uit_admin.welcome.PendingCompaniesActivity;
import com.antizon.uit_android.uit_admin.welcome.UitMembers;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.google.android.material.card.MaterialCardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminHomeFragment extends Fragment {
    GetDataService service;

    Context context;
    private static final String TAG = AdminHomeFragment.class.getSimpleName();
    MaterialCardView companiesCardView, cardViewCompanies, cardViewMember, cardViewJob, cardViewJobMember, cardViewCommunity;
    SessionManagement sessionManagement;

    TextView companiesCount, membersCount, applicantsCount, jobsCount, communityCount, uit_membersCount;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        setIds(view);
        initialize();
        setListener();

        getAdminHomeData("Bearer " + sessionManagement.getToken());
        return view;
    }


    void setIds(View view) {
        Log.d(TAG, "setIds: ");

        companiesCount = view.findViewById(R.id.companiesCount);
        membersCount = view.findViewById(R.id.membersCount);
        applicantsCount = view.findViewById(R.id.applicantsCount);
        jobsCount = view.findViewById(R.id.jobsCount);
        communityCount = view.findViewById(R.id.communityCount);
        uit_membersCount = view.findViewById(R.id.uitMembersCount);
        cardViewCompanies = view.findViewById(R.id.cardViewCompanies);
        companiesCardView = view.findViewById(R.id.companiesCardView);
        cardViewMember = view.findViewById(R.id.cardViewMember);
        cardViewJob = view.findViewById(R.id.cardViewJob);
        cardViewJobMember = view.findViewById(R.id.cardViewJob2);
        cardViewCommunity = view.findViewById(R.id.cardViewCommunity);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        sessionManagement = new SessionManagement(getContext());

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        Log.d(TAG, "setListener: ");

        companiesCardView.setOnClickListener(v -> moveToNextScreen(PendingCompaniesActivity.class));


        cardViewCompanies.setOnClickListener(v -> moveToNextScreen(AdminApplicants.class));

        cardViewMember.setOnClickListener(v -> moveToNextScreen(AdminMembersActivity.class));

        cardViewJob.setOnClickListener(v -> moveToNextScreen(ListedJobsActivity.class));

        cardViewCommunity.setOnClickListener(v -> moveToNextScreen(CommunityVideoOpen.class));

        cardViewJobMember.setOnClickListener(v -> moveToNextScreen(UitMembers.class));
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
                        String communities = homeDataModel.getCommunity() + " ";
                        String uit_members = homeDataModel.getUit_members() + " ";

                        companiesCount.setText(companies);
                        membersCount.setText(members);
                        applicantsCount.setText(applicants);
                        jobsCount.setText(jobs);
                        communityCount.setText(communities);
                        uit_membersCount.setText(uit_members);


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

    private void moveToNextScreen(Class<?> className){
        Intent intent = new Intent(context, className);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}
