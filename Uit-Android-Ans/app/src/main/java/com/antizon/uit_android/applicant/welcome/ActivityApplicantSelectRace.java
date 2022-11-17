package com.antizon.uit_android.applicant.welcome;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.ApplicantRaceAdapter;
import com.antizon.uit_android.generic.model.ModelApplicantRace;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.models.races.RacesModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityApplicantSelectRace extends BaseActivity implements ApplicantRaceAdapter.SelectionListener {
    private static final String TAG = ActivityApplicantSelectRace.class.getSimpleName();

    Context context;
    GetDataService service;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    ImageView backIcon, menYellow;
    TextView next;

    
    RecyclerView applicantRaceRecyclerView;
    ApplicantRaceAdapter driverAdapter;
    
    ArrayList<ModelApplicantRace> list;
    ArrayList<ModelApplicantRace> selectedRaces;
    ModelApplicantRace dataModel;

    String encodedImageData = "", from;
    List<NameIdModel> applicantRacesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_race);
        Utilities.setWhiteBars(ActivityApplicantSelectRace.this);
        context = ActivityApplicantSelectRace.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        initViews();

        getRaces();
    }
    

    void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        applicantRaceRecyclerView = findViewById(R.id.race_recyclerview);

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            applicantRacesList = getIntent().getParcelableArrayListExtra("applicantRacesList");
        }else {
            encodedImageData = getIntent().getStringExtra("profilePic");
        }
     

        sessionManagement=new SessionManagement(ActivityApplicantSelectRace.this);
        progressDialog = new ProgressDialog(ActivityApplicantSelectRace.this);
        list = new ArrayList<>();
        loadProfile(ActivityApplicantSelectRace.this, sessionManagement.getProfileImage(), menYellow);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(view -> {
            selectedRaces = new ArrayList<>();
            for (int i = 0; i< list.size(); i++){
                if (list.get(i).isSelected()){
                    selectedRaces.add(list.get(i));
                }
            }

            if (selectedRaces.size() > 0) {
                if (from.equals("edit")){
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    requestForUpdateRacesList(selectedRaces);
                }else {
                    openNextScreen();
                }

            } else {
                CustomCookieToast.showRequiredToast(ActivityApplicantSelectRace.this, "Please select minimum one Race.");
            }
        });
    }

    private void getRaces() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityApplicantSelectRace.this);

        @SuppressLint("NotifyDataSetChanged")
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_RACE,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    try {

                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        if (status) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                list.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    int id;
                                    String name;
                                    id = dataObject.getInt("id");
                                    name = dataObject.getString("name");

                                    dataModel = new ModelApplicantRace();
                                    dataModel.setId(id);
                                    dataModel.setName(name);
                                    dataModel.setSelected(false);

                                    list.add(dataModel);
                                }

                                if (from.equals("edit")){
                                    for (int i=0; i<list.size(); i++){
                                        for (int j=0; j<applicantRacesList.size(); j++){
                                            if (list.get(i).getId() == applicantRacesList.get(j).getId()){
                                                list.get(i).setSelected(true);
                                            }
                                        }
                                    }
                                }
                                setUpApplicantRaceRecyclerView(applicantRaceRecyclerView, list);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {

            progressDialog.dismiss();
                    Log.d(TAG, "onErrorResponse: error: " + error);
                });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120), //After the set time elapses the request will timeout
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void setUpApplicantRaceRecyclerView(RecyclerView recyclerView, List<ModelApplicantRace> raceList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityApplicantSelectRace.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        driverAdapter = new ApplicantRaceAdapter(raceList, ActivityApplicantSelectRace.this, this);
        recyclerView.setAdapter(driverAdapter);
    }


    private void openNextScreen () {
        Intent intent = new Intent(ActivityApplicantSelectRace.this, ActivityApplicantSelectGenderIdentity.class);
        intent.putExtra("from", from);
        intent.putExtra("selectedRaces", new RacesModel(selectedRaces));
        intent.putExtra("image", encodedImageData);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void selectedCompanySize(boolean isChecked, int position) {
        list.get(position).setSelected(!list.get(position).isSelected());
        driverAdapter.notifyDataSetChanged();
    }


    private void requestForUpdateRacesList(List<ModelApplicantRace> raceList) {

        JsonArray racesArray = new JsonArray();
        applicantRacesList = new ArrayList<>();

        for (int i = 0; i < raceList.size(); i++) {
            JsonObject raceObject = new JsonObject();
            // add the exercise category to Json
            raceObject.addProperty("race", raceList.get(i).getId());
            racesArray.add(raceObject);
            applicantRacesList.add(new NameIdModel(raceList.get(i).getId(), raceList.get(i).getName()));
        }

        JsonObject mainObject = new JsonObject();
        mainObject.add("races", racesArray);

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Call<MainResponseModel> call = service.updateDemographicInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("applicantRacesList", (ArrayList<? extends Parcelable>) applicantRacesList);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(ActivityApplicantSelectRace.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantSelectRace.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showFailureToast(ActivityApplicantSelectRace.this, "Failure!", t.getMessage());
            }
        });
    }
}