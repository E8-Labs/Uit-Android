package com.antizon.uit_android.recruiter.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class FullName extends BaseActivity {
    private static final String TAG = FullName.class.getSimpleName();

    ImageView backIcon,menYellow;
    TextView next;
    EditText fullNameEditText;
    String fullNameEditTextValue = "", encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_name3);

        setIds();
        getIntentData();
        initialize();
        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        menYellow = findViewById(R.id.menYellow);
    }
    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            Log.d(TAG, "getIntentData: image: " + encodedImageData);
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(FullName.this, encodedImageData, menYellow);
    }



    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(FullName.this, fullNameEditText);
                if (!validate()) {

                } else {
                    openNextScreen();
                }
            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        fullNameEditTextValue = fullNameEditText.getText().toString().trim();

        boolean valid = true;
        if (fullNameEditTextValue.isEmpty()) {
            fullNameEditText.setError("Please enter your full name");
            valid = false;
        }

        if (fullNameEditTextValue.matches(".*[@#$%^&*()_+/.:;|,{}?]+.*")) {

            fullNameEditText.setError("Please enter your full name");
            valid = false;

        }
        if (fullNameEditTextValue.matches(".*[0-9]+.*")) {

            fullNameEditText.setError("Please enter valid name");
            valid = false;

        }
        return valid;
    }

    void openNextScreen() {

        Intent intent = new Intent(FullName.this, Job.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", fullNameEditTextValue);
        startActivity(intent);
    }

}