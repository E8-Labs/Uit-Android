package com.antizon.uit_android.company.activities.postjob;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.filter.SingleSelectionAdapter;
import com.antizon.uit_android.applicant.welcome.ApplicantSelectDegreeActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantSelectDepartmentActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantSelectLanguageActivity;
import com.antizon.uit_android.company.welcome.SelectCompanyIndustryActivity;
import com.antizon.uit_android.generic.activities.SelectLocationActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.filter.MultiSelectionModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.slider.RangeSlider;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompanyPostJobActivity extends AppCompatActivity implements SingleSelectionAdapter.SingleSelectionAdapterCallBack {
    public static final int SELECT_USER_LOCATION = 101;
    FusedLocationProviderClient client;
    
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout layout_main, btnBack, btnSelectIndustry, btnSelectDepartment, btnSelectLocation, layout_permissionDenied, btnSettings, btnSelectEducation, btnSelectLanguage, btnVaccinationRequired;
    TextView btnNext, text_companyName, text_industryName, text_departmentName, text_location, text_minEducation, text_selectLanguage;
    LinearLayout btn_educationRequired, btn_languageRequired;
    ImageView education_required_ic, language_required_ic, vaccination_required_ic;
    RoundedImageView company_profileImage;
    EditText editText_jobTitle;
    RangeSlider slider_salaryRange;
    RecyclerView recyclerview_jobLocation, recyclerview_employmentType;
    List<MultiSelectionModel> locationList, employmentList;
    SingleSelectionAdapter locationAdapter, employmentAdapter;

    MultiSelectionModel selectedLocation, selectedEmployment;
    ApplicantDegreeDataModel selectedIndustryModel, selectedDepartmentModel, selectedDegreeModel, selectedLanguageModel;
    double latitude = 31.5204, longitude = 74.3587;
    String city, country, state;
    boolean isLocationSelected, isEmploymentSelected, isEducationRequired = true, isLanguageRequired = true, isVaccinationRequired;

    String from;
    JobDetailsDataModel jobDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_post_job);
        Utilities.setWhiteBars(CompanyPostJobActivity.this);
        context = CompanyPostJobActivity.this;
        sessionManagement = new SessionManagement(context);
        client = LocationServices.getFusedLocationProviderClient(context);
        initViews();
    }

    private void initViews(){
        from = getIntent().getStringExtra("from");

        if (from.equals("edit")){
            jobDetails = getIntent().getParcelableExtra("jobDetail");
        }

        layout_main = findViewById(R.id.layout_main);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        company_profileImage = findViewById(R.id.company_profileImage);
        text_companyName = findViewById(R.id.text_companyName);
        editText_jobTitle = findViewById(R.id.editText_jobTitle);
        btnSelectIndustry = findViewById(R.id.btnSelectIndustry);
        text_industryName = findViewById(R.id.text_industryName);
        btnSelectDepartment = findViewById(R.id.btnSelectDepartment);
        text_departmentName = findViewById(R.id.text_departmentName);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        text_location = findViewById(R.id.text_location);
        layout_permissionDenied = findViewById(R.id.layout_permissionDenied);
        btnSettings = findViewById(R.id.btnSettings);
        recyclerview_jobLocation = findViewById(R.id.recyclerview_jobLocation);
        recyclerview_employmentType = findViewById(R.id.recyclerview_employmentType);
        slider_salaryRange = findViewById(R.id.slider_salaryRange);
        btnSelectEducation = findViewById(R.id.btnSelectEducation);
        text_minEducation = findViewById(R.id.text_minEducation);
        btn_educationRequired = findViewById(R.id.btn_educationRequired);
        education_required_ic = findViewById(R.id.education_required_ic);
        btnSelectLanguage = findViewById(R.id.btnSelectLanguage);
        text_selectLanguage = findViewById(R.id.text_selectLanguage);
        btn_languageRequired = findViewById(R.id.btn_languageRequired);
        language_required_ic = findViewById(R.id.language_required_ic);
        btnVaccinationRequired = findViewById(R.id.btnVaccinationRequired);
        vaccination_required_ic = findViewById(R.id.vaccination_required_ic);

        slider_salaryRange.setEnabled(true);
        slider_salaryRange.setLabelFormatter(value -> (int) value + "K");

        setJobLocationAndEmploymentRecyclerview();

        btnBack.setOnClickListener(v -> onBackPressed());

        btnNext.setOnClickListener(v -> {

            for (int i = 0; i < locationList.size(); i++) {
                if (locationList.get(i).isSelected()){
                    selectedLocation = locationList.get(i);
                    isLocationSelected = true;
                    break;
                }
            }
            for (int i = 0; i < employmentList.size(); i++) {
                if (employmentList.get(i).isSelected()){
                    selectedEmployment = employmentList.get(i);
                    isEmploymentSelected = true;
                    break;
                }
            }


            String jobTitle = editText_jobTitle.getText().toString();
            String jobIndustry = text_industryName.getText().toString();
            String jobDepartment = text_departmentName.getText().toString();
            String jobLocation = text_location.getText().toString();
            String jobMinEducation = text_minEducation.getText().toString();
            String jobLanguage = text_selectLanguage.getText().toString();

            int minSalary = slider_salaryRange.getValues().get(0).intValue();
            int maxSalary = slider_salaryRange.getValues().get(1).intValue();

            if (jobTitle.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please enter job title");
            }else if (jobIndustry.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select job industry");
            }else if (jobDepartment.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select job department");
            }else if (jobLocation.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select job location");
            }else if (!isLocationSelected){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select location status");
            }else if (!isEmploymentSelected){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select employment status");
            }else if (jobMinEducation.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select minimum education");
            }else if (jobLanguage.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobActivity.this, "Please select job preferred language");
            }else {
                Utilities.hideKeyboard(editText_jobTitle, context);
                moveToNextScreen(jobTitle, jobLocation, minSalary, maxSalary);
            }
        });

        btnSelectIndustry.setOnClickListener(v -> {
            Intent selectIndustries = new Intent(context, SelectCompanyIndustryActivity.class);
            onSelectedIndustryLauncher.launch(selectIndustries);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnSelectDepartment.setOnClickListener(v -> {
            Intent selectIndustries = new Intent(context, ApplicantSelectDepartmentActivity.class);
            onSelectedDepartmentLauncher.launch(selectIndustries);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnSelectEducation.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectDegreeActivity.class);
            onSelectedCategoryLauncher.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        btnSelectLanguage.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectLanguageActivity.class);
            onSelectedLanguageLauncher.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        btnSelectLocation.setOnClickListener(v -> {
            Intent selectionIntent = new Intent(context, SelectLocationActivity.class);
            selectionIntent.putExtra("latitude", latitude);
            selectionIntent.putExtra("longitude", longitude);
            startActivityIfNeeded(selectionIntent, SELECT_USER_LOCATION);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        btn_educationRequired.setOnClickListener(v -> {
            if (!isEducationRequired){
                btnSelectEducation.setEnabled(true);
                education_required_ic.setImageResource(R.drawable.not_checked_ic);
                isEducationRequired = true;
            }else {
                btnSelectEducation.setEnabled(false);
                education_required_ic.setImageResource(R.drawable.checked_ic);
                isEducationRequired = false;
            }
        });

        btn_languageRequired.setOnClickListener(v -> {
            if (!isLanguageRequired){
                btnSelectLanguage.setEnabled(true);
                language_required_ic.setImageResource(R.drawable.not_checked_ic);
                isLanguageRequired = true;
            }else {
                btnSelectLanguage.setEnabled(false);
                language_required_ic.setImageResource(R.drawable.checked_ic);
                isLanguageRequired = false;
            }
        });

        btnVaccinationRequired.setOnClickListener(v -> {
            if (!isVaccinationRequired){
                vaccination_required_ic.setImageResource(R.drawable.checked_ic);
                isVaccinationRequired = true;
            }else {
                vaccination_required_ic.setImageResource(R.drawable.not_checked_ic);
                isVaccinationRequired = false;
            }
        });


        if (from.equals("edit")){
            selectedIndustryModel = new ApplicantDegreeDataModel(jobDetails.getIndustry().getId(), jobDetails.getIndustry().getName(), "", "", "");
            selectedDepartmentModel = new ApplicantDegreeDataModel(jobDetails.getDepartment().getId(), jobDetails.getDepartment().getName(), "", "", "");
            selectedDegreeModel = new ApplicantDegreeDataModel(jobDetails.getDegree().getId(), jobDetails.getDepartment().getName(), "", "", "");
            selectedLanguageModel = new ApplicantDegreeDataModel(jobDetails.getLanguage().getId(), jobDetails.getLanguage().getName(), "", "", "");

            Utilities.loadImage(context, jobDetails.getCompany().getProfileImage(), company_profileImage);
            text_companyName.setText(jobDetails.getCompany().getName());
            editText_jobTitle.setText(jobDetails.getJobTitle());

            text_industryName.setText(selectedIndustryModel.getName());
            text_departmentName.setText(selectedDepartmentModel.getName());
            text_minEducation.setText(selectedDegreeModel.getName());
            text_selectLanguage.setText(selectedLanguageModel.getName());

            if (jobDetails.getDegreeRequired() == 1){
                btnSelectEducation.setEnabled(true);
                education_required_ic.setImageResource(R.drawable.not_checked_ic);
                isEducationRequired = true;
            }else {
                btnSelectEducation.setEnabled(false);
                education_required_ic.setImageResource(R.drawable.checked_ic);
                isEducationRequired = false;
            }


            if (jobDetails.getLanguageRequired() == 1){
                btnSelectLanguage.setEnabled(true);
                language_required_ic.setImageResource(R.drawable.not_checked_ic);
                isLanguageRequired = true;
            }else {
                btnSelectLanguage.setEnabled(false);
                language_required_ic.setImageResource(R.drawable.checked_ic);
                isLanguageRequired = false;
            }

            if (jobDetails.getVaccinationRequired() ==1){
                vaccination_required_ic.setImageResource(R.drawable.checked_ic);
                isVaccinationRequired = true;
            }else {
                vaccination_required_ic.setImageResource(R.drawable.not_checked_ic);
                isVaccinationRequired = false;
            }

            latitude = jobDetails.getLat();
            longitude = jobDetails.getLang();

            String address = jobDetails.getCity() + " , " + jobDetails.getState();
            text_location.setText(address);

            for (int i=0; i < locationList.size(); i++){
                if (jobDetails.getLocationStatus() == locationList.get(i).getValue()){
                    locationList.get(i).setSelected(true);
                    selectedLocation = locationList.get(i);
                    break;
                }
            }

            for (int i=0; i < employmentList.size(); i++){
                if (jobDetails.getEmploymentType() == employmentList.get(i).getValue()){
                    employmentList.get(i).setSelected(true);
                    selectedEmployment = employmentList.get(i);
                    break;
                }
            }

            slider_salaryRange.setValues(jobDetails.getMinSalary()/1000f, jobDetails.getMaxSalary()/1000f);


        }else {
            Utilities.loadImage(context, sessionManagement.getProfileImage(), company_profileImage);
            text_companyName.setText(sessionManagement.getUserName());
        }
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                layout_main.setVisibility(View.VISIBLE);
                layout_permissionDenied.setVisibility(View.GONE);
                client.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }else{
                        LocationRequest locationRequest = LocationRequest.create()
                                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setMaxWaitTime(100)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                if (location1 != null) {
                                    latitude = location1.getLatitude();
                                    longitude = location1.getLongitude();
                                }
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        if (latitude == 0 && longitude == 0) {
                            getCurrentLocation();
                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }

                    }

                    //Get Address
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(context, Locale.getDefault());
                    if (latitude != 0 ){
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            city = addresses.get(0).getLocality();
                            country = addresses.get(0).getCountryName();
                            state = addresses.get(0).getAdminArea();
                            String address = city + " , " + state;
                            text_location.setText(address);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                layout_main.setVisibility(View.GONE);
                layout_permissionDenied.setVisibility(View.VISIBLE);
                btnSettings.setOnClickListener(v -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                });

            }

        } else {
            layout_main.setVisibility(View.GONE);
            layout_permissionDenied.setVisibility(View.VISIBLE);
            btnSettings.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            layout_main.setVisibility(View.VISIBLE);
            layout_permissionDenied.setVisibility(View.GONE);
            getCurrentLocation();
        } else {
            layout_main.setVisibility(View.GONE);
            layout_permissionDenied.setVisibility(View.VISIBLE);
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure the request was successful
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_USER_LOCATION){
                if (data !=null){
                    city = data.getStringExtra("selectedCity");
                    state = data.getStringExtra("selectedState");

                    String selectedLocation = city + ", " + state;
                    latitude = data.getDoubleExtra("selectedLatitude", 31.5204);
                    longitude = data.getDoubleExtra("selectedLongitude", 74.3587);
                    text_location.setText(selectedLocation);
                }

            }
        }
    }

    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }


    private void setJobLocationAndEmploymentRecyclerview() {
        // ------------------------ Locations ---------------------------
        locationList = new ArrayList<>();
        locationList.add(new MultiSelectionModel("Remote", 1));
        locationList.add(new MultiSelectionModel("Hybrid", 2));
        locationList.add(new MultiSelectionModel("InPerson", 3));

        recyclerview_jobLocation.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        locationAdapter = new SingleSelectionAdapter(context, locationList, "location", this);
        recyclerview_jobLocation.setAdapter(locationAdapter);
        
        //---------------------- Employment --------------------------
        employmentList = new ArrayList<>();
        employmentList.add(new MultiSelectionModel("Full Time", 1));
        employmentList.add(new MultiSelectionModel("Part Time", 2));
        employmentList.add(new MultiSelectionModel("Contract", 3));
        employmentList.add(new MultiSelectionModel("Freelance", 4));
        employmentList.add(new MultiSelectionModel("Internship", 5));

        recyclerview_employmentType.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        employmentAdapter = new SingleSelectionAdapter(context, employmentList, "employment", this);
        recyclerview_employmentType.setAdapter(employmentAdapter);
        
    }

    ActivityResultLauncher<Intent> onSelectedIndustryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                selectedIndustryModel = intent.getParcelableExtra("degreeDataModel");
                text_industryName.setText(selectedIndustryModel.getName());
            }
        }
    });

    ActivityResultLauncher<Intent> onSelectedDepartmentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                selectedDepartmentModel = intent.getParcelableExtra("degreeDataModel");
                text_departmentName.setText(selectedDepartmentModel.getName());
            }
        }
    });

    ActivityResultLauncher<Intent> onSelectedCategoryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                selectedDegreeModel = intent.getParcelableExtra("degreeDataModel");
                text_minEducation.setText(selectedDegreeModel.getName());
            }
        }
    });

    ActivityResultLauncher<Intent> onSelectedLanguageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                selectedLanguageModel = intent.getParcelableExtra("degreeDataModel");
                text_selectLanguage.setText(selectedLanguageModel.getName());
            }
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemSelected(int position, String type) {
        if (type.equals("location")){
            for (int i = 0; i < locationList.size(); i++) {
                locationList.get(i).setSelected(i == position);
            }
            locationAdapter.notifyDataSetChanged();
        }else {
            for (int i = 0; i < employmentList.size(); i++) {
                employmentList.get(i).setSelected(i == position);
            }
            employmentAdapter.notifyDataSetChanged();
        }
    }

    private void moveToNextScreen(String jobTitle, String jobLocation, int minSalary, int maxSalary){
        Intent jobPostIntent = new Intent(context, CompanyPostJobStep2Activity.class);
        jobPostIntent.putExtra("jobTitle", jobTitle);
        jobPostIntent.putExtra("jobIndustry", selectedIndustryModel);
        jobPostIntent.putExtra("jobDepartment", selectedDepartmentModel);
        jobPostIntent.putExtra("jobLocation", jobLocation);
        jobPostIntent.putExtra("city", city);
        jobPostIntent.putExtra("state", state);
        jobPostIntent.putExtra("latitude", latitude);
        jobPostIntent.putExtra("longitude", longitude);
        jobPostIntent.putExtra("selectedLocation", selectedLocation);
        jobPostIntent.putExtra("selectedEmployment", selectedEmployment);
        jobPostIntent.putExtra("minSalary", minSalary);
        jobPostIntent.putExtra("maxSalary", maxSalary);
        jobPostIntent.putExtra("jobMinEducation", selectedDegreeModel);
        jobPostIntent.putExtra("isEducationRequired", isEducationRequired);
        jobPostIntent.putExtra("selectedLanguageModel", selectedLanguageModel);
        jobPostIntent.putExtra("isLanguageRequired", isLanguageRequired);
        jobPostIntent.putExtra("isVaccinationRequired", isVaccinationRequired);
        jobPostIntent.putExtra("from", from);
        if (from.equals("edit")){
            jobPostIntent.putExtra("jobDetail", jobDetails);
        }
        onJobPostedLauncher.launch(jobPostIntent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    ActivityResultLauncher<Intent> onJobPostedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}