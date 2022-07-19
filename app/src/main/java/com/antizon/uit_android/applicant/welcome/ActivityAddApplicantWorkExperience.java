package com.antizon.uit_android.applicant.welcome;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.ApplicantReferenceAdapter;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;
import com.antizon.uit_android.models.work.ApplicantReferenceModel;
import com.antizon.uit_android.models.work.ApplicantWorkExperienceModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class ActivityAddApplicantWorkExperience extends AppCompatActivity implements ApplicantReferenceAdapter.ApplicantReferenceAdapterCallBack {
    private DatePickerDialog.OnDateSetListener startDateListener, endDateListener;
    
    boolean isCurrentlySelected;

    Context context;

    TextView btnSave, text_addReference, text_startYear, text_endYear, text_degreeName, btn_selectSkills;
    EditText editText_companyName, editText_schoolName, editText_responsibilities;
    RelativeLayout btnBack, btn_selectStartYear, btn_selectEndYear, btnSelectDegree;
    LinearLayout btn_currentlyWorking;
    ImageView checked_ic;
    RecyclerView recyclerview_references;
    List<ApplicantReferenceModel> referencesList;
    ApplicantReferenceAdapter applicantReferenceAdapter;

    TagContainerLayout tagContainerLayout;
    List<SkillDataModel> skillsList;

    AlertDialog deleteDialog;

    ApplicantDegreeDataModel degreeDataModel;

    String type;
    ApplicantWorkExperienceModel applicantWorkExperienceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_applicant_work_experience);
        context = ActivityAddApplicantWorkExperience.this;
        Utilities.setCustomStatusAndNavColor(ActivityAddApplicantWorkExperience.this, R.color.white_dash, R.color.white_dash);
        initViews();
    }

    private void initViews(){
        referencesList = new ArrayList<>();
        skillsList = new ArrayList<>();

        type = getIntent().getStringExtra("type");

        if (type.equals("edit")){
            applicantWorkExperienceModel = getIntent().getParcelableExtra("applicantWorkExperience");
        }

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        tagContainerLayout = findViewById(R.id.tag_container_skills);
        text_addReference = findViewById(R.id.text_addReference);
        recyclerview_references = findViewById(R.id.recyclerview_references);
        text_startYear = findViewById(R.id.text_startYear);
        text_endYear = findViewById(R.id.text_endYear);
        editText_companyName = findViewById(R.id.editText_companyName);
        editText_schoolName = findViewById(R.id.editText_schoolName);
        editText_responsibilities = findViewById(R.id.editText_responsibilities);
        btn_selectSkills = findViewById(R.id.btn_selectSkills);
        btn_selectStartYear = findViewById(R.id.btn_selectStartYear);
        btn_selectEndYear = findViewById(R.id.btn_selectEndYear);
        btnSelectDegree = findViewById(R.id.btnSelectDegree);
        btn_currentlyWorking = findViewById(R.id.btn_currentlyWorking);
        checked_ic = findViewById(R.id.checked_ic);
        text_degreeName = findViewById(R.id.text_degreeName);

        if (type.equals("edit")){
            degreeDataModel = applicantWorkExperienceModel.getDegreeDataModel();
            referencesList = applicantWorkExperienceModel.getReferencesList();

            editText_companyName.setText(applicantWorkExperienceModel.getCompanyName());
            editText_schoolName.setText(applicantWorkExperienceModel.getJobTitle());


            text_startYear.setText(applicantWorkExperienceModel.getStartDate());
            if (applicantWorkExperienceModel.getEndDate().equals("present")){
                isCurrentlySelected = true;
                text_endYear.setText("");
                btn_selectEndYear.setEnabled(false);
                checked_ic.setImageResource(R.drawable.checked_ic);
            }else {
                text_endYear.setText(applicantWorkExperienceModel.getEndDate());
                btn_selectEndYear.setEnabled(true);
                checked_ic.setImageResource(R.drawable.not_checked_ic);
                isCurrentlySelected = false;
            }

            editText_responsibilities.setText(applicantWorkExperienceModel.getResponsibilities());
            text_degreeName.setText(degreeDataModel.getName());

            skillsList = applicantWorkExperienceModel.getSkillsList();
            if (skillsList.size() == 0){
                tagContainerLayout.setVisibility(View.GONE);
            }else{
                for (int i = 0; i < skillsList.size(); i++) {
                    tagContainerLayout.addTag(skillsList.get(i).getName());
                }
                tagContainerLayout.setVisibility(View.VISIBLE);
            }
        }


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


        btn_selectSkills.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectSkillsActivity.class);
            onSelectedLauncher.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnSelectDegree.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectDepartmentActivity.class);
            onSelectedCategory.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });


        text_addReference.setOnClickListener(v -> {
            Utilities.hideKeyboard(v, context);
            Intent addEducationIntent = new Intent(context, AddApplicantReferenceActivity.class);
            onApplicantAddReference.launch(addEducationIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        showAllReferencesRecyclerview(recyclerview_references, referencesList);

        setDateListenerListeners();

        btn_currentlyWorking.setOnClickListener(v -> checkForCurrentlyEnabled());

        btnSave.setOnClickListener(v -> {
            String companyName = editText_companyName.getText().toString();
            String jobTitle = editText_schoolName.getText().toString();
            String startDate = text_startYear.getText().toString();
            String endDate = text_endYear.getText().toString();
            String responsibilities = editText_responsibilities.getText().toString();
            String degreeName = text_degreeName.getText().toString();

            if (isCurrentlySelected){
                if (companyName.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter company name");
                }else if (jobTitle.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter job title");
                }else if (startDate.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please select start date");
                }else if (responsibilities.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter responsibilities");
                }else if (degreeName.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please select degree name");
                }else if (skillsList.size() == 0){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter skills");
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("applicantWorkExperience", new ApplicantWorkExperienceModel(companyName, jobTitle, startDate, "present", responsibilities, degreeDataModel, referencesList, skillsList));
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
            }else {
                if (companyName.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter company name");
                }else if (jobTitle.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter job title");
                }else if (startDate.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please select start date");
                }else if (endDate.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please select end date");
                }else if (responsibilities.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter responsibilities");
                }else if (degreeName.isEmpty()){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please select degree name");
                }else if (skillsList.size() == 0){
                    CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please enter skills");
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("applicantWorkExperience", new ApplicantWorkExperienceModel(companyName, jobTitle, startDate, endDate, responsibilities, degreeDataModel, referencesList, skillsList));
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
            }

        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onApplicantAddReference = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent !=null){
                ApplicantReferenceModel applicantReferenceModel = intent.getParcelableExtra("applicantReferenceModel");
                referencesList.add(applicantReferenceModel);
                applicantReferenceAdapter.notifyDataSetChanged();
            }
        }
    });

    private void showAllReferencesRecyclerview(RecyclerView recyclerView, List<ApplicantReferenceModel> referencesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        applicantReferenceAdapter = new ApplicantReferenceAdapter(context, referencesList , this);
        recyclerView.setAdapter(applicantReferenceAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    @Override
    public void onRemoveClicked(int position) {
        showRemoveReferencePopup(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showRemoveReferencePopup(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddApplicantWorkExperience.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ActivityAddApplicantWorkExperience.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);


        String deleteTest = "Delete Reference?";
        String areYouSure = "Are you sure you want to delete this reference";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            referencesList.remove(position);
            applicantReferenceAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    ActivityResultLauncher<Intent> onSelectedCategory = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                degreeDataModel = intent.getParcelableExtra("degreeDataModel");
                text_degreeName.setText(degreeDataModel.getName());
            }
        }
    });

    ActivityResultLauncher<Intent> onSelectedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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

    @SuppressLint("ClickableViewAccessibility")
    private void setDateListenerListeners(){
        btn_selectStartYear.setOnTouchListener((View view, MotionEvent motionEvent) -> {
            Utilities.hideKeyboard(btn_selectStartYear, context);
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(ActivityAddApplicantWorkExperience.this, startDateListener, year, month, day);
                    
                    dialog.show();
                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return false;
        });

        startDateListener = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String startDate = Utilities.getMonthNameByNumber(month) + " "  + year;

            text_startYear.setText(startDate);
        };

        btn_selectEndYear.setOnTouchListener((View view, MotionEvent motionEvent) -> {
            Utilities.hideKeyboard(btn_selectStartYear, context);
            String startDate = text_startYear.getText().toString();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (startDate.isEmpty()){
                        CustomCookieToast.showRequiredToast(ActivityAddApplicantWorkExperience.this, "Please select start date first");
                    }else {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(ActivityAddApplicantWorkExperience.this,
                                endDateListener,
                                year, month, day);
                        dialog.show();
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return false;
        });

        endDateListener = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String startDate = Utilities.getMonthNameByNumber(month) + " "  + year;
            text_endYear.setText(startDate);
        };
    }

    private void checkForCurrentlyEnabled(){
        if (!isCurrentlySelected){
            text_endYear.setText("");
            btn_selectEndYear.setEnabled(false);
            checked_ic.setImageResource(R.drawable.checked_ic);
            isCurrentlySelected = true;
        }else {
            btn_selectEndYear.setEnabled(true);
            checked_ic.setImageResource(R.drawable.not_checked_ic);
            isCurrentlySelected = false;
        }
    }


}