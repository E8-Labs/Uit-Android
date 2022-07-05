package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.Serializable;
import java.util.ArrayList;

public class Password extends BaseActivity {
    private static final String TAG = Password.class.getSimpleName();

    ImageView backIcon, redNoah2;
    TextView next;
    EditText passwordEditText;
    ArrayList<ModelCompanySize> industriesList;

    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "", passwordValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password2);

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
        passwordEditText = findViewById(R.id.passwordEditText);

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

            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);
            Log.d(TAG, "getIntentData: stageName: "+selectedStageName);
            Log.d(TAG, "getIntentData: stageId: "+selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: "+headquarterValue);
            Log.d(TAG, "getIntentData: website: "+websiteValue);
            Log.d(TAG, "getIntentData: email: " + emailValue);

            Bundle bundle = getIntent().getExtras();

            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());


        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(Password.this, encodedImageData, redNoah2);
        industriesList = new ArrayList<>();

    }


    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Password.this, passwordEditText);
                if (!validate()) {

                } else {
                    openNextScreen();
                }
            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        passwordValue = passwordEditText.getText().toString().trim();

        boolean valid = true;
        if (passwordValue.isEmpty()) {
            passwordEditText.setError("Please enter password");
            valid = false;
        }
        return valid;
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(Password.this, DEIStatement2.class);

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
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }


}

