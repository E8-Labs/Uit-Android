package com.antizon.uit_android.applicant.activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;

public class ActivityDemoInfo extends BaseActivity {
    private static final String TAG = ActivityDemoInfo.class.getSimpleName();

    ImageView redNoah,grayBack;
    TextView name,reject,approve,delete,pause;
    ModelUitAdminApproved dataModel;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    String role ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_info);
        Utilities.setCustomStatusAndNavColor(ActivityDemoInfo.this, R.color.app_color, R.color.white);
        setIds();
        initialize();
        initializeViewsAsPerRole();
        setListener();
        getIntentData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        redNoah = findViewById(R.id.redNoah);
        name = findViewById(R.id.name);
        grayBack = findViewById(R.id.grayBack);
        reject = findViewById(R.id.reject);
        approve = findViewById(R.id.approve);
        delete = findViewById(R.id.delete);
        pause = findViewById(R.id.pause);
    }
    void getIntentData() {
        if (getIntent().getParcelableExtra("dataModel") != null) {
            dataModel = getIntent().getExtras().getParcelable("dataModel");
            name.setText(dataModel.getName());

            Glide.with(ActivityDemoInfo.this)
                    .load(dataModel.getProfile_image())
                    .circleCrop()
                    .into(redNoah);
        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(ActivityDemoInfo.this);
        sessionManagement = new SessionManagement(ActivityDemoInfo.this);
        role=sessionManagement.getRole();
        loadProfile(ActivityDemoInfo.this, sessionManagement.getProfileImage(), redNoah);

    }
    void initializeViewsAsPerRole() {

//        case UITAdmin = 1
//        case Company = 2
//        case UITMember = 3
//        case CompanyMember = 4
//        case Applicant = 5

//        Set Visible to all the widgets (who are changing there)

        if (role.equalsIgnoreCase("1")) {

            pause.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            reject.setVisibility(View.GONE);
            approve.setVisibility(View.GONE);


        } else if (role.equalsIgnoreCase("2")) {

        } else if (role.equalsIgnoreCase("3")) {

            reject.setVisibility(View.GONE);
            approve.setVisibility(View.GONE);
//            message.setVisibility(View.VISIBLE);

        } else if (role.equalsIgnoreCase("4")) {

        } else if (role.equalsIgnoreCase("5")) {

        }
    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        grayBack.setOnClickListener(view -> {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);

        });
    }
}