package com.antizon.uit_android.applicant.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;

public class ApplicantWorkExperienceActivity extends AppCompatActivity {
    Context context;

    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    ImageView plus, backIcon,menYellow;

    TextView next;

    String employeValue = "", encodedImageData = "";

    ArrayList<ModelCompanySize> selectedProfessionalInterestList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInSizeList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        Utilities.setWhiteBars(ApplicantWorkExperienceActivity.this);
        context = ApplicantWorkExperienceActivity.this;
        sessionManagement = new SessionManagement(context);

        initViews();
    }


    void initViews() {

        plus = findViewById(R.id.plus);
        next = findViewById(R.id.next);
        backIcon = findViewById(R.id.backIcon);
        menYellow = findViewById(R.id.menYellow);

        encodedImageData = getIntent().getStringExtra("profilePic");
        employeValue = getIntent().getStringExtra("employeStatus");

        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
        locationList = getIntent().getParcelableArrayListExtra("locationList");
        interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");
        selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
        selectedCompanyInterestInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInSizeList");
        selectedProfessionalInterestList = getIntent().getParcelableArrayListExtra("selectedProfessionalInterestList");


        progressDialog = new ProgressDialog(ApplicantWorkExperienceActivity.this);
        Utilities.loadImage(ApplicantWorkExperienceActivity.this, sessionManagement.getProfileImage(), menYellow);

        backIcon.setOnClickListener(view -> onBackPressed());

        next.setOnClickListener(v -> {
            // First hit the api add_professional_info here
//            openNextScreen();
        });


        plus.setOnClickListener(v -> {
            Intent addEducationIntent = new Intent(context, ActivityAddApplicantWorkExperience.class);
            addEducationIntent.putExtra("type", "add");
            onWorkExperienceAddedLauncher.launch(addEducationIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

    }

    void openNextScreen() {
        Intent intent = new Intent(ApplicantWorkExperienceActivity.this, ApplicantResumeActivity.class);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onWorkExperienceAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {

            }
        }
    });
}