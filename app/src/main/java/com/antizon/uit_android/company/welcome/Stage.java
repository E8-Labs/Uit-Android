package com.antizon.uit_android.company.welcome;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.CompanyStageAdapter;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Stage extends BaseActivity {

    private static final String TAG = Stage.class.getSimpleName();
    ImageView backIcon,redNoah2;
    TextView skip;
    ProgressDialog progressDialog;

    RecyclerView companyStageRecyclerView;
    CompanyStageAdapter driverAdapter;
    private List<ModelCompanySize> list;
    private ModelCompanySize dataModel, selectedDataModel;

    ArrayList<ModelCompanySize> industriesList;
    String selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "",stageValue="";
    int selectedStageId=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        setIds();
        getIntentData();
        initialize();

        setListener();

        setUpCompanyStageRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        getCompanyStage();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        skip = findViewById(R.id.skip);
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        companyStageRecyclerView = findViewById(R.id.company_stage_recyclerview);

    }
    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);

                Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                industriesList = bundle.getParcelableArrayList("industries");
                Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());
            }
        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Stage.this);
        list = new ArrayList<>();
        industriesList = new ArrayList<>();
        loadProfile(Stage.this, encodedImageData, redNoah2);

    }



    void setListener() {
        Log.d(TAG, "setListener: ");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedDataModel != null) {
                    openNextScreen();
                } else {
                    Toast.makeText(Stage.this, "Please select company size.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void setUpCompanyStageRecyclerView() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Stage.this);
        companyStageRecyclerView.setLayoutManager(linearLayoutManager);

        companyStageRecyclerView.setAdapter(driverAdapter);
    }


    void getCompanyStage() {
        Log.d(TAG, "getCompanySize: ");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Stage.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_COMPANY_STAGE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {

                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            Boolean status = jsonObject.getBoolean("status");


                            if (status) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                if (dataArray.length() > 0) {
                                    list.clear();

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                        int id;
                                        String name = "";
                                        id = dataObject.getInt("id");
                                        name = dataObject.getString("name");

                                        dataModel = new ModelCompanySize();
                                        dataModel.setId(id);
                                        dataModel.setName(name);

                                        list.add(dataModel);
                                    }
                                }
                            }
                            driverAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Log.d(TAG, "onErrorResponse: error: " + error);
                    }
                });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120), //After the set time elapses the request will timeout
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(Stage.this, Size.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" +selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}