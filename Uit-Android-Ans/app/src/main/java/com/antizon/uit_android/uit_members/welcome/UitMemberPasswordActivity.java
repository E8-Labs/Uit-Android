package com.antizon.uit_android.uit_members.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    Context context;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView menYellow, backIcon, ivLength, ivCase, ivSpecial;

    TextView next, text_characters, text_upperLowerCase, text_specialNumber;
    EditText passwordEditText;
    String passwordEditTextValue = "", selectDobValue = "", phoneValue = "", codeValue = "", emailAddressEditTextValue = "", applicantNameValue, encodedImageData = "";
    File file;

    boolean isUpper = false, isLower = false, isNumber = false, isSpecial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_password);
        Utilities.setWhiteBars(UitMemberPasswordActivity.this);
        context = UitMemberPasswordActivity.this;
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        initViews();

        addEditTextChangeListener(passwordEditText);
    }

    private void initViews() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("fullName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            codeValue = getIntent().getStringExtra("verification");
            phoneValue = getIntent().getStringExtra("phoneNumber");
            selectDobValue = "31/12/1998";
            file = new File(getRealPathFromURI(Uri.parse(encodedImageData)));
        }

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        ivLength = findViewById(R.id.iv_length);
        text_characters = findViewById(R.id.text_characters);
        ivCase = findViewById(R.id.iv_case);
        text_upperLowerCase = findViewById(R.id.text_upperLowerCase);
        ivSpecial = findViewById(R.id.iv_num_special);
        text_specialNumber = findViewById(R.id.text_specialNumber);
        passwordEditText = findViewById(R.id.passwordEditText);

        loadProfile(UitMemberPasswordActivity.this, encodedImageData, menYellow);

        backIcon.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });

        next.setOnClickListener(v -> {
            hideSoftKeyboard(UitMemberPasswordActivity.this, passwordEditText);
            passwordEditTextValue = passwordEditText.getText().toString().trim();
            if (passwordEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(UitMemberPasswordActivity.this, "Please enter your password");
            } else if (!(passwordEditTextValue.length() >= 7 && isLower && isUpper && (isNumber || isSpecial))) {
                CustomCookieToast.showRequiredToast(UitMemberPasswordActivity.this, "Please enter valid password");
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

                        e.printStackTrace();
                        progressDialog.dismiss();
                        Snackbar.make(getCurrentFocus(), "uploadVideo: Error uploading image", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(getCurrentFocus(), "Error uploading video", Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                progressDialog.dismiss();
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
                openNextScreen();

            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openNextScreen() {
        Intent intent = new Intent(UitMemberPasswordActivity.this, UitMemberPendingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void addEditTextChangeListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isUpper = false;
                isLower = false;
                isNumber = false;
                isSpecial = false;

                // length
                setTick(s.length() >= 7, ivLength, text_characters);
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);

                    if (c >= 'a' && c <= 'z') {
                        isLower = true;
                    }

                    if (c >= 'A' && c <= 'Z') {
                        isUpper = true;
                    }

                    if (c >= '0' && c <= '9') {
                        isNumber = true;
                    }

                    if ("!@#$%^&*().,?-_+=`~".contains(String.valueOf(c))) {
                        isSpecial = true;
                    }
                }

                // upper and lower
                setTick(isLower && isUpper, ivCase, text_upperLowerCase);
                // number of special
                setTick(isNumber || isSpecial, ivSpecial, text_specialNumber);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setTick(boolean enabled, ImageView imageView, TextView textView) {
        if (enabled) {
            imageView.setImageResource(R.drawable.checked_ic);
            textView.setTextColor(getColor(R.color.app_color));
        } else {
            imageView.setImageResource(R.drawable.not_checked_ic);
            textView.setTextColor(getColor(R.color.gray));
        }
    }
}