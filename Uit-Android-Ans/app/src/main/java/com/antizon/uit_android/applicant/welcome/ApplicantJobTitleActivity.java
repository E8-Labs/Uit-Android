package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantSelectJobActivity;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class ApplicantJobTitleActivity extends AppCompatActivity {
    Context context;

    ImageView backIcon, menYellow;

    TextView next, text_jobTitle;

    String selectedJobTitle = "", selectDobValue = "", phoneValue = "", emailAddressEditTextValue = "",   applicantNameValue = "", encodedImageData = "";

    ApplicantJobDataModel applicantJobDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_title);
        Utilities.setWhiteBars(ApplicantJobTitleActivity.this);
        context = ApplicantJobTitleActivity.this;
        initViews();
    }

    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        menYellow = findViewById(R.id.menYellow);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            phoneValue = getIntent().getStringExtra("phoneNumber");
            selectDobValue = getIntent().getStringExtra("dob");

            Utilities.loadCircleImage(ApplicantJobTitleActivity.this, encodedImageData, menYellow);
        }

        backIcon.setOnClickListener(v -> onBackPressed());

        text_jobTitle.setOnClickListener(v -> {
            Intent selectJobIntent = new Intent(context, ApplicantSelectJobActivity.class);
            onSelectedJobTitleLauncher.launch(selectJobIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        next.setOnClickListener(v -> {
            if (selectedJobTitle.isEmpty()){
                CustomCookieToast.showRequiredToast(ApplicantJobTitleActivity.this, "Please select your job");
            }else {
                openNextScreen();
            }
        });
    }


    private void openNextScreen() {
        Intent intent = new Intent(ApplicantJobTitleActivity.this, ApplicantPasswordActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        intent.putExtra("email", emailAddressEditTextValue);
        intent.putExtra("phoneNumber", phoneValue);
        intent.putExtra("dob", selectDobValue);
        intent.putExtra("jobTitle", selectedJobTitle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onSelectedJobTitleLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                applicantJobDataModel = intent.getParcelableExtra("applicantJobDataModel");
                selectedJobTitle = applicantJobDataModel.getName();
                text_jobTitle.setText(selectedJobTitle);
            }
        }
    });
}