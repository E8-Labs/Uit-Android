package com.antizon.uit_android.applicant.welcome;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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

public class ActivityApplicantSelectGenderIdentity extends BaseActivity {
    private static final String TAG = ActivityApplicantSelectGenderIdentity.class.getSimpleName();
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    ImageView backIcon, menYellow;
    TextView skip, man, woman, nonBinary, prefer;

    ImageView menIcon, womenIcon, nonIcon;
    ConstraintLayout approvedLayout, pausedLayout, maleLayout, femaleLayout;

    RacesModel selectedRaces;

    String identityValue = "", encodedImageData = "", from, applicantGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_gender_identity);
        Utilities.setWhiteBars(ActivityApplicantSelectGenderIdentity.this);
        context = ActivityApplicantSelectGenderIdentity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        getIntentData();
        setIds();
        initialize();
        setListener();

        if (from.equals("edit")){
            switch (Integer.parseInt(applicantGender)) {
                case 1:
                    setPendingOneLayout();
                    break;
                case 2:
                    setApprovedOneLayout();
                    break;
                case 3:
                    setPausedOneLayout();
                    break;
                case 4:
                    setFullLayout();
            }
        }
    }

    private void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        skip = findViewById(R.id.skip);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        nonBinary = findViewById(R.id.nonBinary);
        prefer = findViewById(R.id.prefer);
        menYellow = findViewById(R.id.menYellow);
        menIcon = findViewById(R.id.menIcon);
        womenIcon = findViewById(R.id.womenIcon);
        nonIcon = findViewById(R.id.nonIcon);
        maleLayout = findViewById(R.id.maleLayout);
        femaleLayout = findViewById(R.id.femaleLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("edit")){
                applicantGender = getIntent().getStringExtra("applicantGender");
            }else {
                encodedImageData = getIntent().getStringExtra("profilePic");
                selectedRaces = getIntent().getParcelableExtra("selectedRaces");
            }

        }
    }

    private void initialize() {
        loadProfile(ActivityApplicantSelectGenderIdentity.this, encodedImageData, menYellow);
        sessionManagement=new SessionManagement(ActivityApplicantSelectGenderIdentity.this);
        loadProfile(ActivityApplicantSelectGenderIdentity.this, sessionManagement.getProfileImage(), menYellow);
    }

    private void setListener() {
        maleLayout.setOnClickListener(v -> {
            setPendingOneLayout();
            identityValue = "1";
            updateOrMoveToNext();
        });

        femaleLayout.setOnClickListener(v -> {
            setApprovedOneLayout();
            identityValue = "2";
            updateOrMoveToNext();
        });

        approvedLayout.setOnClickListener(v -> {
            setPausedOneLayout();
            identityValue = "3";
            updateOrMoveToNext();
        });

        pausedLayout.setOnClickListener(v -> {
            setFullLayout();
            identityValue = "4";
            updateOrMoveToNext();
        });

        skip.setOnClickListener(v -> {
            if (from.equals("edit")){
                onBackPressed();
            }else {
                openNextScreen();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }

        });

        backIcon.setOnClickListener(v -> onBackPressed());

    }

    private void setPendingOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.login_background);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);

        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));

        man.setTextColor(getColor(R.color.white));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.black));
        prefer.setTextColor(getColor(R.color.black));
    }

    private void setApprovedOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.login_background);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);


        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.white));
        nonBinary.setTextColor(getColor(R.color.black));
        prefer.setTextColor(getColor(R.color.black));
    }

    private void setPausedOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.login_background);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);

        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.white));
        prefer.setTextColor(getColor(R.color.black));

    }

    private void setFullLayout() {
        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.login_background);

        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.black));
        prefer.setTextColor(getColor(R.color.white));
    }

    private void openNextScreen() {
        Intent intent = new Intent(ActivityApplicantSelectGenderIdentity.this, ActivityApplicantSexualOrientation.class);
        intent.putExtra("from", from);
        intent.putExtra("selectedRaces", selectedRaces);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("genderIdentity", identityValue);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
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
            requestForUpdateApplicantGender(identityValue);
        }else {
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }

    }

    private void requestForUpdateApplicantGender(String applicantGender) {


        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("gender", applicantGender);

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
                        intent.putExtra("applicantGender", applicantGender);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(ActivityApplicantSelectGenderIdentity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantSelectGenderIdentity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showFailureToast(ActivityApplicantSelectGenderIdentity.this, "Failure!", t.getMessage());
            }
        });
    }
}