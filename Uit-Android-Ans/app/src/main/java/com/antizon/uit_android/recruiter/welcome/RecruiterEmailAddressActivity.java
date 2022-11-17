package com.antizon.uit_android.recruiter.welcome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class RecruiterEmailAddressActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon,menYellow;
    TextView next;
    EditText edittext_recruiterEmail;

    String profilePic = "", fullName = "", recruiterEmail = "", companyId = "", from ="", companyTeamMemberEmail;
    ApplicantJobDataModel recruiterJobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_email_address);
        Utilities.setWhiteBars(RecruiterEmailAddressActivity.this);
        context = RecruiterEmailAddressActivity.this;
        progressDialog = new ProgressDialog(context);
        initViews();
    }


    private void initViews() {
        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            sessionManagement = new SessionManagement(context);
            companyTeamMemberEmail = getIntent().getStringExtra("companyTeamMemberEmail");
        }else {
            profilePic = getIntent().getStringExtra("profilePic");
            fullName = getIntent().getStringExtra("fullName");
            companyId = getIntent().getStringExtra("companyId");
            recruiterJobDataModel = getIntent().getParcelableExtra("recruiterJobDataModel");
        }



        backIcon = findViewById(R.id.backIcon);
        menYellow = findViewById(R.id.menYellow);
        next = findViewById(R.id.next);
        edittext_recruiterEmail = findViewById(R.id.edittext_recruiterEmail);

        if (from.equals("edit")){
            edittext_recruiterEmail.setText(companyTeamMemberEmail);
            Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), menYellow);
        }else {
            Utilities.loadCircleImage(context, profilePic, menYellow);
        }



        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            recruiterEmail = edittext_recruiterEmail.getText().toString().trim();
            if (recruiterEmail.isEmpty()) {
                CustomCookieToast.showRequiredToast(RecruiterEmailAddressActivity.this, "Please enter email address");
            }else if (!Utilities.isValidEmail(recruiterEmail)) {
                CustomCookieToast.showRequiredToast(RecruiterEmailAddressActivity.this, "Please enter valid email address");
            } else {
                Utilities.hideKeyboard(edittext_recruiterEmail, context);
                sendVerificationCode();
            }
        });

    }

    private void sendVerificationCode() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", recruiterEmail);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("sending code...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {

                Toast.makeText(this, "Code Sent", Toast.LENGTH_SHORT).show();
                openNextScreen();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    private void openNextScreen() {
        Intent intent = new Intent(RecruiterEmailAddressActivity.this, RecruiterVerificationActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("profilePic", profilePic);
        intent.putExtra("companyId", companyId);
        intent.putExtra("fullName", fullName);
        intent.putExtra("recruiterEmail", recruiterEmail);
        intent.putExtra("recruiterJobDataModel", recruiterJobDataModel);
        onCompanyTeamMemberEmailUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onCompanyTeamMemberEmailUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyTeamMemberEmail = resultIntent.getStringExtra("companyTeamMemberEmail");

                Intent intent = new Intent();
                intent.putExtra("companyTeamMemberEmail", companyTeamMemberEmail);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }

        }
    });
}