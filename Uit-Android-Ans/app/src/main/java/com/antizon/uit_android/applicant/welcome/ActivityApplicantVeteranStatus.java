package com.antizon.uit_android.applicant.welcome;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelApplicantRace;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.races.RacesModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityApplicantVeteranStatus extends AppCompatActivity {
    GetDataService service;

    private static final String TAG = ActivityApplicantVeteranStatus.class.getSimpleName();
    ImageView backIcon, menYellow;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    TextView skip;
    TextView man, woman, nonBinary;
    ConstraintLayout approvedLayout, maleLayout, femaleLayout;

    RacesModel selectedRaces;

    String sexualValue = "", genderIdentity = "", encodedImageData = "", veteranValue="", from;

    Integer applicantVeteranStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_veteran_status);
        Utilities.setWhiteBars(ActivityApplicantVeteranStatus.this);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        getIntentData();
        setIds();
        initialize();
        setListener();

        if (from.equals("edit")){
            if (applicantVeteranStatus  == null || applicantVeteranStatus  == -1){
                setPausedOneLayout();
            }else {
                if (applicantVeteranStatus == 0){
                    setApprovedOneLayout();
                }else {
                    setPendingOneLayout();
                }
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

    private void getIntentData() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("edit")){
                applicantVeteranStatus = getIntent().getIntExtra("applicantVeteranStatus", -1);
            }else {
                encodedImageData = getIntent().getStringExtra("profilePic");
                genderIdentity = getIntent().getStringExtra("genderIdentity");
                sexualValue = getIntent().getStringExtra("sexualOrientation");
                selectedRaces = getIntent().getParcelableExtra("selectedRaces");
            }

        }
    }

    private void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(ActivityApplicantVeteranStatus.this);
        sessionManagement=new SessionManagement(ActivityApplicantVeteranStatus.this);
        Utilities.loadCircleImage(ActivityApplicantVeteranStatus.this, sessionManagement.getProfileImage(), menYellow);
    }


    private void setListener() {
        Log.d(TAG, "setListener: ");

        maleLayout.setOnClickListener(v -> {
            veteranValue="1";
            applicantVeteranStatus = 1;
            setPendingOneLayout();
            updateOrMoveToNext();

        });

        femaleLayout.setOnClickListener(v -> {
            veteranValue="0";
            applicantVeteranStatus = 0;
            setApprovedOneLayout();
            updateOrMoveToNext();
        });

        approvedLayout.setOnClickListener(v -> {
            veteranValue="-1";
            applicantVeteranStatus = null;
            setPausedOneLayout();
            updateOrMoveToNext();
        });

        skip.setOnClickListener(v -> {
            if (from.equals("edit")){
                onBackPressed();
            }else {
                openNextScreen();
            }
        });

        backIcon.setOnClickListener(v -> onBackPressed());

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



    private void requestForAddDemoGraphicInfo(List<ModelApplicantRace> raceList) {

        JsonArray racesArray = new JsonArray();

        for (int i = 0; i < raceList.size(); i++) {
            JsonObject raceObject = new JsonObject();
            // add the exercise category to Json
            raceObject.addProperty("race", raceList.get(i).getId());
            racesArray.add(raceObject);
        }

        JsonObject mainObject = new JsonObject();

        mainObject.addProperty("lgbt", sexualValue);
        mainObject.addProperty("veteran", veteranValue);
        mainObject.addProperty("gender", genderIdentity);
        mainObject.add("races", racesArray);

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));


        Call<MainResponseModel> call = service.addDemographicInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (!status) {
                        CustomCookieToast.showFailureToast(ActivityApplicantVeteranStatus.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantVeteranStatus.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showFailureToast(ActivityApplicantVeteranStatus.this, "Failure!", t.getMessage());
            }
        });
    }


    private void openNextScreen() {
        Intent intent = new Intent(ActivityApplicantVeteranStatus.this, ApplicantThankYouActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("selectedRaces", selectedRaces);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("genderIdentity", genderIdentity);
        intent.putExtra("sexualOrientation", sexualValue);
        intent.putExtra("veteranStatus", veteranValue);
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


    private void updateOrMoveToNext(){
        if (from.equals("edit")){
            progressDialog.setMessage("Updating...");
            progressDialog.show();
            requestForUpdateApplicantVeteranStatus(applicantVeteranStatus);
        }else {
            requestForAddDemoGraphicInfo(selectedRaces.getSelectedRacesList());
            openNextScreen();
        }

    }

    private void requestForUpdateApplicantVeteranStatus(Integer veteranValue) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("veteran", veteranValue);
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
                        intent.putExtra("applicantVeteranStatus", applicantVeteranStatus);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(ActivityApplicantVeteranStatus.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantVeteranStatus.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showFailureToast(ActivityApplicantVeteranStatus.this, "Failure!", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}