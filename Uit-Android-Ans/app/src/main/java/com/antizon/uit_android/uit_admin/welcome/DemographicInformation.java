package com.antizon.uit_android.uit_admin.welcome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;

public class DemographicInformation extends BaseActivity {
    private static final String TAG = DemographicInformation.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView grayBack,noahImage;
    TextView reject, message,flagThis,hire;
    String role ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographic_information);


        setIds();
        initialize();
        initializeViewsAsPerRole();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        grayBack = findViewById(R.id.grayBack);

        reject = findViewById(R.id.reject);
        message = findViewById(R.id.message);
        flagThis = findViewById(R.id.flagThis);
        hire = findViewById(R.id.hire);
        noahImage = findViewById(R.id.noahImage);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(DemographicInformation.this);
        role=sessionManagement.getRole();
        loadProfile(DemographicInformation.this, sessionManagement.getProfileImage(), noahImage);

    }

    void initializeViewsAsPerRole() {

//        case UITAdmin = 1
//        case Company = 2
//        case UITMember = 3
//        case CompanyMember = 4
//        case Applicant = 5

//        Set Visible to all the widgets (who are changing there)

        if (role.equalsIgnoreCase("1")) {

//            deleteIcon.setVisibility(View.VISIBLE);


        } else if (role.equalsIgnoreCase("2")) {

        } else if (role.equalsIgnoreCase("3")) {

            reject.setVisibility(View.GONE);
            flagThis.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            hire.setVisibility(View.GONE);


        } else if (role.equalsIgnoreCase("4")) {

        } else if (role.equalsIgnoreCase("5")) {

        }
    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
    }


}