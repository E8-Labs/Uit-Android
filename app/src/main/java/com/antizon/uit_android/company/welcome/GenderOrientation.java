package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GenderOrientation extends BaseActivity {

    private static final String TAG = GenderOrientation.class.getSimpleName();
    ImageView backIcon, redNoah;
    TextView next, notSure;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RangeSeekBar LGBTQSeekBar;

    int LGBTQ = 0;
    String menValue = "", womenValue = "", nonBinaryValue = "", lgbtValue = "", teamValue = "";
    ArrayList<ModelCompanySize> industriesList;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "",
            passwordValue = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "",
            trainingValue = "", genderValue = "", orientationValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_orientation);
        setIds();
        getIntentData();
        initialize();
        setListener();

    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        notSure = findViewById(R.id.notSure);
        redNoah = findViewById(R.id.redNoah);
        LGBTQSeekBar = findViewById(R.id.LGBTQ_seekbar);
    }

    void getIntentData() {
        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            selectedStageId = getIntent().getStringExtra("stageId");
            selectedStageName = getIntent().getStringExtra("stageName");
            sizeValue = getIntent().getStringExtra("size");
            headquarterValue = getIntent().getStringExtra("headquarter");
            websiteValue = getIntent().getStringExtra("website");
            emailValue = getIntent().getStringExtra("email");
            verificationValue = getIntent().getStringExtra("verification");
            passwordValue = getIntent().getStringExtra("password");
            deiStatementValue = getIntent().getStringExtra("deiStatement");
            leadValue = getIntent().getStringExtra("lead");
            ergValue = getIntent().getStringExtra("erg");
            programmingValue = getIntent().getStringExtra("programming");
            trainingValue = getIntent().getStringExtra("training");
            genderValue = getIntent().getStringExtra("genderProportion");

            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);
            Log.d(TAG, "getIntentData: stageName: "+selectedStageName);
            Log.d(TAG, "getIntentData: stageId: "+selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: "+headquarterValue);
            Log.d(TAG, "getIntentData: website: "+websiteValue);
            Log.d(TAG, "getIntentData: email: " + emailValue);
            Log.d(TAG, "getIntentData: deiStatement: " + deiStatementValue);
            Log.d(TAG, "getIntentData: lead: " + leadValue);
            Log.d(TAG, "getIntentData: erg: " + ergValue);
            Log.d(TAG, "getIntentData: programming: " + programmingValue);
            Log.d(TAG, "getIntentData: training: " + trainingValue);
            Log.d(TAG, "getIntentData: genderProportion: " + genderValue);


            Bundle bundle = getIntent().getExtras();
            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(GenderOrientation.this);
        sessionManagement = new SessionManagement(GenderOrientation.this);
        LGBTQSeekBar.setRange(0, 100);
        LGBTQSeekBar.setIndicatorTextDecimalFormat("0");
        loadProfile(GenderOrientation.this, encodedImageData, redNoah);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextScreen();

//                addDeiStatement();
            }
        });

        notSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GenderOrientation.this, Invite.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        LGBTQSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                LGBTQ = (int) leftValue;
                Log.d(TAG, "menSeekbar: onRangeChanged: value: " + LGBTQ);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


    }


    void addDeiStatement() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("men", menValue);
        params.put("women", womenValue);
        params.put("non_binary", nonBinaryValue);
        params.put("lgbt", lgbtValue);
        params.put("training", trainingValue);
        params.put("team_lead", teamValue);
        params.put("dei_statement", deiStatementValue);
        params.put("erg", ergValue);

        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.ADD_DEI_STATEMENT, params, sessionManagement.getToken());

    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" Applicant registering...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();

        JSONObject jsonObject = null;


        try {
            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: status: " + message);
            Log.d(TAG, "onResponse: status: " + status);
            Log.d(TAG, "onResponseReceived: response: " + response);

            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            if (status) {

                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());

                JSONObject profileObject = dataObject.getJSONObject("profile");
                Log.d(TAG, "onResponse: profile: size: " + profileObject.length());

                int id = profileObject.getInt("id");
                Log.d(TAG, "onResponseReceived: " + id);

//                String email = profileObject.getString("email");
//                Log.d(TAG, "onResponse: email " + email);
//                String profile_image = profileObject.getString("profile_image");
//                Log.d(TAG, "onResponse: profile_image " + profile_image);
//                String website = profileObject.getString("website");
//                Log.d(TAG, "onResponse: website " + website);
//                String city = profileObject.getString("city");
//                Log.d(TAG, "onResponse: city " + city);
//                String state = profileObject.getString("state");
//                Log.d(TAG, "onResponse: state " + state);
//                String phone = profileObject.getString("phone");
//                Log.d(TAG, "onResponse: phone " + phone);
//                String job_title = profileObject.getString("job_title");
//                Log.d(TAG, "onResponse: job_title " + job_title);
//                int account_status = profileObject.getInt("account_status");
//                Log.d(TAG, "onResponseReceived: "+ account_status);
//                String address = profileObject.getString("address");
//                Log.d(TAG, "onResponse: address " + address);
//                String bio = profileObject.getString("bio");
//                Log.d(TAG, "onResponse: bio " + bio);
//                String dob = profileObject.getString("dob");
//                Log.d(TAG, "onResponse: dob " + dob);
//                int role = profileObject.getInt("role");
//                Log.d(TAG, "onResponseReceived: "+ role);
//                int application_status = profileObject.getInt("application_status");
//                Log.d(TAG, "onResponseReceived: "+ application_status);
//                int user_id = profileObject.getInt("user_id");
//                Log.d(TAG, "onResponseReceived: "+ user_id);
//                String greenhouse_access_token = profileObject.getString("greenhouse_access_token");
//                Log.d(TAG, "onResponse: greenhouse_access_token " + greenhouse_access_token);
//
//                String name = profileObject.getString("name");
//                Log.d(TAG, "onResponse: name " + name);
//                airBnb.setText(name);


                Toast.makeText(GenderOrientation.this, "Account Created", Toast.LENGTH_SHORT).show();


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
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(GenderOrientation.this, Invite.class);

        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" +selectedStageId);
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

        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }


}