package com.antizon.uit_android.company.fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.adapters.company.CompanyProfileIndustriesAdapter;
import com.antizon.uit_android.applicant.welcome.ActivityContactUs;
import com.antizon.uit_android.company.activities.CompanyProfileDeiInfoActivity;
import com.antizon.uit_android.company.activities.CompanyProfileDemoGraphicInfoActivity;
import com.antizon.uit_android.company.welcome.CompanyEmailAddressActivity;
import com.antizon.uit_android.company.welcome.CompanyInSizeActivity;
import com.antizon.uit_android.company.welcome.CompanyWebsiteActivity;
import com.antizon.uit_android.company.welcome.SelectCompanyStageActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.FileUtils;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfileFragment extends Fragment {
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout btnEditProfile, layout_feedBack, layout_contactUit, layout_term, layout_privacy, layout_faqs, layout_logout, btnDeiInformation, btnDemoGraphicInformation;
    RoundedImageView userProfileImage;
    TextView text_profileName, text_userEmail, text_userLocation, text_companyBioDescription, text_companySizeName, text_editCompanySize,
            text_companyStageName, text_editCompanyStage, text_editEmail, text_companyWebsite, text_visitSite, text_editCompanyWeb, text_greenHouseConnect, text_viewMap;
    RecyclerView recyclerview_industries;

    View rootView;

    String picturePath = "";
    CompanyProfileDataModel profileDataModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        userProfileImage = rootView.findViewById(R.id.userProfileImage);
        text_profileName = rootView.findViewById(R.id.text_profileName);
        text_userEmail = rootView.findViewById(R.id.text_userEmail);
        text_userLocation = rootView.findViewById(R.id.text_userLocation);
        text_companyBioDescription = rootView.findViewById(R.id.text_companyBioDescription);
        text_companySizeName = rootView.findViewById(R.id.text_companySizeName);
        text_editCompanySize = rootView.findViewById(R.id.text_editCompanySize);
        text_companyWebsite = rootView.findViewById(R.id.text_companyWebsite);
        text_visitSite = rootView.findViewById(R.id.text_visitSite);
        text_editCompanyWeb = rootView.findViewById(R.id.text_editCompanyWeb);
        text_greenHouseConnect = rootView.findViewById(R.id.text_greenHouseConnect);
        text_companyStageName = rootView.findViewById(R.id.text_companyStageName);
        text_editCompanyStage = rootView.findViewById(R.id.text_editCompanyStage);
        recyclerview_industries = rootView.findViewById(R.id.recyclerview_industries);

        text_editEmail = rootView.findViewById(R.id.text_editEmail);
        btnDeiInformation = rootView.findViewById(R.id.btnDeiInformation);
        btnDemoGraphicInformation = rootView.findViewById(R.id.btnDemoGraphicInformation);

        layout_feedBack = rootView.findViewById(R.id.layout_feedBack);
        layout_contactUit = rootView.findViewById(R.id.layout_contactUit);
        layout_term = rootView.findViewById(R.id.layout_term);
        layout_privacy = rootView.findViewById(R.id.layout_privacy);
        layout_faqs = rootView.findViewById(R.id.layout_faqs);
        layout_logout = rootView.findViewById(R.id.layout_logout);
        text_viewMap = rootView.findViewById(R.id.text_viewMap);


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

        layout_term.setOnClickListener(v -> goToWebViewActivity("Terms and Condition", "https://www.usintechnology.com/platform-terms"));
        layout_privacy.setOnClickListener(v -> goToWebViewActivity("Privacy Policy", "https://www.usintechnology.com/privacy-policy"));
        layout_faqs.setOnClickListener(v -> goToWebViewActivity("FAQs", "https://usintechnology.froged.help/docs"));
        layout_feedBack.setOnClickListener(v -> goToWebViewActivity("Feedback", "https://www.usintechnology.com"));

        btnEditProfile.setOnClickListener(v -> checkGalleryPermission());

    }

    ActivityResultLauncher<Intent> onContactUsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            CustomCookieToast.showSuccessToast(requireActivity(),"Feedback sent");
        }
    });

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

                    requestForCompanyProfileUpdate("Bearer " + sessionManagement.getToken(), picturePath);
                }

            }else {
                Toast.makeText(context, "No Image Selected", Toast.LENGTH_LONG).show();
            }


        }

    });

    private void requestForCompanyProfileUpdate(String authToken,String profileImage) {
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

    public void requestForCompanyProfile(String authToken, String userId) {
        Call<CompanyProfileResponseModel> call = service.getCompanyProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Response<CompanyProfileResponseModel> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        profileDataModel = response.body().getDataModel();
                        if (profileDataModel!=null){
                            sessionManagement.setProfileImage(profileDataModel.getProfile_image());
                            Utilities.loadImage(context, profileDataModel.getProfile_image(), userProfileImage);
                            text_profileName.setText(profileDataModel.getName());
                            text_userEmail.setText(profileDataModel.getEmail());

                            userProfileImage.setOnClickListener(v -> {
                                Intent intent = new Intent(context, ImageViewerActivity.class);
                                ViewCompat.setTransitionName(userProfileImage, profileDataModel.getProfile_image());
                                String animationName = ViewCompat.getTransitionName(userProfileImage);
                                intent.putExtra("animationName", animationName);
                                intent.putExtra("IMAGE",profileDataModel.getProfile_image());
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), userProfileImage, Objects.requireNonNull(animationName));
                                startActivity(intent, options.toBundle());
                            });

                            text_editEmail.setOnClickListener(v -> {
                                Intent intent = new Intent(requireActivity(), CompanyEmailAddressActivity.class);
                                intent.putExtra("from", "edit");
                                intent.putExtra("companyEmail", profileDataModel.getEmail());
                                onCompanyEmailUpdateLauncher.launch(intent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                            });

                            text_companyWebsite.setText(profileDataModel.getWebsite());
                            text_companyWebsite.setPaintFlags(text_companyWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                            text_visitSite.setOnClickListener(v -> {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("pageTitle", profileDataModel.getName() + "'s Site");
                                intent.putExtra("pageUrl", profileDataModel.getWebsite());
                                startActivity(intent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });

                            text_editCompanyWeb.setOnClickListener(v -> {
                                Intent intent = new Intent(requireActivity(), CompanyWebsiteActivity.class);
                                intent.putExtra("from", "edit");
                                intent.putExtra("companyWebsite", profileDataModel.getWebsite());
                                onCompanyWebsiteUpdateLauncher.launch(intent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                            });

                            text_companyBioDescription.setText(profileDataModel.getBio());

                            String companyLocation =  profileDataModel.getCity() + ", " + profileDataModel.getState();
                            text_userLocation.setText(companyLocation);

                            text_viewMap.setOnClickListener(v -> Utilities.startNavigationIntent(requireActivity(), companyLocation));

                            if (profileDataModel.getUserStagesList() !=null){
                                if (!profileDataModel.getUserStagesList().isEmpty()){
                                    text_companyStageName.setText(profileDataModel.getUserStagesList().get(0).getName());
                                }else {
                                    text_companyStageName.setText(R.string.notSelected);
                                }

                                text_editCompanyStage.setOnClickListener(v -> {
                                    Intent intent = new Intent(requireActivity(), SelectCompanyStageActivity.class);
                                    intent.putExtra("from", "edit");
                                    intent.putExtra("companyInStageModel", profileDataModel.getUserStagesList().get(0));
                                    onCompanyStagesUpdateLauncher.launch(intent);
                                    requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                });
                            }
                            if (profileDataModel.getUserSizesList() !=null){
                                if (!profileDataModel.getUserSizesList().isEmpty()){
                                    text_companySizeName.setText(profileDataModel.getUserSizesList().get(0).getName());
                                }else {
                                    text_companySizeName.setText(R.string.notSelected);
                                }
                                text_editCompanySize.setOnClickListener(v -> {
                                    Intent intent = new Intent(requireActivity(), CompanyInSizeActivity.class);
                                    intent.putExtra("from", "edit");
                                    intent.putExtra("companyInSize", profileDataModel.getUserSizesList().get(0));
                                    onCompanySizeUpdateLauncher.launch(intent);
                                    requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                });
                            }

                            if (profileDataModel.getGreenhouse_access_token() !=null){
                                text_greenHouseConnect.setText(R.string.connected);
                            }else {
                                text_greenHouseConnect.setText(R.string.connect);
                            }

                            if (profileDataModel.getIndustries() !=null){
                                showIndustriesRecyclerView(recyclerview_industries, profileDataModel.getIndustries());
                            }

                            btnDemoGraphicInformation.setOnClickListener(v -> {
                                Intent demoGraphIntent = new Intent(context, CompanyProfileDemoGraphicInfoActivity.class);
                                demoGraphIntent.putExtra("companyProfileDataModel", profileDataModel);
                                demoGraphIntent.putExtra("visitProfileId", sessionManagement.getKeyId());
                                demoGraphIntent.putExtra("accountStatus", 1);
                                onCompanyInfoUpdateLauncher.launch(demoGraphIntent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });

                            btnDeiInformation.setOnClickListener(v -> {
                                Intent demoGraphIntent = new Intent(context, CompanyProfileDeiInfoActivity.class);
                                demoGraphIntent.putExtra("companyProfileDataModel", profileDataModel);
                                demoGraphIntent.putExtra("visitProfileId", sessionManagement.getKeyId());
                                demoGraphIntent.putExtra("accountStatus", 1);
                                onCompanyInfoUpdateLauncher.launch(demoGraphIntent);
                                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
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
            public void onFailure(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), "Failure!", t.getMessage());
            }
        });
    }

    ActivityResultLauncher<Intent> onCompanyEmailUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyEmail = resultIntent.getStringExtra("companyEmail");
                text_userEmail.setText(companyEmail);
                profileDataModel.setEmail(companyEmail);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyWebsiteUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyWebsite = resultIntent.getStringExtra("companyWebsite");
                text_companyWebsite.setText(companyWebsite);
                profileDataModel.setWebsite(companyWebsite);
            }

        }
    });
    
    ActivityResultLauncher<Intent> onCompanyStagesUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                NameIdModel companyInStageModel = resultIntent.getParcelableExtra("companyInStageModel");
                profileDataModel.getUserStagesList().get(0).setName(companyInStageModel.getName());
                profileDataModel.getUserStagesList().get(0).setId(companyInStageModel.getId());
                text_companyStageName.setText(companyInStageModel.getName());
            }

        }
    });
    
    ActivityResultLauncher<Intent> onCompanySizeUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                NameIdModel companyInSize = resultIntent.getParcelableExtra("companyInSize");
                profileDataModel.getUserSizesList().get(0).setName(companyInSize.getName());
                profileDataModel.getUserSizesList().get(0).setId(companyInSize.getId());
                text_companySizeName.setText(companyInSize.getName());
            }

        }
    });


    ActivityResultLauncher<Intent> onCompanyInfoUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                profileDataModel = resultIntent.getParcelableExtra("companyProfileDataModel");
            }

        }
    });

    private void showIndustriesRecyclerView(RecyclerView recyclerView, List<NameIdModel> industriesList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        CompanyProfileIndustriesAdapter profileIndustriesAdapter = new CompanyProfileIndustriesAdapter(context, industriesList);
        recyclerView.setAdapter(profileIndustriesAdapter);
    }

}