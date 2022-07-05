package com.antizon.uit_android.applicant.welcome;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.races.RacesModel;
import com.antizon.uit_android.utilities.Utilities;

public class ActivityApplicantSelectGenderIdentity extends BaseActivity {
    private static final String TAG = ActivityApplicantSelectGenderIdentity.class.getSimpleName();

    SessionManagement sessionManagement;

    ImageView backIcon, menYellow;
    TextView skip, man, woman, nonBinary, prefer;
    String identityValue = "", encodedImageData = "";
    ImageView menIcon, womenIcon, nonIcon;
    ConstraintLayout approvedLayout, pausedLayout, maleLayout, femaleLayout;

    RacesModel selectedRaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_gender_identity);
        Utilities.setWhiteBars(ActivityApplicantSelectGenderIdentity.this);

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
        prefer = findViewById(R.id.prefer);
        menYellow = findViewById(R.id.menYellow);
        menIcon = findViewById(R.id.menIcon);
        womenIcon = findViewById(R.id.womenIcon);
        nonIcon = findViewById(R.id.nonIcon);
        maleLayout = findViewById(R.id.maleLayout);
        femaleLayout = findViewById(R.id.femaleLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
    }

    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            selectedRaces = getIntent().getParcelableExtra("selectedRaces");
        }
    }

    void initialize() {
        loadProfile(ActivityApplicantSelectGenderIdentity.this, encodedImageData, menYellow);
        sessionManagement=new SessionManagement(ActivityApplicantSelectGenderIdentity.this);
        loadProfile(ActivityApplicantSelectGenderIdentity.this, sessionManagement.getProfileImage(), menYellow);
    }

    void setListener() {
        maleLayout.setOnClickListener(v -> {
            setPendingOneLayout();
            identityValue = "1";
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        femaleLayout.setOnClickListener(v -> {
            setApprovedOneLayout();
            identityValue = "2";
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        approvedLayout.setOnClickListener(v -> {
            setPausedOneLayout();
            identityValue = "3";
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        pausedLayout.setOnClickListener(v -> {
            setFullLayout();
            identityValue = "4";
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        skip.setOnClickListener(v -> {
            openNextScreen();
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        backIcon.setOnClickListener(v -> onBackPressed());

    }

    void setPendingOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.login_background);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);

        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));

        man.setTextColor(getColor(R.color.white));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.black));
        prefer.setTextColor(getColor(R.color.black));
    }

    void setApprovedOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.login_background);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);


        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.white));
        nonBinary.setTextColor(getColor(R.color.black));
        prefer.setTextColor(getColor(R.color.black));
    }

    void setPausedOneLayout() {

        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.login_background);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);

        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.white));

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.white));
        prefer.setTextColor(getColor(R.color.black));

    }

    void setFullLayout() {
        maleLayout.setBackgroundResource(R.drawable.text_here_border);
        femaleLayout.setBackgroundResource(R.drawable.text_here_border);
        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.login_background);

        menIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        womenIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));
        nonIcon.setColorFilter(ContextCompat.getColor(this, R.color.black));

        man.setTextColor(getColor(R.color.black));
        woman.setTextColor(getColor(R.color.black));
        nonBinary.setTextColor(getColor(R.color.black));
        prefer.setTextColor(getColor(R.color.white));
    }

    void openNextScreen() {
        Intent intent = new Intent(ActivityApplicantSelectGenderIdentity.this, ActivityApplicantSexualOrientation.class);
        intent.putExtra("selectedRaces", selectedRaces);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("genderIdentity", identityValue);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
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