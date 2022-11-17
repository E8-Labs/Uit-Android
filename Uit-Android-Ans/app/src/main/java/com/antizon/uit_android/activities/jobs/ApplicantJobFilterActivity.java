package com.antizon.uit_android.activities.jobs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.filter.MultiSelectionAdapter;
import com.antizon.uit_android.adapters.filter.TagAdapter;
import com.antizon.uit_android.generic.activities.SelectLocationActivity;
import com.antizon.uit_android.models.applicant.filter.GetIndustriesResponse;
import com.antizon.uit_android.models.applicant.filter.IndustriesList;
import com.antizon.uit_android.models.applicant.filter.IndustryModel;
import com.antizon.uit_android.models.applicant.filter.MultiSelectionModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.MyMapUtils;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.slider.RangeSlider;
import java.util.ArrayList;
import java.util.Arrays;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantJobFilterActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHECK_SETTINGS = 100;
    private static final int REQUEST_CODE_LOCATION = 200;
    private static final int REQUEST_CODE_PERMISSION = 300;
    final Activity activity = ApplicantJobFilterActivity.this;
    Context context;
    GetDataService service;


    RangeSlider rangeSlider;
    View rlReset, rlAllowPermission, tvAllowPermission;
    RelativeLayout layout_cancel, layout_jobTitle, layout_location, rlApplyFilter;
    EditText etTitle;
    RecyclerView rvEmployment, rvLocation, rvTags;
    TextView edit_location, text_currentLocation, tvSelectIndustry;


    TagContainerLayout tagContainerLayout;
    TagAdapter tagAdapter;
    ArrayList<IndustryModel> tags;
    ArrayList<MultiSelectionModel> employmentList;
    ArrayList<MultiSelectionModel> locationList;
    IndustriesList industriesList;

    double latitude, longitude;
    MultiSelectionAdapter locationAdapter, employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_filter);
        try {
            context = ApplicantJobFilterActivity.this;
            Utilities.setWhiteBars(ApplicantJobFilterActivity.this);
            service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

            tagContainerLayout = findViewById(R.id.tag_container_industries);
            rangeSlider = findViewById(R.id.rangeSlider);
            rlReset = findViewById(R.id.layout_resetFilter);
            tvSelectIndustry = findViewById(R.id.tv_select_industry);
            layout_cancel = findViewById(R.id.layout_cancel);
            layout_jobTitle = findViewById(R.id.layout_jobTitle);
            layout_location = findViewById(R.id.layout_location);
            etTitle = findViewById(R.id.edit_productDesigner);
            edit_location = findViewById(R.id.edit_location);
            rvEmployment = findViewById(R.id.rv_employment);
            text_currentLocation = findViewById(R.id.text_currentLocation);
            rvLocation = findViewById(R.id.rv_location);
            rlApplyFilter = findViewById(R.id.layout_applyFilter);
            rvTags = findViewById(R.id.rv_tags);
            rlAllowPermission = findViewById(R.id.rl_enable_location);
            tvAllowPermission = findViewById(R.id.tv_allow_permission);
            rangeSlider.setLabelFormatter(value -> (int) value + "K");

            tags = new ArrayList<>();
            reset();
            getCurrentLocation();
            getIndustries();
            setSelectionLists();

            rlReset.setOnClickListener(v -> reset());
            tvSelectIndustry.setOnClickListener(v -> openSelectIndustry());
            text_currentLocation.setOnClickListener(v -> getCurrentLocation());
            tvAllowPermission.setOnClickListener(v -> getCurrentLocation());
            rlApplyFilter.setOnClickListener(v -> moveToNext());
            layout_cancel.setOnClickListener(v -> back());

            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {
                }

                @Override
                public void onTagLongClick(int position, String text) {
                }

                @Override
                public void onSelectedTagDrag(int position, String text) {
                }

                @Override
                public void onTagCrossClick(int position) {
                    if (tags != null && tags.size() > position) {
                        tagContainerLayout.removeTag(position);
                        tags.remove(position);

                        if (tags.size() == 0){
                            tagContainerLayout.setVisibility(View.GONE);
                        }else{
                            tagContainerLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            layout_location.setOnClickListener(v -> {
                Intent selectionIntent = new Intent(context, SelectLocationActivity.class);
                selectionIntent.putExtra("latitude", latitude);
                selectionIntent.putExtra("longitude", longitude);
                onSelectedNewLocation.launch(selectionIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });

        } catch (Exception e) {
            Toast.makeText(activity, "Exception in on create : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void reset() {
        tagContainerLayout.removeAllTags();
        tagContainerLayout.setVisibility(View.GONE);
        if (tagAdapter != null) tagAdapter.clear();
        if (employeeAdapter != null) employeeAdapter.unselectAll();
        if (locationAdapter != null) locationAdapter.unselectAll();

        etTitle.setText("");
        edit_location.setText("");
        latitude = 0;
        longitude = 0;
        rangeSlider.setValues(Arrays.asList(50f, 400f));
    }

    private void getIndustries() {
        Call<GetIndustriesResponse> call = service.getIndustries();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetIndustriesResponse> call, @NonNull Response<GetIndustriesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            industriesList = new IndustriesList(response.body().getData());
                        } else {
                            CustomCookieToast.showFailureToast(activity, response.body().getMessage());
                        }
                    }

                } else {
                    CustomCookieToast.showFailureToast(activity, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetIndustriesResponse> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(activity, t.getMessage());
            }
        });
    }

    private void moveToNext() {
        ArrayList<String> filtersList = new ArrayList<>();

        String jobTitle = etTitle.getText().toString();
        String location = edit_location.getText().toString();
        int minSalary = rangeSlider.getValues().get(0).intValue();
        int maxSalary = rangeSlider.getValues().get(1).intValue();

        filtersList.add(location);

        ArrayList<String> industryIds = new ArrayList<>();
        for (IndustryModel industry : tags) {
            industryIds.add(industry.getId() + "");
        }

        ArrayList<String> employmentIds = new ArrayList<>();
        for (MultiSelectionModel model : employmentList) {
            if (model.isSelected()){
                employmentIds.add(model.getValue() + "");
                filtersList.add(model.getTitle());
            }

        }

        ArrayList<String> locationIds = new ArrayList<>();
        for (MultiSelectionModel model : locationList) {
            if (model.isSelected()){
                locationIds.add(model.getValue() + "");
                filtersList.add(model.getTitle());
            }

        }
        filtersList.add("$" + minSalary + "k - $" + maxSalary + "k");

        Intent intent = new Intent();
        intent.putStringArrayListExtra("industries", industryIds);//comma separated ids of industries
        intent.putExtra("jobTitle", jobTitle); // string
        intent.putExtra("location", location); // string
        intent.putExtra("latitude", latitude);// double
        intent.putExtra("longitude", longitude);// double
        intent.putStringArrayListExtra("employment", employmentIds);//comma separated ids of selected employment types
        intent.putStringArrayListExtra("work_location", locationIds);//comma separated ids of work location types
        intent.putExtra("min_salary", minSalary);//int
        intent.putExtra("max_salary", maxSalary);//int
        intent.putStringArrayListExtra("filtersList", filtersList);
        intent.putExtra("filterApplied", true);
        setResult(RESULT_OK, intent);
        back();
    }

    private void getCurrentLocation() {

        //First check for the permissions on runtime because these are dangerous permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "not granted!", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
            return;
        }

        showEnableLocation(false);

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context.getApplicationContext());
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    setLocation();
                } else {
                    LocationRequest locationRequest =  LocationRequest.create()
                            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10000)
                            .setFastestInterval(1000)
                            .setNumUpdates(1);

                    LocationCallback locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location1 = locationResult.getLastLocation();
                            if (location1 !=null){
                                latitude = location1.getLatitude();
                                longitude = location1.getLongitude();
                            }
                        }
                    };

                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    if (latitude == 0 && longitude == 0) {
                        checkLocationPermission();
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }

            });
        } else {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10000)
                    .setFastestInterval(1000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            LocationServices.getSettingsClient(context)
                    .checkLocationSettings(builder.build())
                    .addOnFailureListener(e -> {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            /* For Activity Use
                            resolvable.startResolutionForResult(context, REQUEST_CODE_CHECK_SETTINGS);*/
                            startIntentSenderForResult(resolvable.getResolution().getIntentSender(), REQUEST_CODE_CHECK_SETTINGS, null, 0, 0, 0, null);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    });
        }
    }

    private void setLocation() {
        String address = MyMapUtils.getCityCountry(context, new LatLng(latitude, longitude));
        edit_location.setText(address);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(permissions, REQUEST_CODE_LOCATION);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            boolean isGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    isGranted = false;
                    break;
                }
            }

            showEnableLocation(!isGranted);
        }
    }


    private void showEnableLocation(boolean shouldShow) {
        if (shouldShow)
            rlAllowPermission.setVisibility(View.VISIBLE);
        else
            rlAllowPermission.setVisibility(View.GONE);
    }

    private void openSelectIndustry() {
        Intent intent = new Intent(context, SelectIndustryActivity.class);
        intent.putExtra("list", industriesList);
        launcher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    private void setSelectionLists() {

        //---------------------- Employment --------------------------
        employmentList = new ArrayList<>();
        employmentList.add(new MultiSelectionModel("FullTime", 1));
        employmentList.add(new MultiSelectionModel("PartTime", 2));
        employmentList.add(new MultiSelectionModel("Contract", 3));
        employmentList.add(new MultiSelectionModel("Freelance", 4));
        employmentList.add(new MultiSelectionModel("Internship", 5));

        rvEmployment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        employeeAdapter = new MultiSelectionAdapter(context, employmentList);
        rvEmployment.setAdapter(employeeAdapter);


        // ------------------------ Locations ---------------------------
        locationList = new ArrayList<>();
        locationList.add(new MultiSelectionModel("Remote", 1));
        locationList.add(new MultiSelectionModel("Hybrid", 2));
        locationList.add(new MultiSelectionModel("InPerson", 3));

        rvLocation.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        locationAdapter = new MultiSelectionAdapter(context, locationList);
        rvLocation.setAdapter(locationAdapter);
    }

    private void addToTags(IndustryModel model) {
        tagContainerLayout.setVisibility(View.VISIBLE);
        tagContainerLayout.addTag(model.getName());
        if (tagAdapter == null) {
            tags = new ArrayList<>();
            tags.add(model);
            tagAdapter = new TagAdapter(context, tags);
            rvTags.setLayoutManager(new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL));
            rvTags.setAdapter(tagAdapter);
        } else {
            tagAdapter.addTag(model);
        }

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            IndustryModel model = result.getData().getParcelableExtra("industry");
            addToTags(model);
        }
    });

    ActivityResultLauncher<Intent> onSelectedNewLocation = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String selectedLocation = result.getData().getStringExtra("selectedLocation");
                latitude = result.getData().getDoubleExtra("selectedLatitude", 31.5204);
                longitude = result.getData().getDoubleExtra("selectedLongitude", 74.3587);
                edit_location.setText(selectedLocation);
            }
        }
    });


}