package com.antizon.uit_android.applicant.welcome;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.antizon.uit_android.models.races.RacesModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivityApplicantSelectRace extends BaseActivity implements ApplicantRaceAdapter.SelectionListener {

    private static final String TAG = ActivityApplicantSelectRace.class.getSimpleName();
    SessionManagement sessionManagement;
    ImageView backIcon, menYellow;
    TextView next;
    ProgressDialog progressDialog;
    String  encodedImageData = "";

    RecyclerView applicantRaceRecyclerView;
    ApplicantRaceAdapter driverAdapter;
    
    ArrayList<ModelApplicantRace> list;
    ArrayList<ModelApplicantRace> selectedRaces;
    ModelApplicantRace dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_race);
        Utilities.setWhiteBars(ActivityApplicantSelectRace.this);
        initViews();

        getRaces();
    }
    

    void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        applicantRaceRecyclerView = findViewById(R.id.race_recyclerview);

        encodedImageData = getIntent().getStringExtra("profilePic");

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
                openNextScreen();
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

    void setUpApplicantRaceRecyclerView(RecyclerView recyclerView, List<ModelApplicantRace> raceList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityApplicantSelectRace.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        driverAdapter = new ApplicantRaceAdapter(raceList, ActivityApplicantSelectRace.this, this);
        recyclerView.setAdapter(driverAdapter);
    }


    void openNextScreen () {
        Intent intent = new Intent(ActivityApplicantSelectRace.this, ActivityApplicantSelectGenderIdentity.class);
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
}