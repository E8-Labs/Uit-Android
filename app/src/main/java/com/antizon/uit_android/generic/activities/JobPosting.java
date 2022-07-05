package com.antizon.uit_android.generic.activities;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class JobPosting extends BaseActivity {
    private static final String TAG = JobPosting.class.getSimpleName();

    TextView remote, hybrid, onSite, fullTime, intern, partTime, freeLancer, internship, next;
    ConstraintLayout pendingOneLayout, approvedOneLayout, pausedOneLayout, fullLayout, partTimeLayout,
            contractLayout, freeLancerLayout, internshipLayout;
    EditText jobTitleText, industryEdit, departmentEdit, cityEdit;
    String jobTitleTextValue, industryEditValue, departmentEditValue, cityEditValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting);
        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        next = findViewById(R.id.next);
        jobTitleText = findViewById(R.id.jobTitleText);
        industryEdit = findViewById(R.id.industryEdit);
        departmentEdit = findViewById(R.id.departmentEdit);
        cityEdit = findViewById(R.id.cityEdit);
        pendingOneLayout = findViewById(R.id.pendingOneLayout);
        approvedOneLayout = findViewById(R.id.approvedOneLayout);
        pausedOneLayout = findViewById(R.id.pausedOneLayout);
        fullLayout = findViewById(R.id.fullLayout);
        partTimeLayout = findViewById(R.id.partTimeLayout);
        contractLayout = findViewById(R.id.contractLayout);
        freeLancerLayout = findViewById(R.id.freeLancerLayout);
        internshipLayout = findViewById(R.id.internshipLayout);
        freeLancer = findViewById(R.id.freeLancer);
        internship = findViewById(R.id.internship);
        remote = findViewById(R.id.remote);
        hybrid = findViewById(R.id.hybrid);
        onSite = findViewById(R.id.onSite);
        fullTime = findViewById(R.id.fullTime);
        intern = findViewById(R.id.intern);
        partTime = findViewById(R.id.partTime);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(JobPosting.this, jobTitleText);
                if (!validate()) {
                } else {

                }
            }
        });
        pendingOneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setPendingOneLayout();

            }
        });
        approvedOneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setApprovedOneLayout();

            }
        });
        pausedOneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setPausedOneLayout();

            }
        });
        fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setFullLayout();

            }
        });
        partTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setPartLayout();

            }
        });
        contractLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setContractLayout();

            }
        });
        freeLancerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setFreeLancerLayout();

            }
        });
        internshipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setInternshipLayout();

            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");

        jobTitleTextValue = jobTitleText.getText().toString().trim();
        industryEditValue = industryEdit.getText().toString().trim();
        departmentEditValue = departmentEdit.getText().toString().trim();
        cityEditValue = cityEdit.getText().toString().trim();

        boolean valid = true;
        if (jobTitleTextValue.isEmpty()) {

            valid = false;
            jobTitleText.setError("Please enter your Job title");
        }
        if (industryEditValue.isEmpty()) {

            industryEdit.setError("Please enter your industry");
            valid = false;
        }
        if (departmentEditValue.isEmpty()) {

            departmentEdit.setError("Please enter your department");
            valid = false;
        }
        if (cityEditValue.isEmpty()) {

            cityEdit.setError("Please enter your city");
            valid = false;
        }
        return valid;
    }

    void setPendingOneLayout() {

        pendingOneLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        approvedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        pausedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        remote.setTextColor(getResources().getColor(R.color.white));
        onSite.setTextColor(getResources().getColor(R.color.black));
        hybrid.setTextColor(getResources().getColor(R.color.black));
    }

    void setApprovedOneLayout() {

        pendingOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        approvedOneLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        pausedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        remote.setTextColor(getResources().getColor(R.color.black));
        onSite.setTextColor(getResources().getColor(R.color.white));
        hybrid.setTextColor(getResources().getColor(R.color.black));
    }

    void setPausedOneLayout() {

        pendingOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        approvedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        pausedOneLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));

        remote.setTextColor(getResources().getColor(R.color.black));
        onSite.setTextColor(getResources().getColor(R.color.black));
        hybrid.setTextColor(getResources().getColor(R.color.white));

    }
    void setFullLayout() {
        fullLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.white));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.black));
    }
    void setPartLayout() {
        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.white));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.black));

    }
    void setContractLayout() {

        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.white));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.black));
    }

    void setFreeLancerLayout() {

        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.white));
        internship.setTextColor(getResources().getColor(R.color.black));

    }
    void setInternshipLayout() {

        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.white));
    }
}