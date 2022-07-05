package com.antizon.uit_android.generic.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.activities.JobPostingReview;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.ExperienceAdapter;
import com.antizon.uit_android.generic.adapter.SkillAdapter;
import com.antizon.uit_android.generic.model.ModelExperience;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JobPostingExperience extends BaseActivity {
    private static final String TAG = JobPostingExperience.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    DatePicker datePicker;
    String month, day, selectYear, selectDobValue = "";
    ConstraintLayout addExperienceLayout, requiredLayout, preferLayout, datePickerLayout;

    ImageView companyIV,backIcon;
    TextView companyTitle, salaryRange, cityState, editJobPost, addNewExperience, addNewSkill, cancelAddingExperience,
            next, applicationDeadLine, addExperienceBottomSheet, addSkill, cancelSkill;
    String jobTitleValue = "", industryValue = "", departmentValue = "", cityStateValue = "",
            locationStatusValue = "", employmentStatusValue = "", minSalaryValue = "",
            maxSalaryValue = "", salaryRangeValue = "", minEducationRequiredValue = "",
            languageRequirementValue = "", vaccinationRequirementValue = "", roleValue = "",
            responsibilitiesValue = "", experienceValue = "", yearsValue = ""
            , priorityValue = "Required", skillValue = "",deadLineValue="";

    EditText experienceET, yearsET, skillET;
    CheckBox requiredCB, preferCB;

    RecyclerView experienceRecyclerView;
    ExperienceAdapter experienceAdapter;
    private List<ModelExperience> list;
    private ModelExperience dataModel;

    RecyclerView skillRecyclerView;
    SkillAdapter skillAdapter;
    private List<ModelExperience> skillList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_experience);

        setIds();
        getIntentData();
        initialize();
        setListener();

        setUpExperienceRecyclerview();
        setUpSkillRecyclerview();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        addNewExperience = findViewById(R.id.addNewExperience);
        addNewSkill = findViewById(R.id.addNewSkill);

        applicationDeadLine = findViewById(R.id.applicationDeadLine);
        datePickerLayout = findViewById(R.id.datePickerLayout);
        datePicker = findViewById(R.id.datePicker);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        companyIV = findViewById(R.id.companyIV);
        companyTitle = findViewById(R.id.companyTitle);
        salaryRange = findViewById(R.id.salaryRange);
        cityState = findViewById(R.id.cityState);
        editJobPost = findViewById(R.id.editJobPost);

        experienceRecyclerView = findViewById(R.id.experienceRecyclerView);
        skillRecyclerView = findViewById(R.id.skillRecyclerView);
    }

    void getIntentData() {

        if (getIntent() != null) {
            jobTitleValue = getIntent().getStringExtra("jobTitle");
            industryValue = getIntent().getStringExtra("industryTitle");
            departmentValue = getIntent().getStringExtra("department");
            cityStateValue = getIntent().getStringExtra("cityState");
            locationStatusValue = getIntent().getStringExtra("locationStatus");
            employmentStatusValue = getIntent().getStringExtra("employmentType");
            minSalaryValue = getIntent().getStringExtra("minSalary");
            maxSalaryValue = getIntent().getStringExtra("maxSalary");
            salaryRangeValue = getIntent().getStringExtra("salaryRange");
            minEducationRequiredValue = getIntent().getStringExtra("minEducationRequired");
            languageRequirementValue = getIntent().getStringExtra("languageRequirement");
            vaccinationRequirementValue = getIntent().getStringExtra("vaccinationRequirement");
            roleValue = getIntent().getStringExtra("role");
            responsibilitiesValue = getIntent().getStringExtra("responsibilities");
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        sessionManagement = new SessionManagement(JobPostingExperience.this);
        progressDialog = new ProgressDialog(JobPostingExperience.this);

        list = new ArrayList<>();
        skillList = new ArrayList<>();
        if (getIntent() != null) {
            companyTitle.setText(jobTitleValue);
            salaryRange.setText(salaryRangeValue);
            cityState.setText(cityStateValue);

            showImageUsingGlide(JobPostingExperience.this, companyIV, sessionManagement.getProfileImage());
        }
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                } else {
                    openNextActivity();
                }
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        applicationDeadLine.setOnClickListener(view -> datePickerLayout.setVisibility(View.VISIBLE));

        datePickerLayout.setOnClickListener(view -> datePickerLayout.setVisibility(View.GONE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                Log.d(TAG, "onDateChanged: " + monthOfYear + "/" + dayOfMonth + "/" + year);

                month = "" + (monthOfYear + 1);
                if (month.length() == 1) {

                    month = "0" + month;
                }

                day = "" + (dayOfMonth);
                if (day.length() == 1) {

                    day = "0" + day;
                }

                selectYear = "" + (year);
                applicationDeadLine.setText(day + "-" + month + "-" + selectYear);
            });
        }

        addNewExperience.setOnClickListener(view -> addExperienceBottomSheet());

        addNewSkill.setOnClickListener(view -> addSkillBottomSheet());
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        selectDobValue = applicationDeadLine.getText().toString().trim();

        boolean valid = true;
        if (selectDobValue.isEmpty()) {
            applicationDeadLine.setText("");
        } else {
            try {

                Calendar c = Calendar.getInstance();
                android.icu.text.SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
                String formattedDate = df.format(c.getTime());
                String[] currentStartDate = formattedDate.split("-");
                String[] currentYearDate = currentStartDate[2].split(" ");

                if (currentYearDate[0] != null) {
                    Log.d(TAG, "validate: split: year: " + currentYearDate[0].trim());

                    if (Integer.parseInt(currentYearDate[0].trim()) <= Integer.parseInt(selectYear)) {

                        Toast.makeText(this, "Please enter a valid deadline", Toast.LENGTH_SHORT).show();
                        valid = false;
                    }
                }

            } catch (ArrayIndexOutOfBoundsException exception) {

                Toast.makeText(this, "Please set deadline", Toast.LENGTH_SHORT).show();
                valid = false;

            }

        }
        return valid;
    }

    void setUpExperienceRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobPostingExperience.this);
        experienceRecyclerView.setLayoutManager(linearLayoutManager);

        experienceAdapter = new ExperienceAdapter(list, JobPostingExperience.this,
                new ExperienceAdapter.SelectionListener() {
                    @Override
                    public void delete(int position) {
                        list.remove(position);
                        experienceAdapter.notifyDataSetChanged();
                    }
                });
        experienceRecyclerView.setAdapter(experienceAdapter);
    }

    void setUpSkillRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobPostingExperience.this);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(JobPostingExperience.this, 2);
        skillRecyclerView.setLayoutManager(linearLayoutManager);

        skillAdapter = new SkillAdapter(skillList, JobPostingExperience.this,
                new SkillAdapter.SelectionListener() {
                    @Override
                    public void delete(int position) {
                        skillList.remove(position);
                        skillAdapter.notifyDataSetChanged();
                    }
                });
        skillRecyclerView.setAdapter(skillAdapter);
    }


    void addExperienceBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.job_posting_experience);

        experienceValue = "";
        yearsValue = "";
        priorityValue = "";

        experienceET = bottomSheetDialog.findViewById(R.id.experienceET);
        yearsET = bottomSheetDialog.findViewById(R.id.yearsET);

        requiredLayout = bottomSheetDialog.findViewById(R.id.requiredLayout);
        requiredCB = bottomSheetDialog.findViewById(R.id.required_checkbox);
        preferLayout = bottomSheetDialog.findViewById(R.id.preferredLayout);
        preferCB = bottomSheetDialog.findViewById(R.id.prefer_checkbox);
        cancelAddingExperience = bottomSheetDialog.findViewById(R.id.cancelAddingExperience);
        addExperienceBottomSheet = bottomSheetDialog.findViewById(R.id.addExperience);


        cancelAddingExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        requiredLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requiredCB.setChecked(true);
                preferCB.setChecked(false);
                priorityValue = "Required";
            }
        });

        preferLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requiredCB.setChecked(false);
                preferCB.setChecked(true);
                priorityValue = "Preferred";
            }
        });

        addExperienceBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateExperience()) {
                } else {
                    dataModel = new ModelExperience();
                    dataModel.setYears(yearsValue);
                    dataModel.setExperience(experienceValue);
                    dataModel.setPriority(priorityValue);
                    list.add(dataModel);

                    bottomSheetDialog.dismiss();
                }
                experienceAdapter.notifyDataSetChanged();
            }
        });
        bottomSheetDialog.show();
    }

    public boolean validateExperience() {
        Log.d(TAG, "validate: ");
        experienceValue = experienceET.getText().toString().trim();
        yearsValue = yearsET.getText().toString().trim();

        boolean valid = true;
        if (experienceValue.isEmpty()) {
            experienceET.setError("Please enter required experience");
            valid = false;
        }
        if (yearsValue.isEmpty()) {
            yearsET.setError("Please enter experience years");
            valid = false;
        }

        if (requiredCB.isChecked()) {
            priorityValue = "Required";
        } else if (preferCB.isChecked()) {
            priorityValue = "Preferred";
        }


        return valid;
    }


    void addSkillBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.layout_skill);

        skillValue = "";

        skillET = bottomSheetDialog.findViewById(R.id.skillET);
        cancelSkill = bottomSheetDialog.findViewById(R.id.cancelSkill);
        addSkill = bottomSheetDialog.findViewById(R.id.addSkill);


        cancelSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateSkill()) {
                } else {
                    dataModel = new ModelExperience();
                    dataModel.setSkill(skillValue);
                    skillList.add(dataModel);

                    bottomSheetDialog.dismiss();
                }
                skillAdapter.notifyDataSetChanged();
            }
        });
        bottomSheetDialog.show();
    }

    public boolean validateSkill() {
        Log.d(TAG, "validate: ");
        skillValue = skillET.getText().toString().trim();

        boolean valid = true;
        if (skillValue.isEmpty()) {
            skillET.setError("Please enter Skill");
            valid = false;
        }

        return valid;
    }


    void openNextActivity() {

        Intent intent = new Intent(JobPostingExperience.this, JobPostingReview.class);
        intent.putExtra("jobTitle", jobTitleValue);
        intent.putExtra("industryTitle", industryValue);
        intent.putExtra("department", departmentValue);
        intent.putExtra("cityState", cityStateValue);
        intent.putExtra("locationStatus", locationStatusValue);
        intent.putExtra("employmentType", employmentStatusValue);
        intent.putExtra("minSalary", minSalaryValue);
        intent.putExtra("maxSalary", maxSalaryValue);
        intent.putExtra("salaryRange", salaryRangeValue);
        intent.putExtra("minEducationRequired", minEducationRequiredValue);
        intent.putExtra("languageRequirement", languageRequirementValue);
        intent.putExtra("vaccinationRequirement", vaccinationRequirementValue);
        intent.putExtra("role", roleValue);
        intent.putExtra("responsibilities", responsibilitiesValue);
        intent.putExtra("applicationDeadLine", deadLineValue);
        intent.putExtra("experienceET", experienceValue);
        intent.putExtra("yearsET", yearsValue);
        intent.putExtra("skillET", skillValue);
        startActivity(intent);
    }


}