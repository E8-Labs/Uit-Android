package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;

import java.util.ArrayList;

public class DEIStatement2 extends BaseActivity {
    private static final String TAG = DEIStatement2.class.getSimpleName();
    ProgressDialog progressDialog;

    ImageView backIcon, redNoah;
    TextView next;
    EditText deiDetail;
    ArrayList<ModelCompanySize> industriesList;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "",
            sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "",
            passwordValue = "", deiStatementValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deistatement2);

        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah = findViewById(R.id.redNoah);
        deiDetail = findViewById(R.id.deiDetail);

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

            Log.d(TAG, "getIntentData: image: " + encodedImageData);
            Log.d(TAG, "getIntentData: companyName: " + companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: " + typeHereValue);
            Log.d(TAG, "getIntentData: stageName: " + selectedStageName);
            Log.d(TAG, "getIntentData: stageId: " + selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: " + headquarterValue);
            Log.d(TAG, "getIntentData: website: " + websiteValue);
            Log.d(TAG, "getIntentData: email: " + emailValue);


            Bundle bundle = getIntent().getExtras();

            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(DEIStatement2.this);
        loadProfile(DEIStatement2.this, encodedImageData, redNoah);

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
                hideSoftKeyboard(DEIStatement2.this, deiDetail);
                if (!validate()) {

                } else {
                    openNextScreen();
                }
            }
        });


    }


    public boolean validate() {
        Log.d(TAG, "validate: ");
        typeHereValue = deiDetail.getText().toString().trim();

        boolean valid = true;
        if (typeHereValue.isEmpty()) {
            deiDetail.setError("Please enter company bio");
            valid = false;
        }
        return valid;
    }

//    void deiStatment() {
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("men", "20");
//        params.put("women", "41");
//        params.put("non_binary", "39");
//        params.put("lgbt", "39");
//        params.put("training", "1");
//        params.put("team_lead", "1");
//        params.put("dei_statement", "1");
//        params.put("erg", "1");
//        params.put("programming", "0");
//
//        Log.d(TAG, "getDocumentData: params: " + params);
//        sendServerRequestPOST(AppConstants.ADD_DEI_STATEMENT, params);
//
//    }

//
//    @Override
//    public void requestStarted() {
//        super.requestStarted();
//        Log.d(TAG, "requestStarted: running");
//        progressDialog.setMessage(" add dei statement...");
//        progressDialog.show();
//    }
//
//    @Override
//    public void onResponseReceived(String response, String urlCalled) {
//        super.onResponseReceived(response, urlCalled);
//        progressDialog.dismiss();
//
//
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(response);
//            String message = jsonObject.getString("message");
//            Log.d(TAG, "onResponse: message: " + message);
//            String status = jsonObject.getString("status");
//            Log.d(TAG, "onResponse: status: " + status);
//            JSONObject dataObject = jsonObject.getJSONObject("data");
//            Log.d(TAG, "onResponse: data: size: " + dataObject.length());
//            int id = dataObject.getInt("id");
//            Log.d(TAG, "onResponse: id " + id);
//            int dei_statement = dataObject.getInt("dei_statement");
//            Log.d(TAG, "onResponse: dei_statement " + dei_statement);
//            int men = dataObject.getInt("men");
//            Log.d(TAG, "onResponse: men " + men);
//            int women = dataObject.getInt("women");
//            Log.d(TAG, "onResponse: women " + women);
//            int non_binary = dataObject.getInt("non_binary");
//            Log.d(TAG, "onResponse: non_binary " + non_binary);
//
//
//            Log.d(TAG, "onResponseReceived: response: " + response);
//            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void requestEndedWithError(VolleyError error) {
//        super.requestEndedWithError(error);
//        progressDialog.dismiss();
//        Log.d(TAG, "requestEndedWithError: error: " + error);
//    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(DEIStatement2.this, Lead.class);

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

        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}