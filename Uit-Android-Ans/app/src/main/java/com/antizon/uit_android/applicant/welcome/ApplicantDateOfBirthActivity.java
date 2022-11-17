package com.antizon.uit_android.applicant.welcome;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.Utilities;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ApplicantDateOfBirthActivity extends BaseActivity {

    private DatePickerDialog.OnDateSetListener dateListener;

    ImageView backIcon, menYellow;
    TextView next, text_dateOfBirth;

    String selectDobValue = "";
    String phoneValue = "";
    String emailAddressEditTextValue = "";
    String applicantNameValue;
    String encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_date_of_birth);
        Utilities.setWhiteBars(ApplicantDateOfBirthActivity.this);

        initViews();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        text_dateOfBirth = findViewById(R.id.text_dateOfBirth);
        menYellow = findViewById(R.id.menYellow);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            phoneValue = getIntent().getStringExtra("phoneNumber");

            loadProfile(ApplicantDateOfBirthActivity.this, encodedImageData, menYellow);
        }

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> openNextScreen());

        text_dateOfBirth.setOnTouchListener((View view, MotionEvent motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(ApplicantDateOfBirthActivity.this, dateListener, year, month, day);
                    dialog.show();

                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 86400000);
                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            return false;
        });

        dateListener = (datePicker, year, month, day) -> {
            month = month + 1;
            selectDobValue = year + "-" + month + "-" + day;
            @SuppressLint("SimpleDateFormat")
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat")
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String inputDateStr = selectDobValue;
            Date date = null;
            try {
                date = inputFormat.parse(inputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            String outputDateStr = outputFormat.format(date);


            long timeFrom = Utilities.getTimeFromPattern( Utilities.getCurrentTimeStamp(), "dd MMM yyyy");
            long selected = Utilities.getTimeFromPattern( outputDateStr, "dd MMM yyyy");

            datePicker.setMinDate(timeFrom);

            if (selected > timeFrom){
                text_dateOfBirth.setText(outputDateStr);
            }else{
                text_dateOfBirth.setText("");
            }

        };
    }



    private void openNextScreen() {
        Intent intent = new Intent(ApplicantDateOfBirthActivity.this, ApplicantPasswordActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        intent.putExtra("email", emailAddressEditTextValue);
        intent.putExtra("phoneNumber", phoneValue);
        intent.putExtra("dob", selectDobValue);
        intent.putExtra("jobTitle", "");
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}
