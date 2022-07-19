package com.antizon.uit_android.company.activities.postjob;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.welcome.SelectCompanyIndustryActivity;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.company.JobExperienceModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class AddJobExperienceActivity extends AppCompatActivity {
    Context context;

    RelativeLayout btnBack, btnSelectIndustry, btnRequired, btnPreferred;
    TextView text_industryName, btnSave;
    EditText editText_yearsExperience;
    ImageView required_radio_ic, preferred_radio_ic;

    ApplicantDegreeDataModel applicantIndustryModel;
    boolean isRequired = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_experience);
        Utilities.setWhiteBars(AddJobExperienceActivity.this);
        context = AddJobExperienceActivity.this;
        initViews();
    }

    private void initViews(){

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnSelectIndustry = findViewById(R.id.btnSelectIndustry);
        btnRequired = findViewById(R.id.btnRequired);
        btnPreferred = findViewById(R.id.btnPreferred);
        text_industryName = findViewById(R.id.text_industryName);
        editText_yearsExperience = findViewById(R.id.editText_yearsExperience);
        required_radio_ic = findViewById(R.id.required_radio_ic);
        preferred_radio_ic = findViewById(R.id.preferred_radio_ic);

        btnRequired.setOnClickListener(v -> {
            if (!isRequired){
                required_radio_ic.setImageResource(R.drawable.radio_selected_ic);
                preferred_radio_ic.setImageResource(R.drawable.radio_not_selected_ic);
                isRequired = true;
            }
        });

        btnPreferred.setOnClickListener(v -> {
            if (isRequired){
                preferred_radio_ic.setImageResource(R.drawable.radio_selected_ic);
                required_radio_ic.setImageResource(R.drawable.radio_not_selected_ic);
                isRequired = false;
            }
        });

        btnSelectIndustry.setOnClickListener(v -> {
            Intent selectIndustryIntent = new Intent(context, SelectCompanyIndustryActivity.class);
            onSelectedCategory.launch(selectIndustryIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnSave.setOnClickListener(v -> {
            String industryName = text_industryName.getText().toString();
            String totalExperience = editText_yearsExperience.getText().toString();

            if (industryName.isEmpty()){
                CustomCookieToast.showRequiredToast(AddJobExperienceActivity.this, "Please select industry");
            }else if (totalExperience.isEmpty()){
                CustomCookieToast.showRequiredToast(AddJobExperienceActivity.this, "Please enter experience");
            }else {
                Intent intent = new Intent();
                intent.putExtra("jobExperienceModel", new JobExperienceModel(applicantIndustryModel, totalExperience, isRequired));
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }


    ActivityResultLauncher<Intent> onSelectedCategory = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                applicantIndustryModel = intent.getParcelableExtra("degreeDataModel");
                text_industryName.setText(applicantIndustryModel.getName());
            }
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }
}