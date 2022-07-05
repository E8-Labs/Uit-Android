package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.io.Serializable;
import java.util.ArrayList;

public class GenderProportion extends BaseActivity {

    private static final String TAG = GenderProportion.class.getSimpleName();
    ImageView backIcon, redNoah;
    TextView next, notSure;
    RangeSeekBar menSeekBar, womenSeekBar, nonBinarySeekBar;
    int men = 0, women = 0, nonBinary = 0;
    ArrayList<ModelCompanySize> industriesList;

    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "",
            passwordValue = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "",
            trainingValue = "", genderValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_proportion);

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
        menSeekBar = findViewById(R.id.men_seekbar);
        womenSeekBar = findViewById(R.id.women_seekbar);
        nonBinarySeekBar = findViewById(R.id.non_binary_meter);
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
            Log.d(TAG, "getIntentData: training: " + trainingValue);

            Bundle bundle = getIntent().getExtras();
            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        menSeekBar.setRange(0, 100);
        womenSeekBar.setRange(0, 100);
        nonBinarySeekBar.setRange(0, 100);

        menSeekBar.setIndicatorTextDecimalFormat("0");
        womenSeekBar.setIndicatorTextDecimalFormat("0");
        nonBinarySeekBar.setIndicatorTextDecimalFormat("0");
        loadProfile(GenderProportion.this, encodedImageData, redNoah);
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

                if (men > 0 || women > 0 || nonBinary > 0) {

                    Intent intent = new Intent(GenderProportion.this, GenderOrientation.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    openNextScreen();
                    Toast.makeText(GenderProportion.this, "Please select gender proportion", Toast.LENGTH_SHORT).show();
                }
            }
        });


        notSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GenderProportion.this, GenderOrientation.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        menSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                men = (int) leftValue;
                Log.d(TAG, "menSeekbar: onRangeChanged: value: " + men);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


        womenSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                women = (int) leftValue;
                Log.d(TAG, "menSeekbar: onRangeChanged: value: " + women);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


        nonBinarySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                nonBinary = (int) leftValue;
                Log.d(TAG, "menSeekbar: onRangeChanged: value: " + nonBinary);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(GenderProportion.this, GenderOrientation.class);

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
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}