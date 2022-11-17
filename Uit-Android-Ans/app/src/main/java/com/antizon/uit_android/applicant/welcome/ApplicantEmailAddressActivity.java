package com.antizon.uit_android.applicant.welcome;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ApplicantEmailAddressActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    ProgressDialog progressDialog;
    ImageView backIcon, menYellow;
    TextView next;
    EditText emailAddressEditText;
    String emailAddressEditTextValue = "", applicantNameValue = "", encodedImageData = "", from, applicantEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_email_address);
        Utilities.setWhiteBars(ApplicantEmailAddressActivity.this);
        context= ApplicantEmailAddressActivity.this;

        initViews();
    }


    private void initViews() {
        progressDialog = new ProgressDialog(this);

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            sessionManagement = new SessionManagement(context);
            applicantEmail = getIntent().getStringExtra("applicantEmail");
        }else {
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
        }


        menYellow = findViewById(R.id.menYellow);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);


        if (from.equals("edit")){
            emailAddressEditText.setText(applicantEmail);
            Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), menYellow);
        }else {
            loadProfile(ApplicantEmailAddressActivity.this, encodedImageData, menYellow);
        }


        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(ApplicantEmailAddressActivity.this, emailAddressEditText);
            emailAddressEditTextValue = emailAddressEditText.getText().toString().trim();

            if (emailAddressEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ApplicantEmailAddressActivity.this, "Please enter your email");
            }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddressEditTextValue).matches()) {
                CustomCookieToast.showRequiredToast(ApplicantEmailAddressActivity.this, "Please enter valid email");
            } else {
                sendVerificationCode();
            }
        });

    }


    private void sendVerificationCode() {
        HashMap<String, String> params;
        params = new HashMap<>();
        params.put("email", emailAddressEditTextValue);
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
        Intent intent = new Intent(ApplicantEmailAddressActivity.this, ApplicantVerificationCodeActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        intent.putExtra("email", emailAddressEditTextValue);
        onApplicantEmailUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


    ActivityResultLauncher<Intent> onApplicantEmailUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String applicantEmail = resultIntent.getStringExtra("applicantEmail");

                Intent intent = new Intent();
                intent.putExtra("applicantEmail", applicantEmail);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }

        }
    });
}