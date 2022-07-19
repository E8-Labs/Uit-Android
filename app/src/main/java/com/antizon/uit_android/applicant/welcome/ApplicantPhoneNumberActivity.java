package com.antizon.uit_android.applicant.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.CountriesModel;
import com.antizon.uit_android.utilities.Utilities;
import com.hbb20.CountryCodePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplicantPhoneNumberActivity extends BaseActivity {
    private static final String TAG = ApplicantFullNameActivity.class.getSimpleName();
    ImageView backIcon, menYellow;
    TextView next,countryCode;
    EditText phone;
    String phoneValue = "", emailAddressEditTextValue = "", applicantNameValue, encodedImageData = "",selectedCountryCode;

    CountryCodePicker selectCountryCode;
    List<CountriesModel> modelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_phone_number);
        Utilities.setWhiteBars(ApplicantPhoneNumberActivity.this);

        setIds();
        getIntentData();
        initialize();
        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        menYellow = findViewById(R.id.menYellow);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        phone = findViewById(R.id.phone);
        countryCode = findViewById(R.id.countryCode);
        selectCountryCode = findViewById(R.id.selectCountryCode);

        modelList = new ArrayList<>();
    }

    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(ApplicantPhoneNumberActivity.this, encodedImageData, menYellow);
    }


    void setListener() {

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            phoneValue = phone.getText().toString().trim();
            phoneValue = selectedCountryCode + " " + phoneValue;
            openNextScreen();
        });

        try {
            JSONArray jsonArray = new JSONArray(Utilities.loadJSONFromAsset(ApplicantPhoneNumberActivity.this));
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
                int maxLength = Integer.parseInt(countriesModel.getDigit1());
                String format = countriesModel.getFormat();
                phone.setHint(format);
                phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            }
        }
    }


    private void openNextScreen() {
        Intent intent = new Intent(ApplicantPhoneNumberActivity.this, ApplicantDateOfBirthActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        intent.putExtra("email", emailAddressEditTextValue);
        intent.putExtra("phoneNumber", phoneValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}
