package com.antizon.uit_android.recruiter.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CountriesModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruiterPhoneNumberActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;
    GetDataService service;
    ProgressDialog progressDialog;

    ImageView backIcon,menYellow;
    TextView next,countryCode;
    EditText phone;

    String profilePic = "", fullName = "", recruiterEmail = "", phoneValue = "", selectedCountryCode = "", companyId = "", from, companyTeamMemberPhone;
    ApplicantJobDataModel recruiterJobDataModel;

    CountryCodePicker selectCountryCode;
    List<CountriesModel> modelList;
    int maxLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_phone_number_activity);
        Utilities.setWhiteBars(RecruiterPhoneNumberActivity.this);
        context= RecruiterPhoneNumberActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        initViews();
    }


    private void initViews() {
        modelList = new ArrayList<>();

        from = getIntent().getStringExtra("from");
        if (from.equals("add")){
            companyId = getIntent().getStringExtra("companyId");
            profilePic = getIntent().getStringExtra("profilePic");
            fullName = getIntent().getStringExtra("fullName");
            recruiterEmail=getIntent().getStringExtra("recruiterEmail");
            recruiterJobDataModel= getIntent().getParcelableExtra("recruiterJobDataModel");
        }else{
            sessionManagement = new SessionManagement(context);
            companyTeamMemberPhone = getIntent().getStringExtra("companyTeamMemberPhone");
        }


        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        phone = findViewById(R.id.phone);
        menYellow = findViewById(R.id.menYellow);
        countryCode = findViewById(R.id.countryCode);
        selectCountryCode = findViewById(R.id.selectCountryCode);


        if (from.equals("add")){
            loadProfile(context, profilePic, menYellow);
        }else {
            Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), menYellow);
        }

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            phoneValue = phone.getText().toString().trim();

            if (phoneValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(RecruiterPhoneNumberActivity.this, "Please enter your phone number");
            }else if (phoneValue.length() < maxLength) {
                CustomCookieToast.showRequiredToast(RecruiterPhoneNumberActivity.this, "Please enter your complete phone number");
            } else {
                hideSoftKeyboard(context, phone);
                phoneValue = selectedCountryCode + " " + phoneValue;
                if (from.equals("edit")){
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    requestForUpdateRecruiterPhone("Bearer " + sessionManagement.getToken(), phoneValue);
                }else {
                    openNextScreen();
                }
            }
        });


        try {
            JSONArray jsonArray = new JSONArray(Utilities.loadJSONFromAsset(RecruiterPhoneNumberActivity.this));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo_inside = jsonArray.getJSONObject(i);
                String name = jo_inside.optString("name");
                String code = jo_inside.optString("code");
                String flag = jo_inside.optString("flag");
                String format = jo_inside.optString("format");
                String dial_code = jo_inside.optString("dial_code");
                String digit1 = jo_inside.optString("digit1");
                String digit2 = jo_inside.optString("digit2");

                modelList.add(new CountriesModel(name, code, format, flag, dial_code, digit1, digit2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("UitMemberPhoneNumberActivity", e.getMessage());
        }


        if (from.equals("edit")){
            if (companyTeamMemberPhone !=null){
                String[] phoneNumbers = companyTeamMemberPhone.split(" ");
                if (phoneNumbers.length >= 2){
                    selectedCountryCode = phoneNumbers[0];

                    selectCountryCode.setCountryForPhoneCode(Integer.parseInt(selectedCountryCode.replace("+", "")));

                    selectedCountryCode = selectCountryCode.getSelectedCountryCodeWithPlus();
                    countryCode.setText(selectedCountryCode);
                    setPhoneNumberValidation(selectedCountryCode);


                    phone.setText(phoneNumbers[1].replace("-","").replace(" ", ""));
                }else {
                    phone.setHint("000-000-0000");
                    phone.setText("");

                    selectedCountryCode = selectCountryCode.getSelectedCountryCodeWithPlus();
                    countryCode.setText(selectedCountryCode);
                    setPhoneNumberValidation(selectedCountryCode);
                }
            }
        } else {
            selectedCountryCode = selectCountryCode.getSelectedCountryCodeWithPlus();
            countryCode.setText(selectedCountryCode);
            setPhoneNumberValidation(selectedCountryCode);
        }



        selectCountryCode.setOnCountryChangeListener(() -> {
            selectedCountryCode = selectCountryCode.getSelectedCountryCodeWithPlus();
            countryCode.setText(selectedCountryCode);
            setPhoneNumberValidation(selectedCountryCode);

        });
    }


    private void openNextScreen() {
        Intent intent = new Intent(RecruiterPhoneNumberActivity.this, RecruiterPasswordActivity.class);
        intent.putExtra("companyId", companyId);
        intent.putExtra("profilePic", profilePic);
        intent.putExtra("fullName", fullName);
        intent.putExtra("recruiterEmail", recruiterEmail);
        intent.putExtra("recruiterJobDataModel", recruiterJobDataModel);
        intent.putExtra("phoneValue", phoneValue);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    private void setPhoneNumberValidation(String selectedCountryCode){
        for (CountriesModel countriesModel : modelList){
            String dialCode = countriesModel.getDial_code();
            if (dialCode.equals(selectedCountryCode)){
                maxLength = Integer.parseInt(countriesModel.getDigit1());
                String format = countriesModel.getFormat();
                phone.setHint(format);
                phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            }
        }
    }

    private void requestForUpdateRecruiterPhone(String authToken,String phoneValue) {
        Call<MainResponseModel> call = service.updateUserPhoneNumber(authToken, phoneValue);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra("companyTeamMemberPhone", phoneValue);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
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
}