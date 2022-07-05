package com.antizon.uit_android.company.welcome;

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

public class Website extends BaseActivity {
    private static final String TAG = Website.class.getSimpleName();

    ImageView backIcon,redNoah2;
    TextView next;
    EditText urlText;
    String urlTextValue;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "",
            companyNameHintValue = "", encodedImageData = "", sizeValue = ""
            ,headquarterValue="",websiteValue="www.google.com";
    private ArrayList<ModelCompanySize> industriesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        setIds();
        getIntentData();
        initialize();

        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        urlText = findViewById(R.id.urlText);

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

            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);
            Log.d(TAG, "getIntentData: stageName: "+selectedStageName);
            Log.d(TAG, "getIntentData: stageId: "+selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: "+headquarterValue);

            Bundle bundle = getIntent().getExtras();
                industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(Website.this, encodedImageData, redNoah2);
        industriesList = new ArrayList<>();
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
                hideSoftKeyboard(Website.this, urlText);
                if (!validate()) {

                } else {

                    openNextScreen();
                }
            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        urlTextValue = urlText.getText().toString().trim();

        boolean valid = true;
        if (urlTextValue.isEmpty()) {
            urlText.setError("Please enter company website");
            valid = false;
        }
        return valid;
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(Website.this, EmailAddress.class);

        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" +selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtra("size", sizeValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}