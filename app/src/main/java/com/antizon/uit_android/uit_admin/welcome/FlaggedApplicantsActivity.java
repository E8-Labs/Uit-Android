package com.antizon.uit_android.uit_admin.welcome;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.FlaggedUserAdapter;
import com.antizon.uit_android.generic.model.ModelFlaggedUser;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FlaggedApplicantsActivity extends BaseActivity {
    private static final String TAG = FlaggedApplicantsActivity.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView flaggedUserRecyclerView;
    ImageView backIcon;
    EditText searchOffer;

    FlaggedUserAdapter flaggedUserAdapter;
    List<ModelFlaggedUser> list;
    ModelFlaggedUser dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged);
        Utilities.setCustomStatusAndNavColor(FlaggedApplicantsActivity.this, R.color.white_dash, R.color.white_dash);
        setIds();
        initialize();
        setListener();
        setUpFlaggedUserRecyclerview();
        sendServerRequestGET(AppConstants.GET_FLAGGED_USER, sessionManagement.getToken());
    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        flaggedUserRecyclerView = findViewById(R.id.flaggedUserRecyclerView);
        backIcon = findViewById(R.id.backIcon);
        searchOffer = findViewById(R.id.searchOffer);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(FlaggedApplicantsActivity.this);
        list = new ArrayList<>();

    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        backIcon.setOnClickListener(view -> onBackPressed());

        searchOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = searchOffer.getText().toString().toLowerCase(Locale.getDefault());
                flaggedUserAdapter.search(searchedString);
                Log.d(TAG, "onTextChanged: " + searchedString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void setUpFlaggedUserRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FlaggedApplicantsActivity.this);
        flaggedUserRecyclerView.setLayoutManager(linearLayoutManager);

        flaggedUserAdapter = new FlaggedUserAdapter(list, FlaggedApplicantsActivity.this);
        flaggedUserRecyclerView.setAdapter(flaggedUserAdapter);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    list.clear();

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject dataObject = dataArray.getJSONObject(i);
                        int id = dataObject.getInt("id");
                        Log.d(TAG, "onResponse: id " + id);
                        String comment = dataObject.getString("comment");
                        Log.d(TAG, "onResponse: comment " + comment);
                        String created_at = dataObject.getString("created_at");
                        Log.d(TAG, "onResponse: created_at " + created_at);

                        dataModel = new ModelFlaggedUser();
                        dataModel.setId(id);
                        dataModel.setComment(comment);
                        dataModel.setCreated_at(created_at);

                        list.add(dataModel);

                        JSONObject flaggedObject = dataObject.getJSONObject("flagged");
                        Log.d(TAG, "onResponse: flagged: size: " + flaggedObject.length());

                        id = flaggedObject.getInt("id");
                        String name = flaggedObject.getString("name");
                        int role = flaggedObject.getInt("role");
                        int user_id = flaggedObject.getInt("user_id");
                        String profile_image = flaggedObject.getString("profile_image");
                        String job_title = flaggedObject.getString("job_title");
                        String greenhouse_access_token = flaggedObject.getString("greenhouse_access_token");

                        dataModel.setId(id);
                        dataModel.setUser_id(user_id);
                        dataModel.setRole(role);
                        dataModel.setName(name);
                        dataModel.setGreenhouse_access_token(greenhouse_access_token);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setJob_title(job_title);

//                        JSONObject flagged_byObject = dataObject.getJSONObject("flagged_by");
//                        Log.d(TAG, "onResponse: flagged_by: size: " + flagged_byObject.length());
//
//                        id = flagged_byObject.getInt("id");
//                        name = flagged_byObject.getString("name");
//                        role = flagged_byObject.getInt("role");
//                        user_id = flagged_byObject.getInt("user_id");
//                       profile_image = flagged_byObject.getString("profile_image");
//                        job_title = flagged_byObject.getString("job_title");
//                       greenhouse_access_token = flagged_byObject.getString("greenhouse_access_token");
//
//                        ModelUser userModel = new ModelUser();
//                        userModel.setId(id);
//                        userModel.setUser_id(user_id);
//                        userModel.setRole(role);
//                        userModel.setName(name);
//                        userModel.setGreenhouse_access_token(greenhouse_access_token);
//                        userModel.setProfile_image(profile_image);
//                        userModel.setJob_title(job_title);
//                        dataModel.setFlaggedDataModel(userModel);


                    }
                }
            }
            flaggedUserAdapter.notifyDataSetChanged();
            flaggedUserAdapter.setFilterArrayListValue(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //    RecyclerView listedCompaniesRecyclerview,filledCompaniesRecyclerview,closedCompaniesRecyclerview;
    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}