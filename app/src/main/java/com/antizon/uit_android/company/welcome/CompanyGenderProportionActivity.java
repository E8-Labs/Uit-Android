package com.antizon.uit_android.company.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.slider.Slider;


public class CompanyGenderProportionActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    ImageView backIcon, redNoah;
    TextView next, text_genderProportion, notSure;
    Slider menSeekBar, womenSeekBar, nonBinarySeekBar;

    int totalPercentage = 0, menPercentage = 0, womenPercentage = 0, nonBinaryPercentage = 0;

    String genderProportionPercentage = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_gender_proportion);
        Utilities.setCustomStatusAndNavColor(CompanyGenderProportionActivity.this, R.color.white_dash, R.color.white_dash);
        context = CompanyGenderProportionActivity.this;
        sessionManagement = new SessionManagement(context);
        initViews();
        setListener();
    }


    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        notSure = findViewById(R.id.notSure);
        redNoah = findViewById(R.id.redNoah);
        menSeekBar = findViewById(R.id.men_seekbar);
        womenSeekBar = findViewById(R.id.women_seekbar);
        nonBinarySeekBar = findViewById(R.id.non_binary_meter);
        text_genderProportion = findViewById(R.id.text_genderProportion);

        deiStatementValue = getIntent().getStringExtra("deiStatement");
        leadValue = getIntent().getStringExtra("lead");
        ergValue = getIntent().getStringExtra("erg");
        programmingValue = getIntent().getStringExtra("programming");
        trainingValue = getIntent().getStringExtra("training");



        menSeekBar.setLabelFormatter(value -> (int) value + "%");
        womenSeekBar.setLabelFormatter(value -> (int) value + "%");
        nonBinarySeekBar.setLabelFormatter(value -> (int) value + "%");

        loadProfile(CompanyGenderProportionActivity.this, sessionManagement.getProfileImage(), redNoah);
    }


    void setListener() {

        backIcon.setOnClickListener(v -> onBackPressed());


        next.setOnClickListener(v -> {
            menPercentage = (int) (menSeekBar.getValue());
            womenPercentage = (int) (womenSeekBar.getValue());
            nonBinaryPercentage = (int) (nonBinarySeekBar.getValue());

            if (totalPercentage != 100) {
                CustomCookieToast.showRequiredToast(CompanyGenderProportionActivity.this, "Please select 100% gender proportion");
            } else {
                openNextScreen();
            }
        });


        addSliderListener(menSeekBar, womenSeekBar, nonBinarySeekBar);
        addSliderListener(womenSeekBar, menSeekBar, nonBinarySeekBar);
        addSliderListener(nonBinarySeekBar, menSeekBar, womenSeekBar);

        notSure.setOnClickListener(v -> openNextScreen());

    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyGenderProportionActivity.this, CompanyGenderOrientationActivity.class);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        intent.putExtra("erg", ergValue);
        intent.putExtra("programming", programmingValue);
        intent.putExtra("training", trainingValue);
        intent.putExtra("genderProportion", totalPercentage);
        intent.putExtra("menPercentage", menPercentage);
        intent.putExtra("womenPercentage", womenPercentage);
        intent.putExtra("nonBinaryPercentage", nonBinaryPercentage);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void addSliderListener(Slider proportionSlider, Slider firstSlider, Slider secondSlider){
        proportionSlider.addOnChangeListener((slider, value, fromUser) -> {
            totalPercentage = 0;
            totalPercentage = (int) (firstSlider.getValue() + secondSlider.getValue());
            totalPercentage = (int) (totalPercentage + value);
            genderProportionPercentage = totalPercentage + "%";
            text_genderProportion.setText(genderProportionPercentage);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}