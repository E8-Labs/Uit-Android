package com.antizon.uit_android.company.welcome;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.activities.SelectLocationActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompanyHeadquatersActivity extends AppCompatActivity {
    Context context;
    public static final int SELECT_USER_LOCATION = 101;
    FusedLocationProviderClient client;

    ImageView backIcon,redNoah2;
    TextView next, text_location;
    RelativeLayout btnSelectLocation, layout_permissionDenied, btnSettings;
    ConstraintLayout layout_main;

    String encodedImageData = "",  companyNameHintValue = "", typeHereValue = "", headquarterValue="";
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList, selectedCompanyInSizeList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;
    String city, state;
    double latitude = 31.5204, longitude = 74.3587;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_headquaters);
        Utilities.setWhiteBars(CompanyHeadquatersActivity.this);
        context = CompanyHeadquatersActivity.this;
        client = LocationServices.getFusedLocationProviderClient(context);

        initViews();
    }

    private void initViews() {
        selectedCompanyIndustries = new ArrayList<>();
        selectedCompanyInterestInStageList = new ArrayList<>();
        selectedCompanyInSizeList = new ArrayList<>();

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
            selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
        }

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        text_location = findViewById(R.id.text_location);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        layout_permissionDenied = findViewById(R.id.layout_permissionDenied);
        btnSettings = findViewById(R.id.btnSettings);
        layout_main = findViewById(R.id.layout_main);


        Utilities.loadImage(CompanyHeadquatersActivity.this, encodedImageData, redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            headquarterValue = text_location.getText().toString().trim();
            if (headquarterValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyHeadquatersActivity.this, "Please enter company headquarters");
            } else {
                openNextScreen();
            }
        });


        btnSelectLocation.setOnClickListener(v -> {
            Intent selectionIntent = new Intent(context, SelectLocationActivity.class);
            selectionIntent.putExtra("latitude", latitude);
            selectionIntent.putExtra("longitude", longitude);
            startActivityIfNeeded(selectionIntent, SELECT_USER_LOCATION);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

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

    private void openNextScreen() {
        Intent intent = new Intent(CompanyHeadquatersActivity.this, CompanyWebsiteActivity.class);
        intent.putExtra("from", "add");
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInSizeList", selectedCompanyInSizeList);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
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

                    latitude = data.getDoubleExtra("selectedLatitude", 31.5204);
                    longitude = data.getDoubleExtra("selectedLongitude", 74.3587);

                    String address = city + " , " + state;
                    text_location.setText(address);
                }

            }
        }
    }

}