package com.antizon.uit_android.applicant.welcome;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.races.RacesModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityApplicantSexualOrientation extends BaseActivity {
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    ImageView backIcon,menYellow;
    TextView skip;
    TextView man , woman, nonBinary;
    ConstraintLayout approvedLayout,maleLayout,femaleLayout;
    String sexualValue="", genderIdentity = "", encodedImageData = "", from, applicantLgbt;

    RacesModel selectedRaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_sexual_orientation);
        Utilities.setWhiteBars(ActivityApplicantSexualOrientation.this);
        context = ActivityApplicantSexualOrientation.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);


        getIntentData();
        setIds();
        initialize();
        setListener();

        if (from.equals("edit")){
            switch (Integer.parseInt(applicantLgbt)) {
                case 0:
                    setApprovedOneLayout();
                    break;
                case 1:
                    setPendingOneLayout();
                    break;
                case -1:
                default:
                    setPausedOneLayout();
            }
        }
    }

    private void setIds() {
        backIcon = findViewById(R.id.backIcon);
        skip = findViewById(R.id.skip);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        nonBinary = findViewById(R.id.nonBinary);
        maleLayout = findViewById(R.id.maleLayout);
        femaleLayout = findViewById(R.id.femaleLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        menYellow = findViewById(R.id.menYellow);

    }
    private void  getIntentData(){
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("edit")){
                applicantLgbt = getIntent().getStringExtra("applicantLgbt");
            }else {
                encodedImageData = getIntent().getStringExtra("profilePic");
                genderIdentity = getIntent().getStringExtra("genderIdentity");
                selectedRaces = getIntent().getParcelableExtra("selectedRaces");
            }
        }

    }

    private void initialize() {
        loadProfile(ActivityApplicantSexualOrientation.this, encodedImageData, menYellow);
        sessionManagement = new SessionManagement(ActivityApplicantSexualOrientation.this);
        loadProfile(ActivityApplicantSexualOrientation.this, sessionManagement.getProfileImage(), menYellow);
    }

    private void setListener() {
        skip.setOnClickListener(v -> {
            if (from.equals("edit")){
                onBackPressed();
            }else {
                openNextScreen();
            }

        });
        
        backIcon.setOnClickListener(v -> onBackPressed());

        maleLayout.setOnClickListener(v -> {
            setPendingOneLayout();
            sexualValue="1";
            updateOrMoveToNext();
        });
        
        femaleLayout.setOnClickListener(v -> {
            setApprovedOneLayout();
            sexualValue="0";
            updateOrMoveToNext();
        });
        
        approvedLayout.setOnClickListener(v -> {
            setPausedOneLayout();
            sexualValue="-1";
            updateOrMoveToNext();
        });

    }

    private void setPendingOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.login_background);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);

        man.setTextColor(getColor(R.color.white));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.black));

    }

    private void setApprovedOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.login_background);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.white));
        nonBinary.setTextColor(getColor(R.color.black));

    }

    private void setPausedOneLayout() {
        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.login_background);
        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.white));
    }

    private void openNextScreen() {
        Intent intent = new Intent(ActivityApplicantSexualOrientation.this, ActivityApplicantVeteranStatus.class);
        intent.putExtra("from", from);
        intent.putExtra("selectedRaces", selectedRaces);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("genderIdentity", genderIdentity);
        intent.putExtra("sexualOrientation", sexualValue);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


    private void updateOrMoveToNext(){
        if (from.equals("edit")){
            progressDialog.setMessage("Updating...");
            progressDialog.show();
            requestForUpdateApplicantSexOrientation(sexualValue);
        }else {
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }

    }

    private void requestForUpdateApplicantSexOrientation(String sexualOrientation) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("lgbt", sexualOrientation);
        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Call<MainResponseModel> call = service.updateDemographicInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("applicantLgbt", sexualOrientation);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(ActivityApplicantSexualOrientation.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantSexualOrientation.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showFailureToast(ActivityApplicantSexualOrientation.this, "Failure!", t.getMessage());
            }
        });
    }
}