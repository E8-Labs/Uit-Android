package com.antizon.uit_android.applicant.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.PdfViewerActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantAddCoverLetterActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantResumeActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.profile.ApplicantProfileResponseModel;
import com.antizon.uit_android.models.profile.ProfileResponseDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityApplicantApplyJob extends BaseActivity {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    private static final String TAG = ActivityApplicantApplyJob.class.getSimpleName();
    ProgressDialog progressDialog;
    TextView submitMyApplication, text_location, text_companyName, text_jobTitle, text_salary, text_locationType, text_userName, text_personalJob, text_resume, text_coverLetter, addResume, addCover;
    ImageView backIcon, arrow, resumeIcon, coverIcon;
    RoundedImageView companyImage, user_ProfileImage;

    ApplicantHomeJobDataModel jobDataModel;

    ProfileResponseDataModel applicantProfileDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_apply_job);
        Utilities.setCustomStatusAndNavColor(ActivityApplicantApplyJob.this, R.color.white_dash, R.color.white_dash);
        context = ActivityApplicantApplyJob.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
    }

    void initViews() {
        progressDialog = new ProgressDialog(ActivityApplicantApplyJob.this);
        sessionManagement = new SessionManagement(ActivityApplicantApplyJob.this);

        jobDataModel = getIntent().getParcelableExtra("jobDataModel");

        submitMyApplication = findViewById(R.id.submitMyApplication);
        companyImage = findViewById(R.id.companyImage);
        text_location = findViewById(R.id.text_location);
        text_companyName = findViewById(R.id.text_companyName);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        text_salary = findViewById(R.id.text_salary);
        text_locationType = findViewById(R.id.text_locationType);
        backIcon = findViewById(R.id.backIcon);
        addResume = findViewById(R.id.addResume);
        addCover = findViewById(R.id.addCover);
        arrow = findViewById(R.id.arrow);
        text_userName = findViewById(R.id.text_userName);
        text_personalJob = findViewById(R.id.text_personalJob);
        user_ProfileImage = findViewById(R.id.user_ProfileImage);
        text_resume = findViewById(R.id.text_resume);
        resumeIcon = findViewById(R.id.resumeIcon);
        text_coverLetter = findViewById(R.id.text_coverLetter);
        coverIcon = findViewById(R.id.coverIcon);


        String location =  jobDataModel.getCity() + ", " + jobDataModel.getState();
        String salaryRange = "$" +  jobDataModel.getMin_salary() + "-" + "$" +  jobDataModel.getMax_salary();

        text_location.setText(location);

        Utilities.loadImage(context, jobDataModel.getCompanyDataModel().getProfile_image(), companyImage);

        text_companyName.setText(jobDataModel.getCompanyDataModel().getName());
        text_jobTitle.setText(jobDataModel.getJob_title());
        text_salary.setText(salaryRange);


        text_userName.setText(sessionManagement.getUserName());
        Utilities.loadImage(context, sessionManagement.getProfileImage(), user_ProfileImage);


        submitMyApplication.setOnClickListener(view -> {

            if (sessionManagement.getKeyApplicationStatus().equals("1")){
                CustomCookieToast.showRequiredToast(ActivityApplicantApplyJob.this, "Can't apply for this job because your demo graphic information is incomplete");
            }else if (sessionManagement.getKeyApplicationStatus().equals("2")){
                CustomCookieToast.showRequiredToast(ActivityApplicantApplyJob.this, "Can't apply for this job because your professional information is incomplete");
            }else {
                applyToJob();
            }


        });

        backIcon.setOnClickListener(view -> onBackPressed());


    }

    private void applyToJob() {
        HashMap<String, String> params = new HashMap<>();
        params.put("job_id", String.valueOf(jobDataModel.getId()));
        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.POST_APPLY_FOR_JOB, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" Adding Jobs...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status){
                Intent intent = new Intent(ActivityApplicantApplyJob.this, ActivityApplicantApplyJobCongrats.class);
                intent.putExtra("jobDataModel", jobDataModel);
                onApplyJobLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }else {
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onApplyJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    ActivityResultLauncher<Intent> onResumeAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
            resumeIcon.setVisibility(View.VISIBLE);
            text_resume.setText(R.string.text_resume);
            addResume.setText(R.string.view);
            addResume.setEnabled(false);
            sessionManagement.setResumeSaved("true");
        }
    });

    ActivityResultLauncher<Intent> onCoverLetterAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            requestForUserProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());
            coverIcon.setVisibility(View.VISIBLE);
            text_coverLetter.setText(R.string.coverLetter);
            addCover.setText(R.string.view);
            addCover.setEnabled(false);
            sessionManagement.setCoverLetterSaved("true");
        }
    });

    private void moveToPdfViewerActivity(String pdfUrl, String headerText){
        Intent flagJobIntent = new Intent(context, PdfViewerActivity.class);
        flagJobIntent.putExtra("headerText", headerText);
        flagJobIntent.putExtra("pdfUrl",pdfUrl );
        startActivity(flagJobIntent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void requestForUserProfile(String authToken, String userId) {

        Call<ApplicantProfileResponseModel> call = service.getApplicantProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Response<ApplicantProfileResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        applicantProfileDataModel = response.body().getData();
                        if (applicantProfileDataModel !=null){
                            text_personalJob.setText(applicantProfileDataModel.getJob_title());

                            if (applicantProfileDataModel.getProfessionalInfoDataModel() !=null){

                                sessionManagement.setApplicationStatus(applicantProfileDataModel.getApplication_status() +"");


                                if (applicantProfileDataModel.getProfessionalInfoDataModel().getResume() !=null){
                                    resumeIcon.setVisibility(View.VISIBLE);
                                    text_resume.setText(R.string.text_resume);
                                    addResume.setText(R.string.view);
                                    addResume.setOnClickListener(v -> moveToPdfViewerActivity(sessionManagement.getKeyResumeLink(), "Your Resume"));

                                    addResume.setOnClickListener(v -> moveToPdfViewerActivity(applicantProfileDataModel.getProfessionalInfoDataModel().getResume(), applicantProfileDataModel.getName() +" 's Resume"));

                                }else {

                                    resumeIcon.setVisibility(View.GONE);
                                    text_resume.setText(context.getString(R.string.text_notAvailableShort));
                                    addResume.setText(context.getString(R.string.addResume));
                                    addResume.setOnClickListener(v -> {
                                        Intent intent = new Intent(context, ApplicantResumeActivity.class);
                                        intent.putExtra("type", "edit");
                                        onResumeAddedLauncher.launch(intent);
                                        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });

                                }

                                if (applicantProfileDataModel.getProfessionalInfoDataModel().getCover_letter() !=null){
                                    coverIcon.setVisibility(View.VISIBLE);
                                    text_coverLetter.setText(R.string.coverLetter);
                                    addCover.setText(R.string.view);

                                    addCover.setOnClickListener(v -> moveToPdfViewerActivity(sessionManagement.getKeyCoverLetterLink(), applicantProfileDataModel.getName() +" 's Cover Letter"));

                                }else {
                                    coverIcon.setVisibility(View.GONE);
                                    text_coverLetter.setText(context.getString(R.string.text_notAvailableShort));
                                    addCover.setText(context.getString(R.string.addCoverLetter));
                                    addCover.setOnClickListener(v -> {
                                        Intent intent = new Intent(context, ApplicantAddCoverLetterActivity.class);
                                        intent.putExtra("type", "edit");
                                        onCoverLetterAddedLauncher.launch(intent);
                                        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    });
                                }
                            }else {
                                resumeIcon.setVisibility(View.GONE);
                                text_resume.setText(context.getString(R.string.text_notAvailableShort));
                                addResume.setText(context.getString(R.string.addResume));
                                addResume.setOnClickListener(v -> {
                                    Intent intent = new Intent(context, ApplicantResumeActivity.class);
                                    intent.putExtra("type", "edit");
                                    onResumeAddedLauncher.launch(intent);
                                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                });
                                coverIcon.setVisibility(View.GONE);
                                text_coverLetter.setText(context.getString(R.string.text_notAvailableShort));
                                addCover.setText(context.getString(R.string.addCoverLetter));
                                addCover.setOnClickListener(v -> {
                                    Intent intent = new Intent(context, ApplicantAddCoverLetterActivity.class);
                                    intent.putExtra("type", "edit");
                                    onCoverLetterAddedLauncher.launch(intent);
                                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                });
                            }


                            arrow.setOnClickListener(view -> moveToNextScreen(applicantProfileDataModel));


                        }
                    }else {

                        CustomCookieToast.showFailureToast(ActivityApplicantApplyJob.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantApplyJob.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ActivityApplicantApplyJob.this, "Failure!", t.getMessage());
            }
        });
    }


    private void moveToNextScreen(ProfileResponseDataModel applicantProfileDataModel){
        Intent intent = new Intent(context, ApplicantProfessionalActivity.class);
        intent.putExtra("applicantId", sessionManagement.getKeyId());
        intent.putExtra("applicationStatus", "");
        intent.putExtra("jobId", "");
        intent.putExtra("data", applicantProfileDataModel);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}