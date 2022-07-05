package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;

import java.util.ArrayList;

public class Congratulation extends BaseActivity {
    private static final String TAG = Congratulation.class.getSimpleName();

    TextView logout;
    ProgressDialog progressDialog;
    ArrayList<ModelCompanySize> industriesList;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "", passwordValue = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "", genderValue = "", orientationValue = "", inviteValue = "", summaryValue = "", congratulationValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        setIds();
        initialize();
        setListener();
        getIntentData();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        logout = findViewById(R.id.logout);


    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Congratulation.this);
    }


    void getIntentData() {

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
        inviteValue = getIntent().getStringExtra("invite");
        summaryValue = getIntent().getStringExtra("summary");
//
//        Bundle bundle = getIntent().getExtras();
////        industriesList = (ArrayList<ModelCompanySize>) bundle.getSerializable("industries");

        Bundle bundle = getIntent().getExtras();
        industriesList = bundle.getParcelableArrayList("industries");
        Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

    }

    void setListener() {
        Log.d(TAG, "setListener: ");


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Congratulation.this, SignInActivity.class);
                startActivity(intent);
                progressDialog.setMessage("Logging out...");
                progressDialog.show();
            }
        });

    }

//    void openNextScreen() {
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("industries", (Serializable) industriesList);
//
//        Intent intent = new Intent(Congratulation.this, MainActivity.class);
//
//        intent.putExtra("profilePic", encodedImageData);
//        intent.putExtra("companyName", companyNameHintValue);
//        intent.putExtra("bio", typeHereValue);
//        intent.putExtra("stageId", "" + selectedStageId);
//        intent.putExtra("stageName", selectedStageName);
//        intent.putExtra("headquarter", headquarterValue);
//        intent.putExtra("website", websiteValue);
//        intent.putExtra("email", emailValue);
//        intent.putExtra("verification", verificationValue);
//        intent.putExtra("password", passwordValue);
//        intent.putExtra("deiStatement", deiStatementValue);
//        intent.putExtra("lead", leadValue);
//        intent.putExtra("erg", ergValue);
//        intent.putExtra("programming", programmingValue);
//        intent.putExtra("training", trainingValue);
//        intent.putExtra("genderProportion", genderValue);
//        intent.putExtra("genderOrientation", orientationValue);
//        intent.putExtra("invite", inviteValue);
//        intent.putExtra("summary", summaryValue);
//        intent.putExtra("congratulation", congratulationValue);
//        intent.putExtras(bundle);
//
//        startActivity(intent);
//        overridePendingTransition(R.anim.left_in, R.anim.left_out);
//    }
}
