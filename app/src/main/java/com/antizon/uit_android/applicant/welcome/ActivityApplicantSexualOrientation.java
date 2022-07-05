package com.antizon.uit_android.applicant.welcome;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.races.RacesModel;
import com.antizon.uit_android.utilities.Utilities;

public class ActivityApplicantSexualOrientation extends BaseActivity {
    private static final String TAG = ActivityApplicantSexualOrientation.class.getSimpleName();
    SessionManagement sessionManagement;
    ImageView backIcon,menYellow;
    TextView skip;
    TextView man , woman, nonBinary;
    ConstraintLayout approvedLayout,maleLayout,femaleLayout;
    String sexualValue="", genderIdentity = "", encodedImageData = "";

    RacesModel selectedRaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_sexual_orientation);
        Utilities.setWhiteBars(ActivityApplicantSexualOrientation.this);
        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        skip = findViewById(R.id.skip);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        nonBinary = findViewById(R.id.nonBinary);
        maleLayout = findViewById(R.id.maleLayout);
        femaleLayout = findViewById(R.id.femaleLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        menYellow = findViewById(R.id.menYellow);

    }
    void  getIntentData(){
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            genderIdentity = getIntent().getStringExtra("genderIdentity");
            selectedRaces = getIntent().getParcelableExtra("selectedRaces");
        }

    }

    void initialize() {
        loadProfile(ActivityApplicantSexualOrientation.this, encodedImageData, menYellow);
        sessionManagement = new SessionManagement(ActivityApplicantSexualOrientation.this);
        loadProfile(ActivityApplicantSexualOrientation.this, sessionManagement.getProfileImage(), menYellow);
    }

    void setListener() {
        skip.setOnClickListener(v -> openNextScreen());
        
        backIcon.setOnClickListener(v -> onBackPressed());

        maleLayout.setOnClickListener(v -> {
            setPendingOneLayout();
            sexualValue="1";
            openNextScreen();
        });
        
        femaleLayout.setOnClickListener(v -> {
            setApprovedOneLayout();
            sexualValue="0";
            openNextScreen();
        });
        
        approvedLayout.setOnClickListener(v -> {
            setPausedOneLayout();
            sexualValue="";
            openNextScreen();
        });

    }

    void setPendingOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.login_background);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);

        man.setTextColor(getColor(R.color.white));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.black));

    }

    void setApprovedOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.login_background);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.white));
        nonBinary.setTextColor(getColor(R.color.black));

    }

    void setPausedOneLayout() {
        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.login_background);
        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.white));
    }

    void openNextScreen() {
        Intent intent = new Intent(ActivityApplicantSexualOrientation.this, ActivityApplicantVeteranStatus.class);
        intent.putExtra("selectedRaces", selectedRaces);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("genderIdentity", genderIdentity);
        intent.putExtra("sexualOrientation", sexualValue);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}