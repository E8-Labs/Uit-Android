package com.antizon.uit_android.company.activities.postjob;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.company.CompanyPostJobExperienceAdapter;
import com.antizon.uit_android.applicant.welcome.ApplicantSelectSkillsActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.filter.MultiSelectionModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;
import com.antizon.uit_android.models.company.JobExperienceModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.models.jobs.JobExperienceDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class CompanyPostJobStep3Activity extends AppCompatActivity implements CompanyPostJobExperienceAdapter.CompanyPostJobExperienceAdapterCallBack {
    private DatePickerDialog.OnDateSetListener dateListener;

    Context context;
    SessionManagement sessionManagement;

    RelativeLayout btnBack, btnSelectDate;
    RoundedImageView company_profileImage;
    TextView btnNext, text_companyName, text_salaryRange, text_location, btnAddNewExperience, btnAddNewSkill, text_date;
    TagContainerLayout tagContainerLayout;
    ArrayList<SkillDataModel> skillsList;

    RecyclerView recyclerview_experience;
    ArrayList<JobExperienceModel> experiencesList;
    CompanyPostJobExperienceAdapter experienceAdapter;

    MultiSelectionModel selectedLocation, selectedEmployment;
    ApplicantDegreeDataModel selectedIndustryModel, selectedDepartmentModel, selectedDegreeModel, selectedLanguageModel;
    double latitude, longitude;
    boolean isEducationRequired, isLanguageRequired, isVaccinationRequired;
    String jobTitle, jobLocation, city, state, jobRole, jobResponsibilities,
            applicationDeadLineDate = "", from;
    int minSalary, maxSalary;

    AlertDialog deleteDialog;

    JobDetailsDataModel jobDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_post_job_step3);
        Utilities.setWhiteBars(CompanyPostJobStep3Activity.this);
        context = CompanyPostJobStep3Activity.this;
        sessionManagement = new SessionManagement(context);

        initViews();


    }

    private void initViews(){
        experiencesList = new ArrayList<>();
        skillsList = new ArrayList<>();

        getIntentData();

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        company_profileImage = findViewById(R.id.company_profileImage);
        text_companyName = findViewById(R.id.text_companyName);
        text_salaryRange = findViewById(R.id.text_salaryRange);
        text_location = findViewById(R.id.text_location);
        btnAddNewExperience = findViewById(R.id.btnAddNewExperience);
        recyclerview_experience = findViewById(R.id.recyclerview_experience);
        btnAddNewSkill = findViewById(R.id.btnAddNewSkill);
        tagContainerLayout = findViewById(R.id.tag_container_skills);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        text_date = findViewById(R.id.text_date);

        btnBack.setOnClickListener(v -> onBackPressed());
        Utilities.loadImage(context, sessionManagement.getProfileImage(), company_profileImage);

        String salaryRange = "$" + minSalary+ "k - $" + maxSalary + "k";
        text_companyName.setText(sessionManagement.getUserName());
        text_salaryRange.setText(salaryRange);
        text_location.setText(jobLocation);

        btnAddNewExperience.setOnClickListener(v -> {
            Intent addExperienceIntent = new Intent(context,  AddJobExperienceActivity.class);
            onExperienceAddedLauncher.launch(addExperienceIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnAddNewSkill.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectSkillsActivity.class);
            onSelectedSkillLauncher.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {
            }

            @Override
            public void onSelectedTagDrag(int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
                if (skillsList != null && skillsList.size() > position) {
                    tagContainerLayout.removeTag(position);
                    skillsList.remove(position);

                    if (skillsList.size() == 0){
                        tagContainerLayout.setVisibility(View.GONE);
                    }else{
                        tagContainerLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        setDateListener();


        btnNext.setOnClickListener(v -> {
            if (experiencesList.size() == 0){
                CustomCookieToast.showRequiredToast(CompanyPostJobStep3Activity.this, "Please add minimum one experience");
            }else if (skillsList.size() == 0){
                CustomCookieToast.showRequiredToast(CompanyPostJobStep3Activity.this, "Please add minimum one skill");
            }else {
                moveToNextScreen(experiencesList, skillsList, applicationDeadLineDate);
            }
        });

        if (from.equals("edit")){
            if (jobDetails.getDeadline() !=null){
                text_date.setText(jobDetails.getDeadline());
            }

            if (jobDetails.getSkills() !=null){
                tagContainerLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < jobDetails.getSkills().size(); i++) {
                    SkillDataModel skillDataModel = new SkillDataModel(jobDetails.getSkills().get(i).getId(), jobDetails.getSkills().get(i).getName());
                    skillsList.add(skillDataModel);
                    tagContainerLayout.addTag(skillDataModel.getName());
                }
            }

            if (jobDetails.getExperience() !=null){
                for (int i = 0; i < jobDetails.getExperience().size(); i++) {
                    JobExperienceDataModel jobExperienceDataModel = jobDetails.getExperience().get(i);
                    JobExperienceModel jobExperienceModel = new JobExperienceModel(
                            new ApplicantDegreeDataModel(jobExperienceDataModel.getIndustry().getId(), jobExperienceDataModel.getIndustry().getName(), "", "", ""),
                            jobExperienceDataModel.getYears() + "", jobExperienceDataModel.getRequirementStatus() == 1);
                    experiencesList.add(jobExperienceModel);
                }
            }

        }

        showAllExperiencesRecyclerview(recyclerview_experience, experiencesList);

    }

    private void getIntentData(){
        if (getIntent() !=null){
            from = getIntent().getStringExtra("from");

            jobTitle = getIntent().getStringExtra("jobTitle");
            selectedIndustryModel = getIntent().getParcelableExtra("jobIndustry");
            selectedDepartmentModel = getIntent().getParcelableExtra("jobDepartment");
            jobLocation = getIntent().getStringExtra("jobLocation");
            city = getIntent().getStringExtra("city");
            state = getIntent().getStringExtra("state");
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
            selectedLocation = getIntent().getParcelableExtra("selectedLocation");
            selectedEmployment = getIntent().getParcelableExtra("selectedEmployment");
            minSalary = getIntent().getIntExtra("minSalary", 0);
            maxSalary = getIntent().getIntExtra("maxSalary", 0);
            selectedDegreeModel = getIntent().getParcelableExtra("jobMinEducation");
            isEducationRequired = getIntent().getBooleanExtra("isEducationRequired", false);
            selectedLanguageModel = getIntent().getParcelableExtra("selectedLanguageModel");
            isLanguageRequired = getIntent().getBooleanExtra("isLanguageRequired", false);
            isVaccinationRequired = getIntent().getBooleanExtra("isVaccinationRequired", false);
            jobRole = getIntent().getStringExtra("jobRole");
            jobResponsibilities = getIntent().getStringExtra("jobResponsibilities");

            if (from.equals("edit")){
                jobDetails = getIntent().getParcelableExtra("jobDetail");
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDateListener(){
        btnSelectDate.setOnTouchListener((View view, MotionEvent motionEvent) -> {
            Utilities.hideKeyboard(btnSelectDate, context);
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(context,
                            //   R.style.DialogTheme,
                            dateListener,
                            year, month, day);
                    dialog.show();
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return false;
        });

        dateListener = (datePicker, year, month, day) -> {
            month = month + 1;
            applicationDeadLineDate = year + "-" + month + "-" + day;
            @SuppressLint("SimpleDateFormat")
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat")
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String inputDateStr = applicationDeadLineDate;
            Date date = null;
            try {
                date = inputFormat.parse(inputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            String outputDateStr = outputFormat.format(date);
            text_date.setText(outputDateStr);

        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onSelectedSkillLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                SkillDataModel skillDataModel = intent.getParcelableExtra("skillDataModel");
                tagContainerLayout.setVisibility(View.VISIBLE);
                tagContainerLayout.addTag(skillDataModel.getName());
                skillsList.add(skillDataModel);
            }
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onExperienceAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                JobExperienceModel jobExperienceModel = intent.getParcelableExtra("jobExperienceModel");
                experiencesList.add(jobExperienceModel);
                experienceAdapter.notifyDataSetChanged();
            }
        }
    });

    private void showAllExperiencesRecyclerview(RecyclerView recyclerView, ArrayList<JobExperienceModel> experiencesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        experienceAdapter = new CompanyPostJobExperienceAdapter(context, experiencesList , "add",this);
        recyclerView.setAdapter(experienceAdapter);
    }

    @Override
    public void onRemoveClicked(int position) {
        showRemoveExperiencePopup(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showRemoveExperiencePopup(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyPostJobStep3Activity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(CompanyPostJobStep3Activity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);


        String deleteTest = "Delete Experience?";
        String areYouSure = "Are you sure you want to delete this experience";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            experiencesList.remove(position);
            experienceAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void moveToNextScreen(ArrayList<JobExperienceModel> experiencesList, ArrayList<SkillDataModel> skillsList, String applicationDeadLineDate){
        Intent jobPostIntent = new Intent(context, CompanyPostJobReviewActivity.class);
        jobPostIntent.putExtra("from", from);
        jobPostIntent.putExtra("jobTitle", jobTitle);
        jobPostIntent.putExtra("jobIndustry", selectedIndustryModel);
        jobPostIntent.putExtra("jobDepartment", selectedDepartmentModel);
        jobPostIntent.putExtra("jobLocation", jobLocation);
        jobPostIntent.putExtra("city", city);
        jobPostIntent.putExtra("state", state);
        jobPostIntent.putExtra("latitude", latitude);
        jobPostIntent.putExtra("longitude", longitude);
        jobPostIntent.putExtra("selectedLocation", selectedLocation);
        jobPostIntent.putExtra("selectedEmployment", selectedEmployment);
        jobPostIntent.putExtra("minSalary", minSalary);
        jobPostIntent.putExtra("maxSalary", maxSalary);
        jobPostIntent.putExtra("jobMinEducation", selectedDegreeModel);
        jobPostIntent.putExtra("isEducationRequired", isEducationRequired);
        jobPostIntent.putExtra("selectedLanguageModel", selectedLanguageModel);
        jobPostIntent.putExtra("isLanguageRequired", isLanguageRequired);
        jobPostIntent.putExtra("isVaccinationRequired", isVaccinationRequired);
        jobPostIntent.putExtra("jobRole", jobRole);
        jobPostIntent.putExtra("jobResponsibilities", jobResponsibilities);
        jobPostIntent.putExtra("applicationDeadLineDate", applicationDeadLineDate);
        jobPostIntent.putParcelableArrayListExtra("experiencesList", experiencesList);
        jobPostIntent.putParcelableArrayListExtra("skillsList", skillsList);
        if (from.equals("edit")){
            jobPostIntent.putExtra("jobDetail", jobDetails);
        }
        onJobPostedLauncher.launch(jobPostIntent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onJobPostedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

}