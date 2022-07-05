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

public class Headquaters extends BaseActivity {
    private static final String TAG = Headquaters.class.getSimpleName();
    ImageView backIcon,redNoah2;
    TextView next;
    EditText californiaText;
    String californiaTextValue;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = ""
            ,headquarterValue="San Jose, California";
    ArrayList<ModelCompanySize> industriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headquaters);

        setIds();
        getIntentData();
        initialize();

        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        californiaText = findViewById(R.id.californiaText);

    }

    void getIntentData() {
        Bundle b = getIntent().getExtras();
        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            selectedStageId = getIntent().getStringExtra("stageId");
            selectedStageName = getIntent().getStringExtra("stageName");
            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);
            Log.d(TAG, "getIntentData: stageName: "+selectedStageName);
            Log.d(TAG, "getIntentData: stageId: "+selectedStageId);

            sizeValue = getIntent().getStringExtra("size");

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                industriesList = bundle.getParcelableArrayList("industries");
                Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());
            }
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(Headquaters.this, encodedImageData, redNoah2);
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
                hideSoftKeyboard(Headquaters.this, californiaText);
                if (!validate()) {

                } else {

                    openNextScreen();
                }

            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        californiaTextValue = californiaText.getText().toString().trim();

        boolean valid = true;
        if (californiaTextValue.isEmpty()) {
            californiaText.setError("Please enter company headquarters");
            valid = false;
        }
        return valid;
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(Headquaters.this, Website.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" +selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtra("size", sizeValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}