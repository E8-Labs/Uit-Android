package com.antizon.uit_android.company.welcome;

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
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class CompanyEmailAddressActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon, redNoah2;
    TextView next;
    EditText emailEditText;

    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "", city = "", state = "", websiteValue="", emailValue = "", from, companyEmail;
    double latitude, longitude;

    ArrayList<ModelCompanySize>  selectedCompanyInterestInStageList, selectedCompanyInSizeList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_email_address);
        Utilities.setWhiteBars(CompanyEmailAddressActivity.this);
        context = CompanyEmailAddressActivity.this;

        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("edit")){
                companyEmail = getIntent().getStringExtra("companyEmail");
                sessionManagement = new SessionManagement(context);
            }else {
                encodedImageData = getIntent().getStringExtra("profilePic");
                companyNameHintValue = getIntent().getStringExtra("companyName");
                typeHereValue = getIntent().getStringExtra("bio");
                city = getIntent().getStringExtra("city");
                state = getIntent().getStringExtra("state");
                websiteValue = getIntent().getStringExtra("website");
                latitude = getIntent().getDoubleExtra("latitude", latitude);
                longitude = getIntent().getDoubleExtra("latitude", longitude);
                selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
                selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
                selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
            }
        }

        progressDialog = new ProgressDialog(this);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        emailEditText = findViewById(R.id.emailEditText);

        if (from.equals("edit")){
            emailEditText.setText(companyEmail);
            Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), redNoah2);
        }else {
            loadProfile(CompanyEmailAddressActivity.this, encodedImageData, redNoah2);
        }


        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            emailValue = emailEditText.getText().toString().trim();
            if (emailValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyEmailAddressActivity.this, "Please enter email address");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()){
                CustomCookieToast.showRequiredToast(CompanyEmailAddressActivity.this, "Please enter valid email address");
            }else {
                Utilities.hideKeyboard(emailEditText, CompanyEmailAddressActivity.this);
                sendVerificationCode();
            }
        });


    }


    private void sendVerificationCode() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailValue);
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
                CustomCookieToast.showFailureToast(CompanyEmailAddressActivity.this, message);
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
        Intent intent = new Intent(CompanyEmailAddressActivity.this, CompanyVerificationCodeActivity.class);
        intent.putExtra("email", emailValue);
        intent.putExtra("from", from);

        if (from.equals("add")){
            intent.putExtra("profilePic", encodedImageData);
            intent.putExtra("companyName", companyNameHintValue);
            intent.putExtra("bio", typeHereValue);
            intent.putExtra("website", websiteValue);
            intent.putExtra("city", city);
            intent.putExtra("state", state);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
            intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
            intent.putParcelableArrayListExtra("selectedCompanyInSizeList", selectedCompanyInSizeList);
        }
        onCompanyEmailUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onCompanyEmailUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyEmail = resultIntent.getStringExtra("companyEmail");

                Intent intent = new Intent();
                intent.putExtra("companyEmail", companyEmail);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }

        }
    });
}