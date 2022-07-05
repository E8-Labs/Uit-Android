package com.antizon.uit_android.applicant.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class AddEducation extends BaseActivity {
    private static final String TAG = Degree.class.getSimpleName();
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_education);
        setIds();
        initialize();
        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        save = findViewById(R.id.save);


    }

    void initialize() {
        Log.d(TAG, "initialize: ");


    }
    void setListener(){
        Log.d(TAG, "setListener: ");

        save.setOnClickListener(v -> {

            Intent intent = new Intent(AddEducation.this, ApplicantEducationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);

        });


    }
}