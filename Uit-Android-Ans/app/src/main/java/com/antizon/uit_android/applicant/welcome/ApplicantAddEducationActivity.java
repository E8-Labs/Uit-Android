package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import java.util.Calendar;


public class ApplicantAddEducationActivity extends AppCompatActivity {
    Context context;
    private DatePickerDialog.OnDateSetListener startDateListener, endDateListener;

    RelativeLayout btnBack, btnSelectDegree;
    TextView text_degreeName, text_startYear, text_endYear, btnSave;
    EditText editText_fieldOfStudy, editText_schoolName;
    RelativeLayout btn_selectStartYear, btn_selectEndYear;

    ApplicantDegreeDataModel degreeDataModel;
    ApplicantEducationDataModel education;

    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_add_education);
        Utilities.setCustomStatusAndNavColor(ApplicantAddEducationActivity.this, R.color.white_dash, R.color.white_dash);
        context =  ApplicantAddEducationActivity.this;


        type = getIntent().getStringExtra("type");

        if (type.equals("edit")){
            education = getIntent().getParcelableExtra("education");
        }

        btnBack = findViewById(R.id.btnBack);
        btnSelectDegree = findViewById(R.id.btnSelectDegree);
        text_degreeName = findViewById(R.id.text_degreeName);
        editText_fieldOfStudy = findViewById(R.id.editText_fieldOfStudy);
        editText_schoolName = findViewById(R.id.editText_schoolName);
        btnSave = findViewById(R.id.btnSave);
        btn_selectStartYear = findViewById(R.id.btn_selectStartYear);
        btn_selectEndYear = findViewById(R.id.btn_selectEndYear);
        text_startYear = findViewById(R.id.text_startYear);
        text_endYear = findViewById(R.id.text_endYear);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnSelectDegree.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectDegreeActivity.class);
            onSelectedCategory.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        if (type.equals("edit")){
            degreeDataModel = education.getApplicantDegreeData();
            text_degreeName.setText(degreeDataModel.getName());
            editText_fieldOfStudy.setText(education.getFieldOfStudy());
            editText_schoolName.setText(education.getSchoolName());
            text_startYear.setText(education.getStartDate());
            text_endYear.setText(education.getEndDate());
        }

        btnSave.setOnClickListener(v -> {
            String fieldOfStudy = editText_fieldOfStudy.getText().toString();
            String schoolName = editText_schoolName.getText().toString();
            String startDate = text_startYear.getText().toString();
            String endDate = text_endYear.getText().toString();

            if (degreeDataModel==null){
                CustomCookieToast.showRequiredToast(ApplicantAddEducationActivity.this, "Please select degree or certification");
            }else if (TextUtils.isEmpty(fieldOfStudy)){
                CustomCookieToast.showRequiredToast(ApplicantAddEducationActivity.this, "Please enter field of study");
            }else if (TextUtils.isEmpty(schoolName)){
                CustomCookieToast.showRequiredToast(ApplicantAddEducationActivity.this, "Please enter school name");
            }else if (TextUtils.isEmpty(startDate)){
                CustomCookieToast.showRequiredToast(ApplicantAddEducationActivity.this, "Please select start year");
            }else {
                Intent intent = new Intent();
                intent.putExtra("applicantEducationDataModel", new ApplicantEducationDataModel(degreeDataModel, fieldOfStudy, schoolName, startDate, endDate));
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });

        setDateListenerListeners();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

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

                    DatePickerDialog dialog = new DatePickerDialog(ApplicantAddEducationActivity.this,
                            startDateListener,
                            year, month, day);

                    dialog.show();
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - (86400000L *365));
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
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(ApplicantAddEducationActivity.this,
                            endDateListener,
                            year, month, day);
                    dialog.show();
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
}