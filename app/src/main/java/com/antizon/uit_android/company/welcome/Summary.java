package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic.uit_interface.JsonPlaceHolderAPI;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Summary extends BaseActivity {

    private static final String TAG = Summary.class.getSimpleName();
    ImageView backIcon, redNoah;
    TextView confirm, companyNameTitle, https, gmail, address, stage, size, companyBioDetail, deiStatement;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    String latValue = "", langValue = "", cityValue = "", stateValue = "", addressValue = "", bioValue = "";

    ArrayList<ModelCompanySize> industriesList;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "",
            sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "",
            passwordValue = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "",
            trainingValue = "", genderValue = "", orientationValue = "", inviteValue = "", summaryValue = "", jobTitleValue = "";
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        redNoah = findViewById(R.id.redNoah);
        backIcon = findViewById(R.id.backIcon);
        confirm = findViewById(R.id.confirm);
        companyBioDetail = findViewById(R.id.companyBioDetail);
        https = findViewById(R.id.https);
        companyNameTitle = findViewById(R.id.companyTitle);
        gmail = findViewById(R.id.gmail);
        address = findViewById(R.id.address);
        stage = findViewById(R.id.stage);
        size = findViewById(R.id.size);
        deiStatement = findViewById(R.id.deiStatement);

    }

    void getIntentData() {
        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            companyNameTitle.setText("" + companyNameHintValue);
            typeHereValue = getIntent().getStringExtra("bio");
            companyBioDetail.setText(typeHereValue);

            selectedStageId = getIntent().getStringExtra("stageId");
            selectedStageName = getIntent().getStringExtra("stageName");
            stage.setText("" + selectedStageName);
            sizeValue = getIntent().getStringExtra("size");
            size.setText("" + sizeValue);

            headquarterValue = getIntent().getStringExtra("headquarter");

            websiteValue = getIntent().getStringExtra("website");
            https.setText("" + websiteValue);

            emailValue = getIntent().getStringExtra("email");
            gmail.setText("" + emailValue);

            verificationValue = getIntent().getStringExtra("verification");
            passwordValue = getIntent().getStringExtra("password");

            deiStatementValue = getIntent().getStringExtra("deiStatement");
            deiStatement.setText("" + deiStatementValue);
            leadValue = getIntent().getStringExtra("lead");
            ergValue = getIntent().getStringExtra("erg");
            programmingValue = getIntent().getStringExtra("programming");
            trainingValue = getIntent().getStringExtra("training");
            genderValue = getIntent().getStringExtra("genderProportion");
            inviteValue = getIntent().getStringExtra("invite");

            Bundle bundle = getIntent().getExtras();

            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

            file = new File(getRealPathFromURI(Uri.parse(encodedImageData)));
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Summary.this);
        sessionManagement = new SessionManagement(Summary.this);
        loadProfile(Summary.this, encodedImageData, redNoah);
    }


    void setListener() {
        Log.d(TAG, "setListener: ");


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerCompany();

            }
        });


    }


    private void registerCompany() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        Log.d(TAG, "registerApplicant: name: " + companyNameHintValue);
        Log.d(TAG, "registerApplicant: email: " + emailValue);
        Log.d(TAG, "registerApplicant: password: " + passwordValue);
        Log.d(TAG, "registerApplicant: website: " + websiteValue);
        Log.d(TAG, "registerApplicant: lat: " + latValue);
        Log.d(TAG, "registerApplicant: lang: " + langValue);
        Log.d(TAG, "registerApplicant: city: " + cityValue);
        Log.d(TAG, "registerApplicant: state: " + stateValue);
        Log.d(TAG, "registerApplicant: address: " + addressValue);
        Log.d(TAG, "registerApplicant: bio: " + bioValue);
        Log.d(TAG, "registerApplicant: file: " + file.getAbsolutePath());

        final RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), companyNameHintValue);
        final RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailValue);
        final RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), passwordValue);

        final RequestBody website = RequestBody.create(MediaType.parse("multipart/form-data"), websiteValue);
        final RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), latValue);
        final RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), langValue);
        final RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), cityValue);
        final RequestBody state = RequestBody.create(MediaType.parse("multipart/form-data"), stateValue);
        final RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), addressValue);
        final RequestBody bio = RequestBody.create(MediaType.parse("multipart/form-data"), bioValue);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", "profile_image", requestFile);

        JsonPlaceHolderAPI api = getApi();

//        Call<String> registerCompany(@Part("name") RequestBody name, @Part("email") RequestBody email,
//                @Part("password") RequestBody password, @Part("website") RequestBody website,
//                @Part("lat") RequestBody lat,@Part("lang") RequestBody lang,
//                @Part("city") RequestBody city,@Part("state") RequestBody state,
//                @Part("address") RequestBody address,@Part("bio") RequestBody bio,
//                @Part MultipartBody.Part file);
        Call<String> call = api.registerCompany(name, email, password, website,
                latitude, longitude, city, state, address, bio, body);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.d(TAG, response.toString());
                progressDialog.dismiss();
                if (response.body() != null) {
                    try {
                        createSession(response.body());
                    } catch (Exception e) {

                        Log.d(TAG, "uploadVideo: onResponse: Exception: " + e.getMessage());
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.d(TAG, e.getLocalizedMessage());
//                        Snackbar.make(getCurrentFocus(), "uploadVideo: Error uploading image", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "uploadVideo: Error uploading Video");
                    progressDialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "uploadVideo: onFailure: error: " + t.getMessage());
            }
        });
    }

    void createSession(String response) {
        progressDialog.dismiss();
        JSONObject jsonObject = null;
        try {
            Log.d(TAG, "onResponseReceived: response: " + response);

            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");

            Log.d(TAG, "createSession: status: " + status);
            Log.d(TAG, "createSession: message: " + message);

            if (status) {

                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONObject profileObject = dataObject.getJSONObject("profile");
                JSONObject accessTokenObject = dataObject.getJSONObject("access_token");
                Log.d(TAG, "onResponse: data: size: " + profileObject.length());
                int id = profileObject.getInt("id");
                String email = profileObject.getString("email");
                String name = profileObject.getString("name");
                String profile_image = profileObject.getString("profile_image");
                String website = profileObject.getString("website");
                String city = profileObject.getString("city");
                String state = profileObject.getString("state");
                String phone = profileObject.getString("phone");
                String job_title = profileObject.getString("job_title");
                int account_status = profileObject.getInt("account_status");
                String address = profileObject.getString("address");
                String bio = profileObject.getString("bio");
                String dob = profileObject.getString("dob");
                int role = profileObject.getInt("role");
                int application_status = profileObject.getInt("application_status");
                int user_id = profileObject.getInt("user_id");
                String access_token = accessTokenObject.getString("token");

                sessionManagement.createLoginSession("" + id, email, name, passwordValue, website, profile_image,
                        address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role,
                        access_token, "" + application_status);
                Toast.makeText(Summary.this, "User Successfully Signed In.", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onResponseReceived: role: " + role);
                openNextScreen();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(Summary.this, Congratulation.class);

        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" + selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtra("size", sizeValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
        intent.putExtra("verification", verificationValue);
        intent.putExtra("password", passwordValue);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        intent.putExtra("erg", ergValue);
        intent.putExtra("programming", programmingValue);
        intent.putExtra("training", trainingValue);
        intent.putExtra("genderProportion", genderValue);
        intent.putExtra("genderOrientation", orientationValue);
        intent.putExtra("invite", inviteValue);
        intent.putExtra("summary", summaryValue);
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}