package com.antizon.uit_android.recruiter.welcome;


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
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruiterPasswordActivity extends BaseActivity {
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;


    ImageView backIcon, menYellow,  ivLength, ivCase, ivSpecial;
    TextView next,  text_characters, text_upperLowerCase, text_specialNumber;
    EditText passwordEditText;

    String profilePic = "", fullName = "", recruiterEmail = "", phoneValue = "", passwordValue = "", companyId = "";
    ApplicantJobDataModel recruiterJobDataModel;

    boolean isUpper = false, isLower = false, isNumber = false, isSpecial = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_password);
        Utilities.setWhiteBars(RecruiterPasswordActivity.this);
        context = RecruiterPasswordActivity.this;
        progressDialog = new ProgressDialog(RecruiterPasswordActivity.this);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        initViews();

        addEditTextChangeListener(passwordEditText);
    }


    private void initViews() {


        profilePic = getIntent().getStringExtra("profilePic");
        fullName = getIntent().getStringExtra("fullName");
        recruiterEmail=getIntent().getStringExtra("recruiterEmail");
        recruiterJobDataModel= getIntent().getParcelableExtra("recruiterJobDataModel");
        phoneValue = getIntent().getStringExtra("phoneValue");
        companyId = getIntent().getStringExtra("companyId");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        passwordEditText = findViewById(R.id.passwordEditText);
        menYellow = findViewById(R.id.menYellow);
        ivLength = findViewById(R.id.iv_length);
        text_characters = findViewById(R.id.text_characters);
        ivCase = findViewById(R.id.iv_case);
        text_upperLowerCase = findViewById(R.id.text_upperLowerCase);
        ivSpecial = findViewById(R.id.iv_num_special);
        text_specialNumber = findViewById(R.id.text_specialNumber);

        Utilities.loadCircleImage(RecruiterPasswordActivity.this, profilePic, menYellow);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            passwordValue = passwordEditText.getText().toString().trim();
            if (passwordValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(RecruiterPasswordActivity.this, "Please enter password");
            }else if (!(passwordValue.length() >= 7 && isLower && isUpper && (isNumber || isSpecial))) {
                CustomCookieToast.showRequiredToast(RecruiterPasswordActivity.this, "Please enter valid password");
            } else {
                Utilities.hideKeyboard(passwordEditText, RecruiterPasswordActivity.this);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                requestForCreateCompanyTeamMemberAccount(recruiterEmail, passwordValue, phoneValue, fullName, recruiterJobDataModel.getName(), companyId, getRealPathFromURI(Uri.parse(profilePic)) );
            }
        });
    }

    private void openNextScreen() {
        Intent intent = new Intent(RecruiterPasswordActivity.this, RecruiterCongratulationsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    private void requestForCreateCompanyTeamMemberAccount(String email, String password, String phone, String name, String job_title, String company_id, String profileImage) {


        RequestBody requestBodyEmail = RequestBody.create(email, MediaType.parse("text/plain"));
        RequestBody requestBodyPassword = RequestBody.create(password, MediaType.parse("text/plain"));
        RequestBody requestBodyName = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody requestBodyPhone = RequestBody.create(phone, MediaType.parse("text/plain"));
        RequestBody requestBodyJobTitle = RequestBody.create(job_title, MediaType.parse("text/plain"));
        RequestBody requestBodyCompanyId = RequestBody.create(company_id, MediaType.parse("text/plain"));

        File image = new File(profileImage);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part profileImageMultiPart = MultipartBody.Part.createFormData("profile_image", image.getName(), requestBodyPostMedia);

        Call<MainResponseModel> call = service.registerCompanyTeamMember(requestBodyEmail, requestBodyPassword, requestBodyPhone, requestBodyName, requestBodyJobTitle, requestBodyCompanyId, profileImageMultiPart);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        openNextScreen();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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