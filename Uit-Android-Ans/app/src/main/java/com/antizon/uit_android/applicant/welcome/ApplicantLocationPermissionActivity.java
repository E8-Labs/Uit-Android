package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;

public class ApplicantLocationPermissionActivity extends BaseActivity {

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    TextView allowLocation;
    ImageView backIcon;

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    boolean mLocationPermissionGranted = false;

    ArrayList<ModelCompanySize> selectedProfessionalInterestList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInSizeList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    String employeValue = "", from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_location_permission);
        Utilities.setWhiteBars(ApplicantLocationPermissionActivity.this);

        initViews();
    }


    private void initViews() {

        allowLocation = findViewById(R.id.allowLocation);
        backIcon = findViewById(R.id.backIcon);

        from = getIntent().getStringExtra("from");
        employeValue = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
        locationList = getIntent().getParcelableArrayListExtra("locationList");
        interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");
        selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
        selectedCompanyInterestInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInSizeList");
        selectedProfessionalInterestList = getIntent().getParcelableArrayListExtra("selectedProfessionalInterestList");

        progressDialog = new ProgressDialog(ApplicantLocationPermissionActivity.this);
        sessionManagement = new SessionManagement(ApplicantLocationPermissionActivity.this);

        backIcon.setOnClickListener(v -> onBackPressed());

        allowLocation.setOnClickListener(v -> checkIsUserLocationPermissionGranted());
    }


    public void checkIsUserLocationPermissionGranted() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(ApplicantLocationPermissionActivity.this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(ApplicantLocationPermissionActivity.this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                isLocationEnabled();
            } else {
                ActivityCompat.requestPermissions(ApplicantLocationPermissionActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(ApplicantLocationPermissionActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void isLocationEnabled() {
        if (!canGetLocation()) {
            showSettingsAlert();
        } else {
            Intent intent = new Intent(ApplicantLocationPermissionActivity.this, ApplicantNotificationPermissionActivity.class);
            intent.putExtra("from", from);
            intent.putExtra("employeStatus", employeValue);
            intent.putParcelableArrayListExtra("educationList", educationList);
            intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
            intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
            intent.putParcelableArrayListExtra("locationList", locationList);
            intent.putParcelableArrayListExtra("interestedJobTypeList", interestedJobTypeList);
            intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
            intent.putParcelableArrayListExtra("selectedCompanyInterestInSizeList", selectedCompanyInterestInSizeList);
            intent.putParcelableArrayListExtra("selectedProfessionalInterestList", selectedProfessionalInterestList);
            onProfileUpdateLauncher.launch(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ApplicantLocationPermissionActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Location!");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is disabled. To enable again go to settings.");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                });

        alertDialog.show();
    }


    public boolean canGetLocation() {
        boolean result;
        LocationManager lm;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        result = gps_enabled && network_enabled;

        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isLocationEnabled();
                        return;
                    }
                }
                mLocationPermissionGranted = true;
                isLocationEnabled();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}