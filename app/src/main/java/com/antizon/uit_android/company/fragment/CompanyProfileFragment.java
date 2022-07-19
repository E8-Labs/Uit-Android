package com.antizon.uit_android.company.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.activities.ActivityDemoInfo;
import com.antizon.uit_android.applicant.welcome.ActivityContactUs;
import com.antizon.uit_android.company.welcome.CompanySummaryActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.company.profile.CompanyDeiStatementModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfileFragment extends Fragment {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout layout_feedBack, layout_contactUit, layout_term, layout_privacy, layout_logout, btnDeiInformation, btnDemoGraphicInformation;
    RoundedImageView userProfileImage;
    TextView text_profileName, text_userEmail, text_userLocation, text_companyBioDescription, text_companySizeName, text_companyStageName,  text_companyWebsite, text_greenHouseConnect;

    View rootView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView  = inflater.inflate(R.layout.fragment_company_profile, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        initViews(rootView);

        requestForCompanyProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
        return rootView;
    }

    private void initViews(View rootView){
        userProfileImage = rootView.findViewById(R.id.userProfileImage);
        text_profileName = rootView.findViewById(R.id.text_profileName);
        text_userEmail = rootView.findViewById(R.id.text_userEmail);
        text_userLocation = rootView.findViewById(R.id.text_userLocation);
        text_companyBioDescription = rootView.findViewById(R.id.text_companyBioDescription);
        text_companySizeName = rootView.findViewById(R.id.text_companySizeName);
        text_companyWebsite = rootView.findViewById(R.id.text_companyWebsite);
        text_greenHouseConnect = rootView.findViewById(R.id.text_greenHouseConnect);
        text_companyStageName = rootView.findViewById(R.id.text_companyStageName);

        btnDeiInformation = rootView.findViewById(R.id.btnDeiInformation);
        btnDemoGraphicInformation = rootView.findViewById(R.id.btnDemoGraphicInformation);

        layout_feedBack = rootView.findViewById(R.id.layout_feedBack);
        layout_contactUit = rootView.findViewById(R.id.layout_contactUit);
        layout_term = rootView.findViewById(R.id.layout_term);
        layout_privacy = rootView.findViewById(R.id.layout_privacy);
        layout_logout = rootView.findViewById(R.id.layout_logout);


        layout_logout.setOnClickListener(view -> {
            if (sessionManagement.isLoggedIn()) {
                doYouWantToLogout();
            }
        });

        layout_contactUit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityContactUs.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        layout_term.setOnClickListener(v -> goToWebViewActivity("Terms"));
        layout_privacy.setOnClickListener(v -> goToWebViewActivity("Privacy Policy"));
        layout_feedBack.setOnClickListener(v -> goToWebViewActivity("Feedback"));



    }

    private void goToWebViewActivity(String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("pageTitle", title);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
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

    public void requestForCompanyProfile(String authToken, String userId) {

        Call<CompanyProfileResponseModel> call = service.getCompanyProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Response<CompanyProfileResponseModel> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        CompanyProfileDataModel profileDataModel = response.body().getDataModel();
                        if (profileDataModel!=null){

                            Utilities.loadImage(context, profileDataModel.getProfile_image(), userProfileImage);
                            text_profileName.setText(profileDataModel.getName());
                            text_userEmail.setText(profileDataModel.getEmail());
                            text_companyWebsite.setText(profileDataModel.getWebsite());
                            text_companyWebsite.setPaintFlags(text_companyWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                            text_companyBioDescription.setText(profileDataModel.getBio());
                            text_userLocation.setText(profileDataModel.getCity());


                            if (profileDataModel.getUserStagesList() !=null){
                                text_companyStageName.setText(profileDataModel.getUserStagesList().get(0).getName());
                            }
                            if (profileDataModel.getUserSizesList() !=null){
                                text_companySizeName.setText(profileDataModel.getUserSizesList().get(0).getName());
                            }

                            if (profileDataModel.getGreenhouse_access_token() !=null){
                                text_greenHouseConnect.setText(R.string.connected);
                            }else {
                                text_greenHouseConnect.setText(R.string.connect);
                            }
                        }
                    }else {
                        CustomCookieToast.showFailureToast(requireActivity(), "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), "Failure!", t.getMessage());
            }
        });

    }
}