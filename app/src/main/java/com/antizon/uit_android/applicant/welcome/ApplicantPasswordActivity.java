package com.antizon.uit_android.applicant.welcome;

import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.home.ApplicantBottomNavigationActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.uit_interface.JsonPlaceHolderAPI;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ApplicantPasswordActivity extends BaseActivity {

    private static final String TAG = ApplicantPasswordActivity.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView menYellow, backIcon;
    TextView next;
    EditText passwordEditText;
    String passwordEditTextValue = "", selectDobValue = "", phoneValue = "", emailAddressEditTextValue = "",
            applicantNameValue = "", encodedImageData = "", jobTitleValue = "";

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_password);
        Utilities.setWhiteBars(ApplicantPasswordActivity.this);

        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    void setIds() {
        backIcon = findViewById(R.id.backIcon);
        menYellow = findViewById(R.id.menYellow);
        next = findViewById(R.id.next);
        passwordEditText = findViewById(R.id.passwordEditText);

    }

    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            phoneValue = getIntent().getStringExtra("phoneNumber");
            selectDobValue = getIntent().getStringExtra("dob");
            jobTitleValue = getIntent().getStringExtra("jobTitle");
            file = new File(getRealPathFromURI(Uri.parse(encodedImageData)));
        }
    }

    void initialize() {
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(this);
        loadProfile(ApplicantPasswordActivity.this, encodedImageData, menYellow);
    }


    void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());
        next.setOnClickListener(v -> {
            hideSoftKeyboard(ApplicantPasswordActivity.this, passwordEditText);
            passwordEditTextValue = passwordEditText.getText().toString().trim();
            if (passwordEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ApplicantPasswordActivity.this, "Please enter your password");
            } else {
                registerApplicant();
            }
        });
    }


    private void registerApplicant() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        RequestBody name = RequestBody.create(applicantNameValue, MediaType.parse("text/plain"));
        RequestBody email = RequestBody.create(emailAddressEditTextValue, MediaType.parse("text/plain"));
        RequestBody password = RequestBody.create(passwordEditTextValue, MediaType.parse("text/plain"));
        RequestBody job_title = RequestBody.create(jobTitleValue, MediaType.parse("text/plain"));
        RequestBody phone = RequestBody.create(phoneValue, MediaType.parse("text/plain"));
        RequestBody dob = RequestBody.create(selectDobValue, MediaType.parse("text/plain"));

        RequestBody requestFile = RequestBody.create(file,  MediaType.parse("*/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);

        JsonPlaceHolderAPI api = getApi();
        Call<String> call = api.registerApplicant(name, email, password, phone, dob, job_title, body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                Log.d(TAG, response.toString());
                progressDialog.dismiss();
                if (response.body() != null) {
                    try {
                        createSession(response.body());
                    } catch (Exception e) {

                        e.printStackTrace();
                        progressDialog.dismiss();

                    }
                } else {
                    Log.d(TAG, "uploadVideo: Error uploading Video");
                    progressDialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "uploadVideo: onFailure: error: " + t.getMessage());
            }
        });
    }

    void createSession(String response) {
        progressDialog.dismiss();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {

                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONObject profileObject = dataObject.getJSONObject("profile");
                JSONObject accessTokenObject = dataObject.getJSONObject("access_token");
                Log.d(TAG, "onResponse: data: size: " + profileObject.length());

                int id = profileObject.getInt("user_id");
                String email = profileObject.getString("email");
                String name = profileObject.getString("name");
                String profile_image = profileObject.getString("profile_image");
                String website = profileObject.getString("website");
                String city = profileObject.getString("city");
                String state = profileObject.getString("state");
                String phone = profileObject.getString("phone");
                String job_title = profileObject.getString("job_title");
                int account_status = profileObject.getInt("account_status");
                String address = profileObject.getString("address");
                String bio = profileObject.getString("bio");
                String dob = profileObject.getString("dob");
                int role = profileObject.getInt("role");
                int application_status = profileObject.getInt("application_status");
                String access_token = accessTokenObject.getString("token");

                sessionManagement.createLoginSession("" + id, email, name, passwordEditTextValue, website, profile_image,
                        address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role,
                        access_token, "" + application_status);

                Toast.makeText(ApplicantPasswordActivity.this, "User Successfully Signed Up.", Toast.LENGTH_SHORT).show();

                openNextScreen();

            } else {
                CustomCookieToast.showFailureToast(ApplicantPasswordActivity.this, message);
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openNextScreen() {
        finishAffinity();
        Intent intent = new Intent(ApplicantPasswordActivity.this, ApplicantBottomNavigationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}