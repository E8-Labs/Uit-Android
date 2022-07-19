package com.antizon.uit_android.company.fragment;

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
import com.antizon.uit_android.applicant.activities.ActivityDEIInfo;
import com.antizon.uit_android.applicant.activities.ActivityDemoInfo;
import com.antizon.uit_android.applicant.welcome.ActivityContactUs;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ProfileFragment extends Fragment {
    ImageView arrow, arrowTwo, changeProfile;
    TextView editEmail, address, logout,email, roleText, text_userName, terms, privacy, contact;
    ImageView profilePicture;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    String role,emailValue, userName;
    ModelUitAdminApproved dataModel;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        context = requireActivity();
        initViews(rootView);

        return rootView;
    }

    void initViews(View rootView) {
        arrow = rootView.findViewById(R.id.arrow);
        arrowTwo = rootView.findViewById(R.id.arrowTwo);
        editEmail = rootView.findViewById(R.id.editEmail);
        changeProfile = rootView.findViewById(R.id.changeProfile);
        address = rootView.findViewById(R.id.address);
        logout = rootView.findViewById(R.id.logout);
        profilePicture = rootView.findViewById(R.id.profilePicture);
        email = rootView.findViewById(R.id.email);
        roleText = rootView.findViewById(R.id.roleText);
        text_userName = rootView.findViewById(R.id.text_userName);
        terms = rootView.findViewById(R.id.terms);
        privacy = rootView.findViewById(R.id.privacy);
        contact = rootView.findViewById(R.id.contact);

        progressDialog = new ProgressDialog(getContext());
        sessionManagement = new SessionManagement(getContext());

        Glide.with(context).load(sessionManagement.getProfileImage()).apply(new RequestOptions().circleCrop()).into(profilePicture);

        role = sessionManagement.getRole();
        emailValue=sessionManagement.getKeyEmail();
        userName=sessionManagement.getUserName();
        email.setText(emailValue);

        text_userName.setText(userName);

        dataModel = new ModelUitAdminApproved();

        arrow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityDemoInfo.class);
            intent.putExtra("dataModel", dataModel);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        arrowTwo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityDEIInfo.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        logout.setOnClickListener(view -> {
            if (sessionManagement.isLoggedIn()) {
                doYouWantToLogout();
            }
        });

        contact.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityContactUs.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        terms.setOnClickListener(v -> goToWebViewActivity("Terms"));
        privacy.setOnClickListener(v -> goToWebViewActivity("Privacy Policy"));
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
            reportPostPopup.dismiss();
            requireActivity().finishAffinity();
            Intent intent = new Intent(context, SignInActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            sessionManagement.logoutUser();
        });
    }

}