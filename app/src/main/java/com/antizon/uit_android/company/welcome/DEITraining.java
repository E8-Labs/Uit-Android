package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;

import java.io.Serializable;
import java.util.ArrayList;

public class DEITraining extends BaseActivity {

    private static final String TAG = DEITraining.class.getSimpleName();
    ImageView backIcon, redNoah;
    TextView yes, no, skip;
    ArrayList<ModelCompanySize> industriesList;

    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "", passwordValue = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "";
    ConstraintLayout yesLayout, noLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deitraining);
        setIds();
        getIntentData();
        initialize();

        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        yes = findViewById(R.id.yes);
        skip = findViewById(R.id.skip);
        yesLayout = findViewById(R.id.yesLayout);
        noLayout = findViewById(R.id.noLayout);
        no = findViewById(R.id.no);
        redNoah = findViewById(R.id.redNoah);

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

            Log.d(TAG, "getIntentData: image: " + encodedImageData);
            Log.d(TAG, "getIntentData: companyName: " + companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: " + typeHereValue);
            Log.d(TAG, "getIntentData: stageName: " + selectedStageName);
            Log.d(TAG, "getIntentData: stageId: " + selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: " + headquarterValue);
            Log.d(TAG, "getIntentData: website: " + websiteValue);
            Log.d(TAG, "getIntentData: email: " + emailValue);
            Log.d(TAG, "getIntentData: deiStatement: " + deiStatementValue);
            Log.d(TAG, "getIntentData: lead: " + leadValue);
            Log.d(TAG, "getIntentData: erg: " + ergValue);
            Log.d(TAG, "getIntentData: programming: " + programmingValue);

            Bundle bundle = getIntent().getExtras();

            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(DEITraining.this, encodedImageData, redNoah);

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

        yesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setYesLayout();

            }
        });
        noLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setNoLayout();

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextScreen();
            }
        });
    }


    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(DEITraining.this, GenderProportion.class);

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
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    void setYesLayout() {

        yesLayout.setBackground(getResources().getDrawable(R.drawable.login_background));
        noLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        yes.setTextColor(getResources().getColor(R.color.white));
        no.setTextColor(getResources().getColor(R.color.black));


    }

    void setNoLayout() {

        yesLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        noLayout.setBackground(getResources().getDrawable(R.drawable.login_background));

        yes.setTextColor(getResources().getColor(R.color.black));
        no.setTextColor(getResources().getColor(R.color.white));

    }
}