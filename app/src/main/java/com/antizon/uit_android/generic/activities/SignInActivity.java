package com.antizon.uit_android.generic.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.home.ApplicantBottomNavigationActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.activities.home.CompanyBottomNavigationActivity;
import com.antizon.uit_android.company.welcome.CompanyCreationCongratulationActivity;
import com.antizon.uit_android.company.welcome.CompanyInviteTeamMembersActivity;
import com.antizon.uit_android.company.welcome.CompanySummaryActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.activities.home.UitAdminDashboardActivity;
import com.antizon.uit_android.activities.home.UitMemberMainDashboardActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;

public class SignInActivity extends BaseActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    ImageView backIcon;
    EditText username;
    TextInputEditText password;
    String usernameValue, passwordValue;
    TextView login, signUpNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Utilities.setCustomStatusAndNavColor(SignInActivity.this, R.color.white, R.color.white);
        setIds();
        initialize();
        setListener();
    }

    private void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signUpNow = findViewById(R.id.signUpNow);
    }

    private void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(SignInActivity.this);
        sessionManagement = new SessionManagement(SignInActivity.this);

    }

    private void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(v -> onBackPressed());

        login.setOnClickListener(v -> {
            hideSoftKeyboard(SignInActivity.this, username);

            usernameValue = username.getText().toString().trim();
            passwordValue = Objects.requireNonNull(password.getText()).toString().trim();

            if (usernameValue.isEmpty()){
                CustomCookieToast.showRequiredToast(SignInActivity.this, "Please enter your email");
            }else if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()){
                CustomCookieToast.showRequiredToast(SignInActivity.this, "Please enter valid email");
            }else if (passwordValue.isEmpty()){
                CustomCookieToast.showRequiredToast(SignInActivity.this, "Please enter your password");
            }else {
                applySignIn();
            }
        });

        signUpNow.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, AccountTypeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

    }

    void applySignIn() {
        Log.d(TAG, "applySignIn: ");

        HashMap<String, String> params = new HashMap<>();
        params.put("email", usernameValue);
        params.put("password", passwordValue);

        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.SIGN_IN, params);

    }

    public boolean validate() {
        Log.d(TAG, "validate: ");

        usernameValue = username.getText().toString().trim();
        passwordValue = Objects.requireNonNull(password.getText()).toString().trim();

        boolean valid = true;
        if (usernameValue.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
            username.setError("Please enter valid email");
            valid = false;
        }
        if (passwordValue.isEmpty()) {
            password.setError("Please enter your password");
            valid = false;
        }
        return valid;
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage("apply sign in...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();
        JSONObject jsonObject;

        try {

            Log.d(TAG, "onResponseReceived: response: " + response);
            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);

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

//                if (role ==  2 && account_status == 4) {
//                    Intent intent = new Intent(SignInActivity.this, CompanyCreationCongratulationActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
//                }else {
                    sessionManagement.createLoginSession("" + id, email, name, passwordValue, website, profile_image,
                            address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role,
                            access_token, "" + application_status);

                    Toast.makeText(SignInActivity.this, "User Successfully Signed In.", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onResponseReceived: role: " + role);
                    openNextActivity(role);
//                }


            } else {
                CustomCookieToast.showFailureToast(SignInActivity.this, message);
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
    }

    void openNextActivity(int role) {
        if (role == 1) {
            moveToNextScreen(UitAdminDashboardActivity.class);
        } else if (role == 2) {
            moveToNextScreen(CompanyBottomNavigationActivity.class);
        } else if (role == 3) {
            moveToNextScreen(UitMemberMainDashboardActivity.class);
        } else if (role == 4) {
            moveToNextScreen(UitAdminDashboardActivity.class);
        } else if (role == 5) {
            moveToNextScreen(ApplicantBottomNavigationActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void moveToNextScreen(Class<?> className){
        finishAffinity();
        Intent intent = new Intent(SignInActivity.this, className);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        finish();
    }
}