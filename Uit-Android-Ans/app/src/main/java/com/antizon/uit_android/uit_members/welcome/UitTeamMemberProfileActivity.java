package com.antizon.uit_android.uit_members.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UitTeamMemberProfileActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RoundedImageView iv_profile;
    TextView tv_name, tv_email, tv_phone, btnDelete;
    ImageView iv_close;

    GenericApplicantDataModel applicantDataModel;
    AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_team_member_profile);
        Utilities.setCustomStatusAndNavColor(UitTeamMemberProfileActivity.this, R.color.app_color, R.color.white_dash);
        context = UitTeamMemberProfileActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");

        applicantDataModel = getIntent().getParcelableExtra("applicantDataModel");

        iv_close = findViewById(R.id.iv_close);
        iv_profile = findViewById(R.id.iv_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        btnDelete = findViewById(R.id.btnDelete);


        Utilities.loadImage(context, applicantDataModel.getProfile_image(), iv_profile);

        iv_profile.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(iv_profile, applicantDataModel.getProfile_image());
            String animationName = ViewCompat.getTransitionName(iv_profile);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE",applicantDataModel.getProfile_image());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(UitTeamMemberProfileActivity.this, iv_profile, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        if (applicantDataModel.getName() !=null){
            tv_name.setText(applicantDataModel.getName());
        }

        if (applicantDataModel.getEmail() !=null){
            tv_email.setText(applicantDataModel.getEmail());
        }
        if (applicantDataModel.getPhone() !=null){
            tv_phone.setText(applicantDataModel.getPhone());
        }

        if (sessionManagement.getRole().equals("1")){
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> showDeleteUitMemberPopup(applicantDataModel.getUser_id() + ""));
        }else {
            btnDelete.setVisibility(View.GONE);
        }

        iv_close.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteUitMemberPopup(String applicantId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UitTeamMemberProfileActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(UitTeamMemberProfileActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        headerText = "Delete Uit Team Member";
        areYouSure = "Are you sure you want to delete this\nUit Team Member?";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            progressDialog.show();
            requestForDeleteUser("Bearer " + sessionManagement.getToken(), applicantId);
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void requestForDeleteUser(String authToken, String userId) {
        Call<MainResponseModel> call = service.deleteUser(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (status){
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }else {
                            CustomCookieToast.showFailureToast(UitTeamMemberProfileActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(UitTeamMemberProfileActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(UitTeamMemberProfileActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(UitTeamMemberProfileActivity.this, t.getLocalizedMessage());
            }
        });
    }
}