package com.antizon.uit_android.applicant.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantJobFilterActivity;
import com.antizon.uit_android.adapters.applicant.ApplicantFeaturedJobsAdapter;
import com.antizon.uit_android.adapters.jobs.ApplicantHomeAdapter;
import com.antizon.uit_android.applicant.activities.ActivityApplicantJobDetail;
import com.antizon.uit_android.applicant.activities.ApplicantSavedJobsActivity;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantJobStatus;
import com.antizon.uit_android.applicant.welcome.ApplicantJobSearchActivity;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantSelectRace;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.activities.NotificationsActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantJobFragment extends Fragment implements ApplicantHomeAdapter.ApplicantHomeAdapterCallBack, SwipeRefreshLayout.OnRefreshListener {
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout layout_permissionDenied, btnSettings;
    RecyclerView recyclerview_applicantHome;

    List<ApplicantHomeJobDataModel> latestJobsList, featuredJobsList, recommendedJobsList;
    ApplicantHomeAdapter applicantHomeAdapter;

    FusedLocationProviderClient client;
    double latitude = 31.5204, longitude = 74.3587;
    ActivityResultLauncher<String[]> requestPermissionLauncher;
    ActivityResultLauncher<Intent> locationSettingsIntentLauncher;
    SwipeRefreshLayout swipeRefresh;

    View rootView;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_applicant_job, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);
        client = LocationServices.getFusedLocationProviderClient(context);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean permissionGranted = false;
            for (int i = 0; i < result.size(); i++) {
                if (Boolean.TRUE.equals(result.get(permissions[i]))) {
                    permissionGranted = true;
                } else {
                    permissionGranted = false;
                    break;
                }
            }

            if (permissionGranted) {
                getCurrentLocation();
            } else {
                Toast.makeText(context, "Permissions are denied!", Toast.LENGTH_SHORT).show();
            }
        });

        locationSettingsIntentLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> getCurrentLocation());

        initViews(rootView);

        if (!sessionManagement.getKeyApplicationStatus().equals("3")){
            completeProfileBottomSheet();
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissionLauncher.launch(permissions);
        }

        swipeRefresh.setOnRefreshListener(this);
        return rootView;
    }

    public void initViews(View rootView) {
        layout_permissionDenied = rootView.findViewById(R.id.layout_permissionDenied);
        recyclerview_applicantHome = rootView.findViewById(R.id.recyclerview_applicantHome);
        btnSettings = rootView.findViewById(R.id.btnSettings);
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.app_color);
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                recyclerview_applicantHome.setVisibility(View.VISIBLE);
                layout_permissionDenied.setVisibility(View.GONE);
                client.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        getApplicantHomeData("Bearer " + sessionManagement.getToken(), latitude+"", longitude+"");

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

                                    progressDialog.setMessage("Loading...");
                                    progressDialog.show();
                                    getApplicantHomeData("Bearer " + sessionManagement.getToken(), latitude+"", longitude+"");

                                }
                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        if (latitude == 0 && longitude == 0) {
                            getCurrentLocation();
                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                });
            }
            else {
                recyclerview_applicantHome.setVisibility(View.GONE);
                layout_permissionDenied.setVisibility(View.VISIBLE);
                btnSettings.setOnClickListener(v -> {
                    Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                });
            }

        } else {
            recyclerview_applicantHome.setVisibility(View.GONE);
            layout_permissionDenied.setVisibility(View.VISIBLE);
            btnSettings.setOnClickListener(v -> locationSettingsIntentLauncher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));
        }
    }

    private void getApplicantHomeData(String token, String latitude, String longitude){
        Call<ApplicantHomeResponseModel> call = service.getApplicantHome(token, latitude, longitude);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantHomeResponseModel> call, @NonNull Response<ApplicantHomeResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        ApplicantHomeResponseDataModel  applicantHomeResponseDataModel = response.body().getApplicantHomeResponseDataModel();

                        if (applicantHomeResponseDataModel !=null){
                            latestJobsList = new ArrayList<>();
                            featuredJobsList = new ArrayList<>();
                            recommendedJobsList = new ArrayList<>();

                            latestJobsList = applicantHomeResponseDataModel.getLatestJobsList();
                            featuredJobsList = applicantHomeResponseDataModel.getFeaturedJobList();
                            recommendedJobsList = applicantHomeResponseDataModel.getRecommendedJobsList();

                            showJobsRecyclerView(recyclerview_applicantHome, latestJobsList, featuredJobsList, recommendedJobsList);
                        }


                    } else {
                        CustomCookieToast.showFailureToast(requireActivity(), "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantHomeResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(requireActivity(), "Failure!",  t.getMessage());
            }
        });
    }

    private void showJobsRecyclerView(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> latestJobsList, List<ApplicantHomeJobDataModel> featuredJobsList, List<ApplicantHomeJobDataModel> recommendedJobsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        applicantHomeAdapter = new ApplicantHomeAdapter(context, latestJobsList, featuredJobsList, recommendedJobsList, sessionManagement.getUserName(),this);
        recyclerView.setAdapter(applicantHomeAdapter);
    }

    private void completeProfileBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.complete_profile_bottomsheet, rootView.findViewById(R.id.completeYourProfileLayout));

        String profileCompletion = "Your Profile is " + sessionManagement.getKeyApplicationStatus() + "/3 complete";

        TextView btnNotNow = sheetView.findViewById(R.id.notNow);
        TextView btnCompleteProfile = sheetView.findViewById(R.id.completeProfile);
        TextView text_profileCompletion = sheetView.findViewById(R.id.text_profileCompletion);

        text_profileCompletion.setText(profileCompletion);

        btnNotNow.setOnClickListener(v -> bottomSheetDialog.dismiss());
        btnCompleteProfile.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            if (sessionManagement.getKeyApplicationStatus().equals("1")){
                Intent intent = new Intent(context, ActivityApplicantSelectRace.class);
                intent.putExtra("from", "add");
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }else if (sessionManagement.getKeyApplicationStatus().equals("2")){
                Intent intent = new Intent(context, ActivityApplicantJobStatus.class);
                intent.putExtra("from", "add");
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }

        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
    }

    ActivityResultLauncher<Intent> onApplyJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            getApplicantHomeData("Bearer " + sessionManagement.getToken(), latitude+"", longitude+"");
        }
    });

    @Override
    public void onLatestJobItemClicked(int position) {
        Intent intent = new Intent(context, ActivityApplicantJobDetail.class);
        intent.putExtra("jobDataModel", latestJobsList.get(position));
        onApplyJobLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void onMyJobsClicked() {
        Intent intent = new Intent(context, ApplicantSavedJobsActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onSearchJobClicked() {
        Intent intent = new Intent(context, ApplicantJobSearchActivity.class);
        intent.putExtra("filterApplied", false);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onApplyFilterJobClicked() {
        Intent intent = new Intent(context, ApplicantJobFilterActivity.class);
        onFilterAppliedLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void onNotificationClicked() {
        Intent intent = new Intent(context, NotificationsActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onFeaturedJobClicked(ApplicantFeaturedJobsAdapter featuredJobsAdapter, int position) {
        Intent intent = new Intent(context, ActivityApplicantJobDetail.class);
        intent.putExtra("jobDataModel", featuredJobsList.get(position));
        onApplyJobLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    ActivityResultLauncher<Intent> onFilterAppliedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                ArrayList<String> filtersList = resultIntent.getStringArrayListExtra("filtersList");
                ArrayList<String> industries = resultIntent.getStringArrayListExtra("industries");
                ArrayList<String> employment = resultIntent.getStringArrayListExtra("employment");
                ArrayList<String> work_location = resultIntent.getStringArrayListExtra("work_location");
                int minSalary = resultIntent.getIntExtra("min_salary", 0);
                int maxSalary = resultIntent.getIntExtra("max_salary", 0);
                String jobTitle = resultIntent.getStringExtra("jobTitle");
                String location = resultIntent.getStringExtra("location");
                double latitude = resultIntent.getDoubleExtra("latitude", 31.5204);
                double longitude = resultIntent.getDoubleExtra("longitude", 74.3587);

                Intent intent = new Intent(context, ApplicantJobSearchActivity.class);
                intent.putExtra("filterApplied", true);
                intent.putStringArrayListExtra("industries", industries);//comma separated ids of industries
                intent.putExtra("jobTitle", jobTitle); // string
                intent.putExtra("location", location); // string
                intent.putExtra("latitude", latitude);// double
                intent.putExtra("longitude", longitude);// double
                intent.putStringArrayListExtra("employment", employment);//comma separated ids of selected employment types
                intent.putStringArrayListExtra("work_location", work_location);//comma separated ids of work location types
                intent.putExtra("min_salary", minSalary);//int
                intent.putExtra("max_salary", maxSalary);//int
                intent.putStringArrayListExtra("filtersList", filtersList);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }

        }
    });

    @Override
    public void onRefresh() {
        Vibrator v = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> swipeRefresh.setRefreshing(false), 400);
        getApplicantHomeData("Bearer " + sessionManagement.getToken(), latitude+"", longitude+"");
    }
}
