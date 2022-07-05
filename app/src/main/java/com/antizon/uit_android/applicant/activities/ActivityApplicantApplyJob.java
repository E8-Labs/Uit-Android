package com.antizon.uit_android.applicant.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelApplicantDepartment;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.uit_admin.welcome.ProfessionalInformation;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.greentoad.turtlebody.docpicker.DocPicker;
import com.greentoad.turtlebody.docpicker.core.DocPickerConfig;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ActivityApplicantApplyJob extends BaseActivity {
    Context context;

    private static final String TAG = ActivityApplicantApplyJob.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    TextView submitMyApplication, text_location, text_companyName, text_jobTitle, text_salary, text_locationType, text_userName, text_personalJob, addResume, addCover;
    ImageView backIcon, arrow;
    RoundedImageView companyImage, user_ProfileImage;

    File file;

    ApplicantHomeJobDataModel jobDataModel;

    ModelAdminApplicants modelAdminApplicants;
    ModelApplicantDepartment modelApplicantDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_apply_job);
        Utilities.setCustomStatusAndNavColor(ActivityApplicantApplyJob.this, R.color.white_dash, R.color.white_dash);
        context = ActivityApplicantApplyJob.this;
        initViews();
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


        String location =  jobDataModel.getCity() + ", " + jobDataModel.getState();
        String salaryRange = "$" +  jobDataModel.getMin_salary() + "-" + "$" +  jobDataModel.getMax_salary();

        text_location.setText(location);

        Utilities.loadImage(context, jobDataModel.getCompanyDataModel().getProfile_image(), companyImage);

        text_companyName.setText(jobDataModel.getCompanyDataModel().getName());
        text_jobTitle.setText(jobDataModel.getJob_title());
        text_salary.setText(salaryRange);

        text_userName.setText(sessionManagement.getUserName());
        Utilities.loadImage(context, sessionManagement.getProfileImage(), user_ProfileImage);


        arrow.setOnClickListener(view -> {
            modelAdminApplicants = new ModelAdminApplicants();
            modelApplicantDepartment =  new ModelApplicantDepartment();

            modelApplicantDepartment.setId(jobDataModel.getIndustryDataModel().getId());
            modelApplicantDepartment.setName(jobDataModel.getIndustryDataModel().getName());

            modelAdminApplicants.setCity(sessionManagement.getKeyCity());
            modelAdminApplicants.setProfile_image(sessionManagement.getProfileImage());
            modelAdminApplicants.setName(sessionManagement.getUserName());
            modelAdminApplicants.setIndustryDataModel(modelApplicantDepartment);

            Intent intent = new Intent( ActivityApplicantApplyJob.this, ProfessionalInformation.class);
            intent.putExtra("dataModel", modelAdminApplicants);
            startActivity(intent);
        });

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

        addResume.setOnClickListener(view -> {
            ArrayList<String> docs = new ArrayList<>();
            docs.add(DocPicker.DocTypes.PDF);
            docs.add(DocPicker.DocTypes.MS_POWERPOINT);
            docs.add(DocPicker.DocTypes.MS_EXCEL);
            docs.add(DocPicker.DocTypes.TEXT);

            DocPickerConfig pickerConfig = new DocPickerConfig()
                    .setAllowMultiSelection(false)
                    .setShowConfirmationDialog(true)
                    .setExtArgs(docs);

            DocPicker.with(ActivityApplicantApplyJob.this)
                    .setConfig(pickerConfig)
                    .onResult()
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: d: " + d.toString());
                        }

                        @Override
                        public void onNext(ArrayList<Uri> uris) {
                            Log.d(TAG, "onNext: uris: " + uris.size());
                            Uri uri = uris.get(0);
                            file = new File(uris.get(0).getPath());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: throwable: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");

                        }
                    });
        });
        addCover.setOnClickListener(view -> {

            ArrayList<String> docs = new ArrayList<>();
            docs.add(DocPicker.DocTypes.PDF);
            docs.add(DocPicker.DocTypes.MS_POWERPOINT);
            docs.add(DocPicker.DocTypes.MS_EXCEL);
            docs.add(DocPicker.DocTypes.TEXT);

            DocPickerConfig pickerConfig = new DocPickerConfig()
                    .setAllowMultiSelection(false)
                    .setShowConfirmationDialog(true)
                    .setExtArgs(docs);

            DocPicker.with(ActivityApplicantApplyJob.this)
                    .setConfig(pickerConfig)
                    .onResult()
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(TAG, "onSubscribe: d: " + d.toString());
                        }

                        @Override
                        public void onNext(ArrayList<Uri> uris) {
                            Log.d(TAG, "onNext: uris: " + uris.size());
                            Uri uri = uris.get(0);
                            file = new File(uris.get(0).getPath());

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: throwable: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");

                        }
                    });
        });
    }

    void applyToJob() {
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
}