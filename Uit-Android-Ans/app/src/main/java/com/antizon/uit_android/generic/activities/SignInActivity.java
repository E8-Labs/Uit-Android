package com.antizon.uit_android.generic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ForgotPasswordActivity;
import com.antizon.uit_android.activities.home.ApplicantBottomNavigationActivity;
import com.antizon.uit_android.activities.home.CompanyTeamMemberBottomNavigationActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.activities.home.CompanyBottomNavigationActivity;
import com.antizon.uit_android.company.welcome.CompanyCreationCongratulationActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.activities.home.UitAdminDashboardActivity;
import com.antizon.uit_android.activities.home.UitMemberMainDashboardActivity;
import com.antizon.uit_android.recruiter.welcome.CompanyTeamMemberConnectGreenHouseActivity;
import com.antizon.uit_android.recruiter.welcome.RecruiterCongratulationsActivity;
import com.antizon.uit_android.uit_members.welcome.UitMemberPendingActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;

public class SignInActivity extends BaseActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    ImageView backIcon, ic_hideEyes;
    EditText username, edit_password;
    RelativeLayout layout_showHide;

    String usernameValue, passwordValue;
    TextView btnForgotPassword, login, signUpNow, terms, privacy;

    boolean isPasswordShown = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Utilities.setWhiteBars(SignInActivity.this);
        progressDialog = new ProgressDialog(SignInActivity.this);
        sessionManagement = new SessionManagement(SignInActivity.this);
        initViews();


        layout_showHide.setOnClickListener(v -> {
            if (!isPasswordShown){
                ic_hideEyes.setImageResource(R.drawable.show_ic);
                edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edit_password.setSelection(edit_password.getText().toString().length());
                isPasswordShown = true;
            }
            else {
                ic_hideEyes.setImageResource(R.drawable.hide_ic);
                edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edit_password.setSelection(edit_password.getText().toString().length());
                isPasswordShown = false;
            }
        });
    }

    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        layout_showHide = findViewById(R.id.layout_showHide);
        ic_hideEyes = findViewById(R.id.ic_hideEyes);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        edit_password = findViewById(R.id.edit_password);
        signUpNow = findViewById(R.id.signUpNow);
        terms = findViewById(R.id.terms);
        privacy = findViewById(R.id.privacy);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        backIcon.setOnClickListener(v -> onBackPressed());

        terms.setOnClickListener(v -> goToWebViewActivity("Terms and Condition", "https://www.usintechnology.com/platform-terms"));
        privacy.setOnClickListener(v -> goToWebViewActivity("Privacy Policy", "https://www.usintechnology.com/privacy-policy"));

        login.setOnClickListener(v -> {
            usernameValue = username.getText().toString().trim();
            passwordValue = Objects.requireNonNull(edit_password.getText()).toString().trim();

            if (usernameValue.isEmpty()){
                CustomCookieToast.showRequiredToast(SignInActivity.this, "Please enter your email");
            }else if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()){
                CustomCookieToast.showRequiredToast(SignInActivity.this, "Please enter valid email");
            }else if (passwordValue.isEmpty()){
                CustomCookieToast.showRequiredToast(SignInActivity.this, "Please enter your password");
            }else {
                hideSoftKeyboard(SignInActivity.this, username);
                applySignIn();
            }
        });

        signUpNow.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, AccountTypeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
    }

    private void applySignIn() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", usernameValue);
        params.put("password", passwordValue);

        sendServerRequestPOST(AppConstants.SIGN_IN, params);

    }

    @Override
    public void requestStarted() {
        super.requestStarted();
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
                String greenhouse_access_token = profileObject.getString("greenhouse_access_token");
                int greenhouse_status = profileObject.getInt("greenhouse_status");
                int unread_notifications = profileObject.getInt("unread_notifications");
                int unread_messages = profileObject.getInt("unread_messages");

                if (role ==  2 && account_status == 4) {
                    Intent intent = new Intent(SignInActivity.this, CompanyCreationCongratulationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }else if (role ==  2 && account_status == 5) {
                    CustomCookieToast.showFailureToast(SignInActivity.this, "Your company has been paused by Uit Admin");
                }else if (role ==  2 && account_status == 2) {
                    CustomCookieToast.showFailureToast(SignInActivity.this, "Your company has been rejected by Uit Admin");
                } else if (role ==  4 && account_status == 4) {
                    Intent intent = new Intent(SignInActivity.this, RecruiterCongratulationsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }else if (role ==  4 && account_status == 5) {
                    CustomCookieToast.showFailureToast(SignInActivity.this, "Your company has been paused by Uit Admin");
                }else if (role ==  4 && account_status == 2) {
                    CustomCookieToast.showFailureToast(SignInActivity.this, "Your account has been reject by Company Admin");
                }else if (role == 3  && account_status == 4) {
                    Intent intent = new Intent(SignInActivity.this, UitMemberPendingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                } else {
                    sessionManagement.createLoginSession("" + id, email, name, passwordValue, website, profile_image, address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role, access_token, "" + application_status, unread_notifications +"", unread_messages  +"");
                    Toast.makeText(SignInActivity.this, "User Successfully Signed In.", Toast.LENGTH_SHORT).show();

                    if (role == 4){
                        JSONObject parentObject = profileObject.getJSONObject("parent");
                        int parentId = parentObject.getInt("user_id");
                        String parentName = parentObject.getString("name");
                        String parentProfileImage = parentObject.getString("profile_image");
                        sessionManagement.setKeyParentId("" + parentId);
                        sessionManagement.setKeyParentProfileName(parentName);
                        sessionManagement.setKeyParentProfileImage(parentProfileImage);
                    }

                    openNextActivity(role);
                }

            } else {
                CustomCookieToast.showFailureToast(SignInActivity.this, message);
            }
        } catch (JSONException e) {
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
            moveToNextScreen(CompanyTeamMemberBottomNavigationActivity.class);
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
    }

    private void goToWebViewActivity(String title, String pageUrl){
        Intent intent = new Intent(SignInActivity.this, WebViewActivity.class);
        intent.putExtra("pageTitle", title);
        intent.putExtra("pageUrl", pageUrl);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    private void commentedCoded(){
//        else if (role == 4  && greenhouse_status == 0) {
//            JSONObject parentObject = profileObject.getJSONObject("parent");
//            int parentId = parentObject.getInt("user_id");
//            String parentName = parentObject.getString("name");
//            String parentProfileImage = parentObject.getString("profile_image");
//
//            Intent intent = new Intent(SignInActivity.this, CompanyTeamMemberConnectGreenHouseActivity.class);
//            intent.putExtra("id", "" + id);
//            intent.putExtra("email", email);
//            intent.putExtra("name", name);
//            intent.putExtra("passwordValue", passwordValue);
//            intent.putExtra("website", website);
//            intent.putExtra("profile_image", profile_image);
//            intent.putExtra("address", address);
//            intent.putExtra("city", city);
//            intent.putExtra("state", state);
//            intent.putExtra("phone", phone);
//            intent.putExtra("account_status", "" + account_status);
//            intent.putExtra("bio",   bio);
//            intent.putExtra("dob",   dob);
//            intent.putExtra("job_title",   job_title);
//            intent.putExtra("role",   ""+role);
//            intent.putExtra("access_token",   access_token);
//            intent.putExtra("application_status",   "" + application_status);
//            intent.putExtra("parentId",   "" + parentId);
//            intent.putExtra("parentName",   parentName);
//            intent.putExtra("parentProfileImage",   parentProfileImage);
//            startActivity(intent);
//            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
//
//        }
    }
}