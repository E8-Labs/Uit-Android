package com.antizon.uit_android.uit_admin.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.antizon.uit_android.R;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.home.AdminHomeDataModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.activities.NotificationsActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminApplicantsListActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminAllCompanyMembersActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminAllJobsActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminCompaniesListActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminUitTeamMembersActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.google.android.material.card.MaterialCardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminHomeFragment extends Fragment {
    GetDataService service;
    Context context;
    MaterialCardView companiesCardView, cardViewCompanies, cardViewMember, cardViewJob, cardViewJobMember, cardViewCommunity;
    SessionManagement sessionManagement;
    TextView text_logout, companiesCount, membersCount, applicantsCount, jobsCount, communityCount, uit_membersCount;
    ImageView dashboardNotification;
    AdminHomeFragmentCallBacks callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AdminHomeFragment(){
    }

    public AdminHomeFragment(AdminHomeFragmentCallBacks callBack){
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        setIds(view);
        setListener();

        getAdminHomeData("Bearer " + sessionManagement.getToken());

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

        return view;
    }


    private void setIds(View view) {
        sessionManagement = new SessionManagement(context);
        companiesCount = view.findViewById(R.id.companiesCount);
        dashboardNotification = view.findViewById(R.id.dashboardNotification);
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
        text_logout = view.findViewById(R.id.text_logout);

        text_logout.setOnClickListener(v -> doYouWantToLogout());
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


    private void setListener() {
        companiesCardView.setOnClickListener(v -> moveToNextScreen(AdminCompaniesListActivity.class));
        cardViewCompanies.setOnClickListener(v -> moveToNextScreen(AdminApplicantsListActivity.class));
        cardViewMember.setOnClickListener(v -> moveToNextScreen(AdminAllCompanyMembersActivity.class));
        cardViewJob.setOnClickListener(v -> moveToNextScreen(AdminAllJobsActivity.class));
        cardViewCommunity.setOnClickListener(v -> callBack.onCommunityClicked());
        cardViewJobMember.setOnClickListener(v -> moveToNextScreen(AdminUitTeamMembersActivity.class));
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

    public interface AdminHomeFragmentCallBacks{
        void onCommunityClicked();
    }
}
