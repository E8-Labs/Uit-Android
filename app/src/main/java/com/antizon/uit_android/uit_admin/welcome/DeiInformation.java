package com.antizon.uit_android.uit_admin.welcome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.bumptech.glide.Glide;

public class DeiInformation extends BaseActivity {
    private static final String TAG = DeiInformation.class.getSimpleName();

    TextView name,statement,yes,yesTraining,yesEmployment,yesPrograming,yesTraining2,reject,approved,delete,pause;
    ImageView noahImage, grayBack;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ModelUitAdminApproved dataModel;
    ModelUitAdminPending pendingModel;
    String role ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dei_information);
        setIds();
        initialize();
        initializeViewsAsPerRole();
        setListener();
        getIntentData();
        getIntentDataPending();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        noahImage = findViewById(R.id.noahImage);
        name = findViewById(R.id.name);
        statement = findViewById(R.id.statement);
        grayBack = findViewById(R.id.grayBack);
        yes = findViewById(R.id.yes);
        yesTraining = findViewById(R.id.yesTraining);
        yesEmployment = findViewById(R.id.yesEmployment);
        yesPrograming = findViewById(R.id.yesPrograming);
        yesTraining2 = findViewById(R.id.yesTraining2);
        reject = findViewById(R.id.reject);
        approved = findViewById(R.id.approved);
        delete = findViewById(R.id.delete);
        pause = findViewById(R.id.pause);

    }
    void getIntentDataPending() {
        if (getIntent().getExtras().getSerializable("pendingModel") != null) {
            pendingModel = (ModelUitAdminPending) getIntent().getExtras().getSerializable("pendingModel");
            name.setText(pendingModel.getName());
            statement.setText((pendingModel.getDeistatement()));
            yes.setText(("Yes"));
            yesTraining.setText(("Yes"));
            yesEmployment.setText(("Yes"));
            yesPrograming.setText(("Yes"));
            yesTraining2.setText(("Yes"));

            Glide.with(DeiInformation.this)
                    .load(pendingModel.getProfile_image())
                    .circleCrop()
                    .into(noahImage);
        }
    }
    void getIntentData() {
        if (getIntent().getExtras().getSerializable("dataModel") != null) {
            dataModel = (ModelUitAdminApproved) getIntent().getExtras().getSerializable("dataModel");
            name.setText(dataModel.getName());
            statement.setText((dataModel.getDeistatement()));
            yes.setText((""+dataModel.getTeam_lead()));
            yesTraining.setText((""+dataModel.getTraining()));
            yesEmployment.setText((""+dataModel.getErg()));
            yesPrograming.setText((""+dataModel.getProgramming()));
            yesTraining2.setText((""+dataModel.getTraining()));

            Glide.with(DeiInformation.this)
                    .load(dataModel.getProfile_image())
                    .circleCrop()
                    .into(noahImage);
        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(DeiInformation.this);
        role=sessionManagement.getRole();
        loadProfile(DeiInformation.this, sessionManagement.getProfileImage(), noahImage);

    }
    void initializeViewsAsPerRole() {

//        case UITAdmin = 1
//        case Company = 2
//        case UITMember = 3
//        case CompanyMember = 4
//        case Applicant = 5

//        Set Visible to all the widgets (who are changing there)

        if (role.equalsIgnoreCase("1")) {

            delete.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            reject.setVisibility(View.GONE);
            approved.setVisibility(View.GONE);

        } else if (role.equalsIgnoreCase("2")) {

        } else if (role.equalsIgnoreCase("3")) {

            reject.setVisibility(View.GONE);
            approved.setVisibility(View.GONE);
//            message.setVisibility(View.VISIBLE);

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