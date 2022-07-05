package com.antizon.uit_android.applicant.welcome;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
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

public class ApplicantAddCoverLetterActivity extends BaseActivity {
    private static final String TAG = ApplicantAddCoverLetterActivity.class.getSimpleName();
    ImageView addCoverLetter ,backIcon;

    SessionManagement sessionManagement;

    ProgressDialog progressDialog;
    ImageView redNoah2;
    String coverLetterValue;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_add_cover_letter);
        Utilities.setWhiteBars(ApplicantAddCoverLetterActivity.this);
        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        uploadCoverLetter();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        addCoverLetter = findViewById(R.id.companyLogoImage);
        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(ApplicantAddCoverLetterActivity.this);
        sessionManagement = new SessionManagement(ApplicantAddCoverLetterActivity.this);

        loadProfile(ApplicantAddCoverLetterActivity.this, sessionManagement.getProfileImage(), redNoah2);

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(view -> onBackPressed());


        addCoverLetter.setOnClickListener(view -> {

            ArrayList<String> docs = new ArrayList<>();
            docs.add(DocPicker.DocTypes.PDF);
            docs.add(DocPicker.DocTypes.MS_POWERPOINT);
            docs.add(DocPicker.DocTypes.MS_EXCEL);
            docs.add(DocPicker.DocTypes.TEXT);

            DocPickerConfig pickerConfig = new DocPickerConfig()
                    .setAllowMultiSelection(false)
                    .setShowConfirmationDialog(true)
                    .setExtArgs(docs);

            DocPicker.with(ApplicantAddCoverLetterActivity.this)
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
                            getContentResolver().getType(uri);
                            Glide.with(ApplicantAddCoverLetterActivity.this)
                                    .load(uri)
                                    .into(addCoverLetter);
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


    void uploadCoverLetter() {
        HashMap<String, String> params = new HashMap<>();
        params.put("cover_letter", coverLetterValue);

        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.UPLOAD_COVER_LETTER, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" Uploading Cover Letter...");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}