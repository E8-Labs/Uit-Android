package com.antizon.uit_android.recruiter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.applicant.welcome.ActivityContactUs;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.recruiter.welcome.RecruiterEmailAddressActivity;
import com.antizon.uit_android.recruiter.welcome.RecruiterPhoneNumberActivity;
import com.antizon.uit_android.recruiter.welcome.RecruiterSelectJobActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;


public class CompanyTeamMemberProfileFragment extends Fragment {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout layout_myJobs, layout_feedBack, layout_contactUit, layout_term, layout_privacy, layout_faqs, layout_logout;
    TextView text_profileName, text_editJobTitle, text_jobTitle, text_userEmail, text_userPhoneNumber, text_editContactInfo;
    RoundedImageView userProfileImage;
    View rootView;
    BottomSheetDialog bottomSheetDialog;
    CompanyTeamMemberProfileFragmentCallBacks callBack;

    public CompanyTeamMemberProfileFragment(){
    }

    public CompanyTeamMemberProfileFragment(CompanyTeamMemberProfileFragmentCallBacks callBack){
        this.callBack = callBack;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_company_team_member_profile, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        initViews(rootView);
        return rootView;
    }


    private void initViews(View rootView){

        text_profileName = rootView.findViewById(R.id.text_profileName);
        text_jobTitle = rootView.findViewById(R.id.text_jobTitle);
        text_editJobTitle = rootView.findViewById(R.id.text_editJobTitle);
        text_userEmail = rootView.findViewById(R.id.text_userEmail);
        text_userPhoneNumber = rootView.findViewById(R.id.text_userPhoneNumber);
        userProfileImage = rootView.findViewById(R.id.userProfileImage);
        text_editContactInfo = rootView.findViewById(R.id.text_editContactInfo);

        layout_myJobs = rootView.findViewById(R.id.layout_myJobs);
        layout_feedBack = rootView.findViewById(R.id.layout_feedBack);
        layout_contactUit = rootView.findViewById(R.id.layout_contactUit);
        layout_term = rootView.findViewById(R.id.layout_term);
        layout_privacy = rootView.findViewById(R.id.layout_privacy);
        layout_faqs = rootView.findViewById(R.id.layout_faqs);
        layout_logout = rootView.findViewById(R.id.layout_logout);


        Utilities.loadImage(context, sessionManagement.getProfileImage(), userProfileImage);
        text_profileName.setText(sessionManagement.getUserName());
        text_jobTitle.setText(sessionManagement.getKeyJobTitle());
        text_userEmail.setText(sessionManagement.getKeyEmail());
        text_userPhoneNumber.setText(sessionManagement.getKeyPhoneNumber());

        userProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(userProfileImage, sessionManagement.getProfileImage());
            String animationName = ViewCompat.getTransitionName(userProfileImage);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE",sessionManagement.getProfileImage());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), userProfileImage, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });


        layout_myJobs.setOnClickListener(v -> callBack.onMyJobsClicked());

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
        layout_faqs.setOnClickListener(v -> goToWebViewActivity("FAQs", "https://usintechnology.froged.help/docs"));
        layout_feedBack.setOnClickListener(v -> goToWebViewActivity("Feedback", "https://www.termsfeed.com/legal/disclaimer"));


        text_editJobTitle.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecruiterSelectJobActivity.class);
            intent.putExtra("from", "edit");
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        text_editContactInfo.setOnClickListener(v -> showEditContactInfoBottomSheet());
    }


    ActivityResultLauncher<Intent> onContactUsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            CustomCookieToast.showSuccessToast(requireActivity(),"Feedback sent");
        }
    });

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
            onCompanyTeamMemberJobTitleUpdateLauncher.launch(intent);
            requireActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

        });
    }

    public interface CompanyTeamMemberProfileFragmentCallBacks{
        void onMyJobsClicked();
    }

    private void showEditContactInfoBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_two_options, rootView.findViewById(R.id.bottom_sheet_option));

        LinearLayout btn_cancel = sheetView.findViewById(R.id.btn_cancel);

        LinearLayout btn_selectImage = sheetView.findViewById(R.id.btn_selectImage);
        TextView text_selectImage = sheetView.findViewById(R.id.text_selectImage);
        LinearLayout btn_selectVideo = sheetView.findViewById(R.id.btn_selectVideo);
        TextView text_selectVideo = sheetView.findViewById(R.id.text_selectVideo);

        btn_cancel.setOnClickListener(view1 -> bottomSheetDialog.dismiss());

        text_selectImage.setText(R.string.editEmail);
        text_selectVideo.setText(R.string.editPhone);

        btn_selectImage.setOnClickListener(v1 -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(requireActivity(), RecruiterEmailAddressActivity.class);
            intent.putExtra("from", "edit");
            intent.putExtra("companyTeamMemberEmail", sessionManagement.getKeyEmail());
            onCompanyTeamMemberEmailUpdateLauncher.launch(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
        });

        btn_selectVideo.setOnClickListener(v12 -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(requireActivity(), RecruiterPhoneNumberActivity.class);
            intent.putExtra("from", "edit");
            intent.putExtra("companyTeamMemberPhone", sessionManagement.getKeyPhoneNumber());
            onCompanyTeamMemberPhoneNumberUpdateLauncher.launch(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
        });

        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    ActivityResultLauncher<Intent> onCompanyTeamMemberEmailUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyTeamMemberEmail = resultIntent.getStringExtra("companyTeamMemberEmail");
                text_userEmail.setText(companyTeamMemberEmail);
                sessionManagement.setKeyEmail(companyTeamMemberEmail);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyTeamMemberPhoneNumberUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyTeamMemberPhone = resultIntent.getStringExtra("companyTeamMemberPhone");
                text_userPhoneNumber.setText(companyTeamMemberPhone);
                sessionManagement.setKeyPhoneNumber(companyTeamMemberPhone);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyTeamMemberJobTitleUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyTeamMemberJobTitle = resultIntent.getStringExtra("companyTeamMemberJobTitle");
                companyTeamMemberJobTitle = companyTeamMemberJobTitle + " at " + sessionManagement.getKeyParentProfileName();
                text_jobTitle.setText(companyTeamMemberJobTitle);
                sessionManagement.setKeyJobTitle(companyTeamMemberJobTitle);
            }

        }
    });
}