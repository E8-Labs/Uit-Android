package com.antizon.uit_android.uit_members.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

import java.util.Calendar;

public class Dob extends BaseActivity {
    private static final String TAG = Dob.class.getSimpleName();

    ImageView menYellow, backIcon;
    TextView next, selectDob;

    ConstraintLayout datePickerLayout;
    DatePicker datePicker;
    String month, day, selectYear, selectDobValue = "", phoneValue = "", codeValue = "", emailAddressEditTextValue = "",
            applicantNameValue, encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dob2);

        setIds();
        getIntentData();
        initialize();
        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        selectDob = findViewById(R.id.selectDob);
        datePickerLayout = findViewById(R.id.datePickerLayout);
        datePicker = findViewById(R.id.datePicker);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(Dob.this, encodedImageData, menYellow);
    }

    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("fullName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            codeValue = getIntent().getStringExtra("verification");
            phoneValue = getIntent().getStringExtra("phoneNumber");

            Log.d(TAG, "getIntentData: image: " + encodedImageData);
            Log.d(TAG, "getIntentData: fullName: " + applicantNameValue);
            Log.d(TAG, "getIntentData: email: " + emailAddressEditTextValue);
            Log.d(TAG, "getIntentData: codeValue: " + codeValue);
            Log.d(TAG, "getIntentData: phoneValue: " + phoneValue);
        }

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(v -> {

            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);

        });

        next.setOnClickListener(v -> openNextScreen());


        //Date of Birth Dialog From Bottom will Appear afeter this


        selectDob.setOnClickListener(view -> datePickerLayout.setVisibility(View.VISIBLE));

        datePickerLayout.setOnClickListener(v -> datePickerLayout.setVisibility(View.GONE));

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
                selectDob.setText(day + "-" + month + "-" + selectYear);
            });
        }


    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        selectDobValue = selectDob.getText().toString().trim();

        boolean valid = true;
        if (selectDobValue.isEmpty()) {
            selectDob.setError("Please enter your dob");
            valid = false;
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

                        Toast.makeText(this, "Please enter a valid date of birth", Toast.LENGTH_SHORT).show();
                        valid = false;
                    } else if ((Integer.parseInt(currentYearDate[0].trim()) - Integer.parseInt(selectYear)) < 10) {

                        Toast.makeText(this, "Only 10+ are eligible to use UIT.", Toast.LENGTH_SHORT).show();
                        valid = false;
                    }
                }

            } catch (ArrayIndexOutOfBoundsException exception) {

                Toast.makeText(this, "Please set Date of Birth", Toast.LENGTH_SHORT).show();
                return valid = false;

            }

        }
        return valid;
    }

    void openNextScreen() {

        Intent intent = new Intent(Dob.this, UitMemberPasswordActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", applicantNameValue);
        intent.putExtra("email", emailAddressEditTextValue);
        intent.putExtra("verification", codeValue);
        intent.putExtra("phoneNumber", phoneValue);
        intent.putExtra("dob", selectDobValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}