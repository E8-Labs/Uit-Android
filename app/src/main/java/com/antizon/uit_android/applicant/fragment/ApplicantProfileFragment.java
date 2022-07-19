package com.antizon.uit_android.applicant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.antizon.uit_android.applicant.welcome.ApplicantAddCoverLetterActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantResumeActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelApplicantDepartment;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.profile.ApplicantProfileResponseModel;
import com.antizon.uit_android.models.profile.ProfileResponseDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.uit_admin.welcome.ProfessionalInformation;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantProfileFragment extends Fragment {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout layout_feedBack, layout_contactUit, layout_term, layout_privacy, layout_logout, layout_professionalInformation, layout_demographicInformation;
    RoundedImageView userProfileImage;
    TextView text_profileName, text_jobTitle, text_userEmail, text_userPhoneNumber, text_resumePdf, text_editResume, text_coverLetterStatus, text_AddCoverLetter;

    ModelAdminApplicants modelAdminApplicants;
    ModelApplicantDepartment modelApplicantDepartment;

    View rootView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_applicant_profile, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        initViews(rootView);

        requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
        return rootView;
    }

    private void initViews(View rootView){
        layout_feedBack = rootView.findViewById(R.id.layout_feedBack);
        layout_contactUit = rootView.findViewById(R.id.layout_contactUit);
        layout_term = rootView.findViewById(R.id.layout_term);
        layout_privacy = rootView.findViewById(R.id.layout_privacy);
        layout_logout = rootView.findViewById(R.id.layout_logout);
        userProfileImage = rootView.findViewById(R.id.userProfileImage);
        text_profileName = rootView.findViewById(R.id.text_profileName);
        text_jobTitle = rootView.findViewById(R.id.text_jobTitle);
        text_userEmail = rootView.findViewById(R.id.text_userEmail);
        text_userPhoneNumber = rootView.findViewById(R.id.text_userPhoneNumber);
        text_resumePdf = rootView.findViewById(R.id.text_resumePdf);
        text_editResume = rootView.findViewById(R.id.text_editResume);
        text_coverLetterStatus = rootView.findViewById(R.id.text_coverLetterStatus);
        text_AddCoverLetter = rootView.findViewById(R.id.text_AddCoverLetter);
        layout_professionalInformation = rootView.findViewById(R.id.layout_professionalInformation);
        layout_demographicInformation = rootView.findViewById(R.id.layout_demographicInformation);



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

        layout_demographicInformation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityDemoInfo.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

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

    public void requestForUserProfile(String authToken, String userId) {

        Call<ApplicantProfileResponseModel> call = service.getApplicantProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Response<ApplicantProfileResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        ProfileResponseDataModel applicantProfileDataModel = response.body().getData();
                        if (applicantProfileDataModel !=null){
                            Utilities.loadImage(context, applicantProfileDataModel.getProfile_image(), userProfileImage);
                            if (applicantProfileDataModel.getName() !=null){
                                text_profileName.setText(applicantProfileDataModel.getName());
                            }
                            if (applicantProfileDataModel.getJob_title() !=null){
                                text_jobTitle.setText(applicantProfileDataModel.getJob_title());
                            }
                            if (applicantProfileDataModel.getEmail() !=null){
                                text_userEmail.setText(applicantProfileDataModel.getEmail());
                            }
                            if (applicantProfileDataModel.getPhone() !=null){
                                text_userPhoneNumber.setText(applicantProfileDataModel.getPhone());
                            }

                            if (applicantProfileDataModel.getProfessionalInfoDataModel() !=null){
                                if (applicantProfileDataModel.getProfessionalInfoDataModel().getResume() !=null){
                                    text_resumePdf.setText(R.string.text_resume);
                                    sessionManagement.setResumeSaved("true");
                                    text_resumePdf.setPaintFlags(text_resumePdf.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    text_editResume.setText(R.string.edit);
                                    text_resumePdf.setTextColor(context.getColor(R.color.codGrey));
                                }else {
                                    text_resumePdf.setText(context.getString(R.string.text_notAvailable));
                                    text_editResume.setText(context.getString(R.string.add));
                                    text_resumePdf.setTextColor(context.getColor(R.color.dark_grey));

                                    text_editResume.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireActivity(), ApplicantResumeActivity.class);
                                        onProfileUpdateLauncher.launch(intent);
                                        requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });
                                }

                                if (applicantProfileDataModel.getProfessionalInfoDataModel().getCover_letter() !=null){
                                    text_coverLetterStatus.setText(context.getString(R.string.text_cover_letter_pdf));
                                    sessionManagement.setCoverLetterSaved("true");
                                    text_coverLetterStatus.setPaintFlags(text_coverLetterStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    text_AddCoverLetter.setText(R.string.edit);
                                    text_coverLetterStatus.setTextColor(context.getColor(R.color.codGrey));
                                }else {
                                    text_coverLetterStatus.setText(context.getString(R.string.text_notAvailable));
                                    text_AddCoverLetter.setText(context.getString(R.string.add));
                                    text_coverLetterStatus.setTextColor(context.getColor(R.color.dark_grey));

                                    text_AddCoverLetter.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireActivity(), ApplicantAddCoverLetterActivity.class);
                                        onProfileUpdateLauncher.launch(intent);
                                        requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });
                                }
                            }else {
                                text_resumePdf.setTextColor(context.getColor(R.color.dark_grey));
                                text_coverLetterStatus.setTextColor(context.getColor(R.color.dark_grey));

                                text_resumePdf.setText(context.getString(R.string.text_notAvailable));
                                text_editResume.setText(context.getString(R.string.add));

                                text_coverLetterStatus.setText(context.getString(R.string.text_notAvailable));
                                text_AddCoverLetter.setText(context.getString(R.string.add));
                            }


                            layout_professionalInformation.setOnClickListener(view -> {
                                modelAdminApplicants = new ModelAdminApplicants();
                                modelApplicantDepartment =  new ModelApplicantDepartment();

                                if (applicantProfileDataModel.getIndustriesList().size() == 0){
                                    modelApplicantDepartment.setId(1);
                                    modelApplicantDepartment.setName("No industry");

                                }else {
                                    modelApplicantDepartment.setId(applicantProfileDataModel.getIndustriesList().get(0).getId());
                                    modelApplicantDepartment.setName(applicantProfileDataModel.getIndustriesList().get(0).getName());

                                }

                                modelAdminApplicants.setCity(sessionManagement.getKeyCity());
                                modelAdminApplicants.setProfile_image(sessionManagement.getProfileImage());
                                modelAdminApplicants.setName(sessionManagement.getUserName());
                                modelAdminApplicants.setIndustryDataModel(modelApplicantDepartment);

                                Intent intent = new Intent( context, ProfessionalInformation.class);
                                intent.putExtra("dataModel", modelAdminApplicants);
                                startActivity(intent);
                            });



                        }
                    }else {

                        CustomCookieToast.showFailureToast(requireActivity(), "Failure!", response.body().getMessage());
                    }
                } else {

                    CustomCookieToast.showFailureToast(requireActivity(), "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), "Failure!", t.getMessage());
            }
        });

    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
        }
    });

}