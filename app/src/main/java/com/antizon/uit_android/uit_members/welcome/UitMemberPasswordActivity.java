package com.antizon.uit_android.uit_members.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.uit_interface.JsonPlaceHolderAPI;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UitMemberPasswordActivity extends BaseActivity {

    private static final String TAG = UitMemberPasswordActivity.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView menYellow, backIcon;
    TextView next;
    EditText passwordEditText;
    String passwordEditTextValue = "", selectDobValue = "", phoneValue = "", codeValue = "",
            emailAddressEditTextValue = "", applicantNameValue, encodedImageData = "";
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_password);
        Utilities.setWhiteBars(UitMemberPasswordActivity.this);
        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(this);
        loadProfile(UitMemberPasswordActivity.this, encodedImageData, menYellow);
    }

    void getIntentData() {
        Log.d(TAG, "getIntentData: ");

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("fullName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            codeValue = getIntent().getStringExtra("verification");
            phoneValue = getIntent().getStringExtra("phoneNumber");
            selectDobValue = "31/12/1998";

            file = new File(getRealPathFromURI(Uri.parse(encodedImageData)));

        }
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });

        next.setOnClickListener(v -> {
            hideSoftKeyboard(UitMemberPasswordActivity.this, passwordEditText);
            passwordEditTextValue = passwordEditText.getText().toString().trim();
            if (passwordEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(UitMemberPasswordActivity.this, "Please enter your password");
            } else {
                registerUITMember();
            }
        });
    }



    private void registerUITMember() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final RequestBody name = RequestBody.create(applicantNameValue, MediaType.parse("multipart/form-data"));
        final RequestBody email = RequestBody.create(emailAddressEditTextValue, MediaType.parse("multipart/form-data"));
        final RequestBody password = RequestBody.create(passwordEditTextValue, MediaType.parse("multipart/form-data"));
        final RequestBody phone = RequestBody.create(phoneValue, MediaType.parse("multipart/form-data"));

        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", "profile_image", requestFile);

        JsonPlaceHolderAPI api = getApi();
        Call<String> call = api.registerUitMember(name, email, password, phone, body);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    try {

                        createSession(response.body());
                    } catch (Exception e) {

                        Log.d(TAG, "uploadVideo: onResponse: Exception: " + e.getMessage());
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.d(TAG, e.getLocalizedMessage());
                        Snackbar.make(getCurrentFocus(), "uploadVideo: Error uploading image", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "uploadVideo: Error uploading Video");
                    Snackbar.make(getCurrentFocus(), "Error uploading video", Snackbar.LENGTH_LONG).show();
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
            Log.d(TAG, "onResponseReceived: response: " + response);

            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");

            Log.d(TAG, "createSession: status: " + status);
            Log.d(TAG, "createSession: message: " + message);

            if (status) {

                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONObject profileObject = dataObject.getJSONObject("profile");
                JSONObject accessTokenObject = dataObject.getJSONObject("access_token");
                Log.d(TAG, "onResponse: data: size: " + profileObject.length());

                int id = profileObject.getInt("id");
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
                String dob = "";
                int role = profileObject.getInt("role");
                int application_status = profileObject.getInt("application_status");

                String access_token = accessTokenObject.getString("token");

                sessionManagement.createLoginSession("" + id, email, name, passwordEditTextValue, website, profile_image,
                        address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role,
                        access_token, "" + application_status);
                Toast.makeText(UitMemberPasswordActivity.this, "User Successfully Signed In.", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onResponseReceived: role: " + role);
                openNextScreen();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void openNextScreen() {
        Log.d(TAG, "openNextScreen: ");
        Intent intent = new Intent(UitMemberPasswordActivity.this, UitMemberPendingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}