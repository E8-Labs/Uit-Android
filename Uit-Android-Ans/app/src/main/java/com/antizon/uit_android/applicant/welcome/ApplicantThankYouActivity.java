package com.antizon.uit_android.applicant.welcome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

public class ApplicantThankYouActivity extends AppCompatActivity {
    Context context;

    ProgressDialog progressDialog;
    TextView continueCongratsFive,notNowFive;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_thank_you);
        Utilities.setWhiteBars(ApplicantThankYouActivity.this);
        context = ApplicantThankYouActivity.this;
        sessionManagement = new SessionManagement(context);
        sessionManagement.setApplicationStatus("2");
        initViews();
    }

    void initViews() {
        continueCongratsFive = findViewById(R.id.continueCongratsFive);
        notNowFive = findViewById(R.id.notNowFive);

        progressDialog = new ProgressDialog(ApplicantThankYouActivity.this);

        continueCongratsFive.setOnClickListener(v -> {
            Intent intent = new Intent(ApplicantThankYouActivity.this, ActivityApplicantJobStatus.class);
            intent.putExtra("from", "add");
            onProfileUpdateLauncher.launch(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });

        notNowFive.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        });
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
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}