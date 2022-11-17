package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantSelectJobActivity;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.work.ApplicantReferenceModel;
import com.antizon.uit_android.utilities.CountriesModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.hbb20.CountryCodePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AddApplicantReferenceActivity extends AppCompatActivity {
    Context context;

    RelativeLayout btnBack;

    TextView countryCode, text_jobTitle, btnSave;
    EditText editText_nameOfReference, phoneNumber;

    String selectedCountryCode;
    CountryCodePicker selectCountryCode;
    List<CountriesModel> modelList;
    ApplicantJobDataModel applicantJobDataModel;
    int maxLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_applicant_reference);
        Utilities.setCustomStatusAndNavColor(AddApplicantReferenceActivity.this, R.color.white_dash, R.color.white_dash);
        context  = AddApplicantReferenceActivity.this;

        btnBack = findViewById(R.id.btnBack);
        phoneNumber = findViewById(R.id.phoneNumber);
        countryCode = findViewById(R.id.countryCode);
        selectCountryCode = findViewById(R.id.selectCountryCode);
        editText_nameOfReference = findViewById(R.id.editText_nameOfReference);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        btnSave = findViewById(R.id.btnSave);

        btnBack.setOnClickListener(v -> onBackPressed());

        text_jobTitle.setOnClickListener(v -> {
            Intent selectJobIntent = new Intent(context, ApplicantSelectJobActivity.class);
            onSelectedJobTitleLauncher.launch(selectJobIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });


        initPhoneNumber();

        btnSave.setOnClickListener(v -> {
            String name = editText_nameOfReference.getText().toString();
            String phone = phoneNumber.getText().toString();
            String jobTitle = text_jobTitle.getText().toString();

            if (name.isEmpty()){
                CustomCookieToast.showRequiredToast(AddApplicantReferenceActivity.this, "Please enter name of reference");
            }else if (jobTitle.isEmpty()){
                CustomCookieToast.showRequiredToast(AddApplicantReferenceActivity.this, "Please enter job title");
            }else if (phone.isEmpty()){
                CustomCookieToast.showRequiredToast(AddApplicantReferenceActivity.this, "Please enter phone number");
            }else if (phone.length() < maxLength) {
                CustomCookieToast.showRequiredToast(AddApplicantReferenceActivity.this, "Please enter complete phone number");
            }else {
                Utilities.hideKeyboard(v, context);
                Intent intent = new Intent();
                intent.putExtra("applicantReferenceModel", new ApplicantReferenceModel(name, selectedCountryCode, phone, applicantJobDataModel));
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
            }

        });
    }

    private void initPhoneNumber(){
        modelList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(Utilities.loadJSONFromAsset(AddApplicantReferenceActivity.this));
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

        selectedCountryCode = selectCountryCode.getSelectedCountryCodeWithPlus();
        countryCode.setText(selectedCountryCode);
        setPhoneNumberValidation(selectedCountryCode);

        selectCountryCode.setOnCountryChangeListener(() -> {
            selectedCountryCode = selectCountryCode.getSelectedCountryCodeWithPlus();
            countryCode.setText(selectedCountryCode);
            setPhoneNumberValidation(selectedCountryCode);

        });
    }

    private void setPhoneNumberValidation(String selectedCountryCode){
        for (CountriesModel countriesModel : modelList){
            String dialCode = countriesModel.getDial_code();
            if (dialCode.equals(selectedCountryCode)){
                maxLength = Integer.parseInt(countriesModel.getDigit1());
                String format = countriesModel.getFormat();
                phoneNumber.setHint(format);
                phoneNumber.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    ActivityResultLauncher<Intent> onSelectedJobTitleLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                applicantJobDataModel = intent.getParcelableExtra("applicantJobDataModel");
                text_jobTitle.setText(applicantJobDataModel.getName());
            }
        }
    });

}