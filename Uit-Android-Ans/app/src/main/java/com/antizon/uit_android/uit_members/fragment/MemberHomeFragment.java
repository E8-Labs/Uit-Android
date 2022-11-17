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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.home.AdminHomeDataModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.activities.NotificationsActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminApplicantsListActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminCompaniesListActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminAllCompanyMembersActivity;
import com.antizon.uit_android.uit_admin.welcome.AdminAllJobsActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MemberHomeFragment extends Fragment {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    MaterialCardView cardViewMember, cardViewCompanies,companiesCardView,cardViewJob;
    RoundedImageView userProfile;
    TextView text_logout, text_companiesCount, text_membersCount, text_applicantsCount, text_jobsCount;
    ImageView dashboardNotification;

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
        setListener();
        getAdminHomeData("Bearer " + sessionManagement.getToken());
        return view;
    }

    void initViews(View rootView) {
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        dashboardNotification = rootView.findViewById(R.id.dashboardNotification);
        cardViewCompanies = rootView.findViewById(R.id.cardViewCompanies);
        companiesCardView = rootView.findViewById(R.id.companiesCardView);
        cardViewMember = rootView.findViewById(R.id.cardViewMember);
        cardViewJob = rootView.findViewById(R.id.cardViewJob);
        userProfile = rootView.findViewById(R.id.userProfile);
        text_companiesCount = rootView.findViewById(R.id.companiesCount);
        text_membersCount = rootView.findViewById(R.id.membersCount);
        text_applicantsCount = rootView.findViewById(R.id.applicantsCount);
        text_jobsCount = rootView.findViewById(R.id.jobsCount);
        text_logout = rootView.findViewById(R.id.text_logout);


        Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), userProfile);

        text_logout.setOnClickListener(v -> doYouWantToLogout());

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

        companiesCardView.setOnClickListener(v -> moveToNextScreen(AdminCompaniesListActivity.class));

        cardViewCompanies.setOnClickListener(v -> moveToNextScreen(AdminApplicantsListActivity.class));

        cardViewMember.setOnClickListener(v -> moveToNextScreen(AdminAllCompanyMembersActivity.class));

        cardViewJob.setOnClickListener(v -> moveToNextScreen(AdminAllJobsActivity.class));

    }

    private void moveToNextScreen(Class<?> className){
        Intent intent = new Intent(context, className);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

}