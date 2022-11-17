package com.antizon.uit_android.applicant.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.PdfViewerActivity;
import com.antizon.uit_android.applicant.activities.ApplicantDemographicActivity;
import com.antizon.uit_android.applicant.activities.ApplicantProfessionalActivity;
import com.antizon.uit_android.applicant.welcome.ActivityContactUs;
import com.antizon.uit_android.applicant.welcome.ApplicantAddCoverLetterActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantBioActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantEmailAddressActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantPhoneNumberActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantResumeActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.ApplicantProfileResponseModel;
import com.antizon.uit_android.models.profile.ProfileResponseDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.FileUtils;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Map;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantProfileFragment extends Fragment {
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout layout_feedBack, layout_contactUit, layout_term, layout_privacy, layout_faqs, layout_logout, layout_professionalInformation, layout_demographicInformation, btnEditProfile;
    RoundedImageView userProfileImage;
    TextView text_profileName, text_jobTitle, text_aboutMeDescription, text_editBio, text_userEmail, text_editEmail, text_userPhoneNumber, text_editPhoneNumber, text_resumePdf,
            text_editResume, text_coverLetterStatus, text_AddCoverLetter;
    String picturePath = "";
    View rootView;

    ProfileResponseDataModel applicantProfileDataModel;

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

        userProfileImage = rootView.findViewById(R.id.userProfileImage);
        text_profileName = rootView.findViewById(R.id.text_profileName);
        text_jobTitle = rootView.findViewById(R.id.text_jobTitle);
        text_aboutMeDescription = rootView.findViewById(R.id.text_aboutMeDescription);
        text_editBio = rootView.findViewById(R.id.text_editBio);
        text_userEmail = rootView.findViewById(R.id.text_userEmail);
        text_editEmail = rootView.findViewById(R.id.text_editEmail);
        text_userPhoneNumber = rootView.findViewById(R.id.text_userPhoneNumber);
        text_editPhoneNumber = rootView.findViewById(R.id.text_editPhoneNumber);
        text_resumePdf = rootView.findViewById(R.id.text_resumePdf);
        text_editResume = rootView.findViewById(R.id.text_editResume);
        text_coverLetterStatus = rootView.findViewById(R.id.text_coverLetterStatus);
        text_AddCoverLetter = rootView.findViewById(R.id.text_AddCoverLetter);
        layout_professionalInformation = rootView.findViewById(R.id.layout_professionalInformation);
        layout_demographicInformation = rootView.findViewById(R.id.layout_demographicInformation);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);

        layout_feedBack = rootView.findViewById(R.id.layout_feedBack);
        layout_contactUit = rootView.findViewById(R.id.layout_contactUit);
        layout_term = rootView.findViewById(R.id.layout_term);
        layout_privacy = rootView.findViewById(R.id.layout_privacy);
        layout_faqs = rootView.findViewById(R.id.layout_faqs);
        layout_logout = rootView.findViewById(R.id.layout_logout);


        layout_logout.setOnClickListener(view -> {
            if (sessionManagement.isLoggedIn()) {
                doYouWantToLogout();
            }
        });

        layout_contactUit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityContactUs.class);
            onContactUsLauncher.launch(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        layout_term.setOnClickListener(v -> goToWebViewActivity("Terms", "https://www.usintechnology.com/platform-terms"));
        layout_privacy.setOnClickListener(v -> goToWebViewActivity("Privacy Policy", "https://www.usintechnology.com/privacy-policy"));
        layout_feedBack.setOnClickListener(v -> goToWebViewActivity("Feedback", "https://www.termsfeed.com/legal/disclaimer"));
        layout_faqs.setOnClickListener(v -> goToWebViewActivity("Faqs", "https://usintechnology.froged.help/docs"));

        btnEditProfile.setOnClickListener(v -> checkGalleryPermission());
    }

    private void goToWebViewActivity(String title, String pageUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("pageTitle", title);
        intent.putExtra("pageUrl", pageUrl);
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

    private void checkGalleryPermission() {
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(context, "User have No Permission", Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(permissions);
        } else {
            // Toast.makeText(context, "User already have Permission, Opening Gallery...", Toast.LENGTH_SHORT).show();
            //permission already granted
            pickImageFromGallery();
        }
    }

    ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            boolean permissionGranted = false;
            for (int i = 0; i < result.size(); i++) {
                if (Boolean.TRUE.equals(result.get(permissions[i]))) {
                    permissionGranted = true;
                } else {
                    permissionGranted = false;
                    break;
                }
            }

            if (permissionGranted) {
                pickImageFromGallery();
            } else {
                Toast.makeText(context, "Permissions are denied!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        onSelectMediaMethodLauncher.launch(intent);

    }

    ActivityResultLauncher<Intent> onSelectMediaMethodLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                picturePath = FileUtils.getPath(context, data.getData());
                if (!TextUtils.isEmpty(picturePath)) {
                    Utilities.loadCircleImage(context, picturePath, userProfileImage);

                    requestForApplicantProfileUpdate("Bearer " + sessionManagement.getToken(), picturePath);
                }

            }else {
                Toast.makeText(context, "No Image Selected", Toast.LENGTH_LONG).show();
            }


        }

    });

    @Override
    public void onResume() {
        super.onResume();
        String profileUpdated = sessionManagement.getKeyProfileUpdated();
        if (profileUpdated.equals("true")){
            requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
        }
    }

    public void requestForUserProfile(String authToken, String userId) {

        Call<ApplicantProfileResponseModel> call = service.getApplicantProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Response<ApplicantProfileResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        applicantProfileDataModel = response.body().getData();
                        if (applicantProfileDataModel !=null){
                            sessionManagement.setKeyProfileUpdated("false");

                            Utilities.loadImage(context, applicantProfileDataModel.getProfile_image(), userProfileImage);

                            userProfileImage.setOnClickListener(v -> {
                                Intent intent = new Intent(context, ImageViewerActivity.class);
                                ViewCompat.setTransitionName(userProfileImage, applicantProfileDataModel.getProfile_image());
                                String animationName = ViewCompat.getTransitionName(userProfileImage);
                                intent.putExtra("animationName", animationName);
                                intent.putExtra("IMAGE",applicantProfileDataModel.getProfile_image());
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), userProfileImage, Objects.requireNonNull(animationName));
                                startActivity(intent, options.toBundle());
                            });

                            sessionManagement.setProfileImage(applicantProfileDataModel.getProfile_image());
                            sessionManagement.setApplicationStatus(applicantProfileDataModel.getApplication_status() +"");

                            if (applicantProfileDataModel.getName() !=null){
                                text_profileName.setText(applicantProfileDataModel.getName());
                            }
                            if (applicantProfileDataModel.getJob_title() !=null){
                                text_jobTitle.setText(applicantProfileDataModel.getJob_title());
                            }

                            if (applicantProfileDataModel.getBio() !=null){
                                text_aboutMeDescription.setText(applicantProfileDataModel.getBio());
                            }

                            text_editBio.setOnClickListener(v -> {
                                Intent intent = new Intent(requireActivity(), ApplicantBioActivity.class);
                                intent.putExtra("applicantBio", applicantProfileDataModel.getBio());
                                onApplicantBioLauncher.launch(intent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                            });

                            if (applicantProfileDataModel.getEmail() !=null){
                                text_userEmail.setText(applicantProfileDataModel.getEmail());
                            }

                            text_editEmail.setOnClickListener(v -> {
                                Intent intent = new Intent(requireActivity(), ApplicantEmailAddressActivity.class);
                                intent.putExtra("from", "edit");
                                intent.putExtra("applicantEmail", applicantProfileDataModel.getEmail());
                                onApplicantEmailLauncher.launch(intent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                            });



                            if (applicantProfileDataModel.getPhone() !=null){
                                text_userPhoneNumber.setText(applicantProfileDataModel.getPhone());
                            }

                            text_editPhoneNumber.setOnClickListener(v -> {
                                Intent intent = new Intent(requireActivity(), ApplicantPhoneNumberActivity.class);
                                intent.putExtra("from", "edit");
                                intent.putExtra("applicantPhone", applicantProfileDataModel.getPhone());
                                onApplicantPhoneNumberLauncher.launch(intent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                            });

                            if (applicantProfileDataModel.getProfessionalInfoDataModel() !=null){
                                if (applicantProfileDataModel.getProfessionalInfoDataModel().getResume() !=null){
                                    text_resumePdf.setText(R.string.text_resume);
                                    sessionManagement.setResumeSaved("true");
                                    sessionManagement.setKeyResumeLink(applicantProfileDataModel.getProfessionalInfoDataModel().getResume());

                                    text_resumePdf.setPaintFlags(text_resumePdf.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    text_editResume.setText(R.string.edit);
                                    text_resumePdf.setTextColor(context.getColor(R.color.codGrey));

                                    text_resumePdf.setOnClickListener(v -> moveToPdfViewerActivity(applicantProfileDataModel.getProfessionalInfoDataModel().getResume(), applicantProfileDataModel.getName() +" 's Resume"));

                                    text_editResume.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireActivity(), ApplicantResumeActivity.class);
                                        intent.putExtra("type", "edit");
                                        onProfileUpdateLauncher.launch(intent);
                                        requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });

                                }else {
                                    text_resumePdf.setText(context.getString(R.string.text_notAvailable));
                                    text_editResume.setText(context.getString(R.string.add));
                                    text_resumePdf.setTextColor(context.getColor(R.color.dark_grey));

                                    text_editResume.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireActivity(), ApplicantResumeActivity.class);
                                        intent.putExtra("type", "edit");
                                        onProfileUpdateLauncher.launch(intent);
                                        requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });
                                }

                                if (applicantProfileDataModel.getProfessionalInfoDataModel().getCover_letter() !=null){
                                    text_coverLetterStatus.setText(context.getString(R.string.text_cover_letter_pdf));
                                    sessionManagement.setCoverLetterSaved("true");
                                    sessionManagement.setKeyCoverLetterLink(applicantProfileDataModel.getProfessionalInfoDataModel().getCover_letter());

                                    text_coverLetterStatus.setPaintFlags(text_coverLetterStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    text_AddCoverLetter.setText(R.string.edit);
                                    text_coverLetterStatus.setTextColor(context.getColor(R.color.codGrey));

                                    text_coverLetterStatus.setOnClickListener(v -> moveToPdfViewerActivity(applicantProfileDataModel.getProfessionalInfoDataModel().getCover_letter(), applicantProfileDataModel.getName() +" 's Cover Letter"));

                                    text_AddCoverLetter.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireActivity(), ApplicantAddCoverLetterActivity.class);
                                        intent.putExtra("type", "edit");
                                        onProfileUpdateLauncher.launch(intent);
                                        requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });
                                }else {
                                    text_coverLetterStatus.setText(context.getString(R.string.text_notAvailable));
                                    text_AddCoverLetter.setText(context.getString(R.string.add));
                                    text_coverLetterStatus.setTextColor(context.getColor(R.color.dark_grey));

                                    text_AddCoverLetter.setOnClickListener(v -> {
                                        Intent intent = new Intent(requireActivity(), ApplicantAddCoverLetterActivity.class);
                                        intent.putExtra("type", "edit");
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

                                text_editResume.setOnClickListener(v -> {
                                    Intent intent = new Intent(requireActivity(), ApplicantResumeActivity.class);
                                    intent.putExtra("type", "edit");
                                    onProfileUpdateLauncher.launch(intent);
                                    requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                });

                                text_AddCoverLetter.setOnClickListener(v -> {
                                    Intent intent = new Intent(requireActivity(), ApplicantAddCoverLetterActivity.class);
                                    intent.putExtra("type", "edit");
                                    onProfileUpdateLauncher.launch(intent);
                                    requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                });
                            }


                            layout_professionalInformation.setOnClickListener(view -> moveToNextScreen(applicantProfileDataModel, ApplicantProfessionalActivity.class));


                            layout_demographicInformation.setOnClickListener(v -> moveToNextScreen(applicantProfileDataModel, ApplicantDemographicActivity.class));

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

    private void moveToNextScreen(ProfileResponseDataModel applicantProfileDataModel, Class<?> className){
        Intent intent = new Intent(context, className);
        intent.putExtra("applicantId", sessionManagement.getKeyId());
        intent.putExtra("applicationStatus", "");
        intent.putExtra("jobId", "");
        intent.putExtra("data", applicantProfileDataModel);
        onProfessionalAndDemoGraphicInfoUpdateLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onProfessionalAndDemoGraphicInfoUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent =result.getData();
            if (resultIntent != null) {
                applicantProfileDataModel = resultIntent.getParcelableExtra("data");
            }
        }
    });

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
        }
    });

    private void requestForApplicantProfileUpdate(String authToken,String profileImage) {
        File image = new File(profileImage);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part profileImageMultiPart = MultipartBody.Part.createFormData("profile_image", image.getName(), requestBodyPostMedia);

        Call<MainResponseModel> call = service.updateApplicantProfileImage(authToken, profileImageMultiPart);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().isStatus()) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> onApplicantBioLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String applicantBio = resultIntent.getStringExtra("applicantBio");
                text_aboutMeDescription.setText(applicantBio);
                applicantProfileDataModel.setBio(applicantBio);
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantEmailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String applicantEmail = resultIntent.getStringExtra("applicantEmail");
                text_userEmail.setText(applicantEmail);
                applicantProfileDataModel.setEmail(applicantEmail);
                sessionManagement.setKeyEmail(applicantEmail);
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantPhoneNumberLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String applicantPhone = resultIntent.getStringExtra("applicantPhone");
                text_userPhoneNumber.setText(applicantPhone);
                applicantProfileDataModel.setPhone(applicantPhone);
                sessionManagement.setKeyPhoneNumber(applicantPhone);
            }

        }
    });

    private void moveToPdfViewerActivity(String pdfUrl, String headerText){
        Intent pdfViewerIntent = new Intent(context, PdfViewerActivity.class);
        pdfViewerIntent.putExtra("headerText", headerText);
        pdfViewerIntent.putExtra("pdfUrl",pdfUrl );
        startActivity(pdfViewerIntent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    ActivityResultLauncher<Intent> onContactUsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
           CustomCookieToast.showSuccessToast(requireActivity(),"Feedback sent");
        }
    });

}