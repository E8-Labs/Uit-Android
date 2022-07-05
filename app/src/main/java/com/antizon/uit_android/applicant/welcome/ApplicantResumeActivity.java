package com.antizon.uit_android.applicant.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;
import com.greentoad.turtlebody.docpicker.DocPicker;
import com.greentoad.turtlebody.docpicker.core.DocPickerConfig;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ApplicantResumeActivity extends BaseActivity {

    private static final String TAG = ApplicantResumeActivity.class.getSimpleName();
    ImageView addResume, backIcon, redNoah2;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    TextView next;
    File file;
    String resumeValue;

    String companyValue="", jobValue="",  roleTextValue="",
            nameOfReferenceValue="", selectJobValue="", phoneValue="";

    String employeValue = "", encodedImageData = "", educationValue = "",professionalValue="",companyDetailValue=""
            ,fieldOfStudyValue="", universityNameValue = "",degreeSpinnerValue="",startYearValue="";
    private ArrayList<ModelApplicantJobs> departmentList, jobList, locationList, interestedJobTypeList, companyStageList,
            companySizeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_resume);
        Utilities.setWhiteBars(ApplicantResumeActivity.this);
        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        addResume();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        next = findViewById(R.id.next);
        addResume = findViewById(R.id.addResume);
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
    }


    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            employeValue = getIntent().getStringExtra("employeStatus");
            educationValue = getIntent().getStringExtra("education");
            companyDetailValue = getIntent().getStringExtra("companyDetail");


            fieldOfStudyValue = getIntent().getStringExtra("fieldOfStudy");
            startYearValue = getIntent().getStringExtra("startYear");
            universityNameValue = getIntent().getStringExtra("universityName");
            degreeSpinnerValue = getIntent().getStringExtra("degreeSpinner");

            Log.d(TAG, "getIntentData: image:" + encodedImageData);
            Log.d(TAG, "getIntentData: employeStatus:" + employeValue);
            Log.d(TAG, "getIntentData: education:" + educationValue);
            Log.d(TAG, "getIntentData: companyDetail:" + companyDetailValue);

            Log.d(TAG, "getIntentData: education:" + fieldOfStudyValue);
            Log.d(TAG, "getIntentData: education:" + startYearValue);
            Log.d(TAG, "getIntentData: education:" + universityNameValue);
            Log.d(TAG, "getIntentData: education:" + degreeSpinnerValue);

            Bundle bundle = getIntent().getExtras();
            departmentList = bundle.getParcelableArrayList("departmentList");
            jobList = bundle.getParcelableArrayList("jobList");
            locationList = bundle.getParcelableArrayList("locationList");
            interestedJobTypeList = bundle.getParcelableArrayList("interestedJobTypeList");
            companyStageList = bundle.getParcelableArrayList("companyStageList");
            companySizeList = bundle.getParcelableArrayList("companySizeList");

            Log.d(TAG, "getIntentData: departmentList: " + departmentList.size());
            Log.d(TAG, "getIntentData: jobList: " + jobList.size());
            Log.d(TAG, "getIntentData: locationList: " + locationList.size());
            Log.d(TAG, "getIntentData: interestedJobTypeList: " + interestedJobTypeList.size());
            Log.d(TAG, "getIntentData: companyStageList: " + companyStageList.size());
            Log.d(TAG, "getIntentData: companySizeList: " + companySizeList.size());
        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(ApplicantResumeActivity.this);
        sessionManagement = new SessionManagement(ApplicantResumeActivity.this);
        loadProfile(ApplicantResumeActivity.this, sessionManagement.getProfileImage(), redNoah2);

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

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

            DocPicker.with(ApplicantResumeActivity.this)
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
                            String mimeType = getContentResolver().getType(uri);
                            Glide.with(ApplicantResumeActivity.this)
                                    .load(uri)
                                    .into(addResume);
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

        next.setOnClickListener(view -> {
            if (file != null) {
                openNextScreen();
//                    sendServerRequestPOST("",);
            } else {
                Toast.makeText(ApplicantResumeActivity.this, "Please attach your resume.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void addResume() {
        HashMap<String, String> params = new HashMap<>();
        params.put("resume", resumeValue);

        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.UPLOAD_RESUME, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" Uploading Resume...");
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
            Log.d(TAG, "onResponse: status: " + message);
            Log.d(TAG, "onResponse: status: " + status);
            Log.d(TAG, "onResponseReceived: response: " + response);

            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            if (status) {

                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());

                int id = dataObject.getInt("id");
                Log.d(TAG, "onResponse: id " + id);

                Log.d(TAG, "onResponseReceived: response: " + response);
                Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);

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

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("departmentList", departmentList);
        bundle.putParcelableArrayList("jobList", jobList);
        bundle.putParcelableArrayList("locationList", locationList);
        bundle.putParcelableArrayList("interestedJobTypeList", interestedJobTypeList);
        bundle.putParcelableArrayList("companyStageList", companyStageList);
        bundle.putParcelableArrayList("companySizeList", companySizeList);
//        bundle.putParcelableArrayList("professionalInterestList", list);

        Intent intent = new Intent(ApplicantResumeActivity.this, ApplicantAddCoverLetterActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("employeStatus", employeValue);
        intent.putExtra("education", educationValue);
        intent.putExtra("companyDetail", companyDetailValue);
        intent.putExtra("interest", professionalValue);

        intent.putExtra("fieldOfStudy", fieldOfStudyValue);
        intent.putExtra("startYear", startYearValue);
        intent.putExtra("universityName", universityNameValue);
        intent.putExtra("degreeSpinner", degreeSpinnerValue);

        intent.putExtra("companyName", companyValue);
        intent.putExtra("job", jobValue);
        intent.putExtra("role", roleTextValue);
        intent.putExtra("selectJob", selectJobValue);
        intent.putExtra("nameOfReference", nameOfReferenceValue);
        intent.putExtra("phone", phoneValue);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}