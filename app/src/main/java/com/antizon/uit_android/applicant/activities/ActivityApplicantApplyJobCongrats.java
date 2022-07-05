package com.antizon.uit_android.applicant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

public class ActivityApplicantApplyJobCongrats extends AppCompatActivity {
    Context context;

    String  jobMessage = "Your application was successfully \n submitted to ";

    TextView browseOtherJob;
    ApplicantHomeJobDataModel jobDataModel;
    RoundedImageView companyImage;
    TextView text_jobSuccessfullySubmitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_apply_job_congrats);
        Utilities.setWhiteBars(ActivityApplicantApplyJobCongrats.this);

        context = ActivityApplicantApplyJobCongrats.this;

        jobDataModel = getIntent().getParcelableExtra("jobDataModel");

        browseOtherJob = findViewById(R.id.browseOtherJob);
        companyImage = findViewById(R.id.companyImage);
        text_jobSuccessfullySubmitted = findViewById(R.id.text_jobSuccessfullySubmitted);

        browseOtherJob.setOnClickListener(view -> onBackPressed());

        if (jobDataModel.getCompanyDataModel() !=null){
            Utilities.loadImage(context, jobDataModel.getCompanyDataModel().getProfile_image(), companyImage);
            jobMessage = jobMessage + jobDataModel.getCompanyDataModel().getName();
            text_jobSuccessfullySubmitted.setText(jobMessage);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}