package com.antizon.uit_android.uit_admin.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.company.welcome.AdminFlaggedJobsActivity;
import com.antizon.uit_android.generic.adapter.AdminAllJobsAdapter;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.antizon.uit_android.generic.model.ModelApplicantDepartment;
import com.antizon.uit_android.generic.model.ModelUser;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_members.welcome.ActivityMembers;
import com.antizon.uit_android.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListedJobsActivity extends BaseActivity {
    private static final String TAG = ListedJobsActivity.class.getSimpleName();

    TextView pending, approved, paused,flaggedJobs;
    ImageView profilePicture;
    ConstraintLayout pendingLayout, approvedLayout, pausedLayout;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView jobsRecyclerview;
    AdminAllJobsAdapter adminAllJobsAdapter;
    private List<ModelAllJobs> list;
    ModelAllJobs dataModel;

    String progressMessage = "", accessToken = "";
    EditText searchOffer;
    Boolean isPendingSelected = true;

    String selectedJobStatus = "1";
    String role ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_jobs);
        Utilities.setCustomStatusAndNavColor(ListedJobsActivity.this, R.color.white_dash, R.color.white_dash);

        setIds();
        initialize();
        initializeViewsAsPerRole();
        setListener();
        setUpListedJobsRecyclerView();

        sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
    }

    void setIds() {

        Log.d(TAG, "setIds: ");
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        paused = findViewById(R.id.paused);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
        jobsRecyclerview = findViewById(R.id.listed_companies_recyclerview);
        searchOffer = findViewById(R.id.searchOffer);
        flaggedJobs = findViewById(R.id.flaggedJobs);
        profilePicture = findViewById(R.id.profilePicture);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        pending.setText("Listed");
        approved.setText("Filled");
        paused.setText("Closed");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(ListedJobsActivity.this);
        role=sessionManagement.getRole();
        loadProfile(ListedJobsActivity.this, sessionManagement.getProfileImage(), profilePicture);
        Log.d(TAG, "initialize: token: " + sessionManagement.getToken());
        list = new ArrayList<>();
    }

    void initializeViewsAsPerRole() {

//        case UITAdmin = 1
//        case Company = 2
//        case UITMember = 3
//        case CompanyMember = 4
//        case Applicant = 5

//        Set Visible to all the widgets (who are changing there)

        if (role.equalsIgnoreCase("1")) {

//            deleteIcon.setVisibility(View.VISIBLE);
            flaggedJobs.setVisibility(View.VISIBLE);

        } else if (role.equalsIgnoreCase("2")) {

        } else if (role.equalsIgnoreCase("3")) {

            flaggedJobs.setVisibility(View.GONE);


        } else if (role.equalsIgnoreCase("4")) {

        } else if (role.equalsIgnoreCase("5")) {

        }
    }


    void setListener() {
        Log.d(TAG, "setListener: ");

        flaggedJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListedJobsActivity.this, AdminFlaggedJobsActivity.class);
                startActivity(intent);
            }
        });

        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                setPendingLayout();
                sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
            }
        });

        approvedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");
                setApprovedLayout();
                sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
            }
        });

        pausedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: paused: ");
                setPausedLayout();
                sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + 2 + "&off_set=0", sessionManagement.getToken());
//                sendServerRequestGET(AppConstants.GET_MY_JOB_LIST_ADMIN + 3, sessionManagement.getToken());
            }
        });

        searchOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchedString = searchOffer.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(TAG, "onTextChanged: " + searchedString);
                if (searchedString.isEmpty()) {

//                    page = 1;
//                    currentPageNumber = 1;
//                    getUitAdminPending("1");
                    sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
                } else {

                    sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0&search=" + searchedString, sessionManagement.getToken());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void setUpListedJobsRecyclerView() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        jobsRecyclerview.setLayoutManager(linearLayoutManager);

        adminAllJobsAdapter = new AdminAllJobsAdapter(list, ListedJobsActivity.this);
        jobsRecyclerview.setAdapter(adminAllJobsAdapter);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);
//        Log.d(TAG, "onResponse: " + response);
        try {

            list.clear();
            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {

                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

//                        "id": 20,
//                            "job_title": "Hello",
//
//                        "city": "San Jose",
//                            "state": "CA",
//                            "location_status": 1,
//                            "employment_type": 1,
//                            "job_status": 1,
//                            "min_salary": 50000,
//                            "max_salary": 200000,
//
//                        "created_at": "2022-04-21T19:15:41.000000Z",
//                            "reason_to_close": null,
//                            "applications": [],
//                        "total_applications": 0,
//                            "match": null
//                    }

                    JSONObject dataObject = dataArray.getJSONObject(i);
                    int id, locationStatus, employmentType, jobStatus, minSalary,
                            maxSalary, totalApplications, industryId;
                    String city = "", jobTitle = "", state = "",
                            createdAt = "", reasonToClose = "", match = "", industryName;

                    id = dataObject.getInt("id");
                    jobTitle = dataObject.getString("job_title");
                    city = dataObject.getString("city");
                    state = dataObject.getString("state");
                    locationStatus = dataObject.getInt("location_status");
                    employmentType = dataObject.getInt("employment_type");
                    jobStatus = dataObject.getInt("job_status");
                    minSalary = dataObject.getInt("min_salary");
                    maxSalary = dataObject.getInt("max_salary");
                    createdAt = dataObject.getString("created_at");
                    reasonToClose = dataObject.getString("reason_to_close");
                    totalApplications = dataObject.getInt("total_applications");

                    Log.d(TAG, "onResponseReceived: match: isNull: " + dataObject.isNull("match"));
                    if (dataObject.isNull("match")) {
                        match = "";
                    } else {
                        match = dataObject.getString("match");
                    }

                    dataModel = new ModelAllJobs();
                    dataModel.setId(id);
                    dataModel.setJob_title(jobTitle);
                    dataModel.setMatch(match);
                    dataModel.setCity(city);
                    dataModel.setReason_to_close(reasonToClose);
                    dataModel.setState(state);
                    dataModel.setLocation_status(locationStatus);
                    dataModel.setEmployment_type(employmentType);
                    dataModel.setJob_status(jobStatus);
                    dataModel.setMin_salary(minSalary);
                    dataModel.setMax_salary(maxSalary);
                    dataModel.setTotal_applications(totalApplications);
                    dataModel.setCreated_at(createdAt);

                    dataModel.setSelectedJobStatus(selectedJobStatus);


//                    "industry": {
//                        "id": 53,
//                                "name": "Health, Wellness & Fitness"
//                    },
                    JSONObject industryObject = dataObject.getJSONObject("industry");
                    industryId = industryObject.getInt("id");
                    industryName = industryObject.getString("name");

                    ModelApplicantDepartment industryDataModel = new ModelApplicantDepartment();
                    industryDataModel.setId(industryId);
                    industryDataModel.setName(industryName);
                    dataModel.setIndustryDataModel(industryDataModel);

                    //                    "user": {
//                        "id": 4,
//                                "name": "Antzion",
//                                "role": 2,
//                                "profile_image": "http://www.zorroapp.tech/uit/storage/app/Images/RnHFBlhGG2ST53qrUqXPtwaStnmuqhU96a179fvF.jpg",
//                                "job_title": "",
//                                "user_id": 6,
//                                "greenhouse_access_token": "token_greenhouse_access"
//                    },

                    int userId, userRole, userUserId;
                    String userName = "", userProfileImage = "", userJobTitle = "", userGreenhouseAccessToken = "";

                    JSONObject userObject = dataObject.getJSONObject("user");
                    userId = userObject.getInt("id");
                    userName = userObject.getString("name");
                    userRole = userObject.getInt("role");
                    userProfileImage = userObject.getString("profile_image");
                    userJobTitle = userObject.getString("job_title");
                    userUserId = userObject.getInt("user_id");
                    userGreenhouseAccessToken = userObject.getString("greenhouse_access_token");

                    ModelUser userDataModel = new ModelUser();
                    userDataModel.setUser_id(userId);
                    userDataModel.setName(userName);
                    userDataModel.setRole(userRole);
                    userDataModel.setProfile_image(userProfileImage);
                    userDataModel.setJob_title(userJobTitle);
                    userDataModel.setId(userUserId);
                    userDataModel.setGreenhouse_access_token(userGreenhouseAccessToken);
                    dataModel.setUserDataModel(userDataModel);

                    int companyId, companyRole, companyUserId;
                    String companyName = "", companyProfileImage = "", companyJobTitle = "", companyGreenhouseAccessToken = "";

                    JSONObject companyObject = dataObject.getJSONObject("company");
                    companyId = companyObject.getInt("id");
                    companyName = companyObject.getString("name");
                    companyRole = companyObject.getInt("role");
                    companyProfileImage = companyObject.getString("profile_image");
                    companyJobTitle = companyObject.getString("job_title");
                    companyUserId = companyObject.getInt("user_id");
                    companyGreenhouseAccessToken = companyObject.getString("greenhouse_access_token");

                    ModelUser companyDataModel = new ModelUser();
                    companyDataModel.setId(companyId);
                    companyDataModel.setName(companyName);
                    companyDataModel.setRole(companyRole);
                    companyDataModel.setProfile_image(companyProfileImage);
                    companyDataModel.setJob_title(companyJobTitle);
                    companyDataModel.setUser_id(companyUserId);
                    companyDataModel.setGreenhouse_access_token(companyGreenhouseAccessToken);
                    dataModel.setCompanyDataModel(companyDataModel);

                    JSONArray applicantArray = dataObject.getJSONArray("applications");
                    List<ModelUser> applicantsList = new ArrayList<>();
                    for (int x = 0; x < applicantArray.length(); x++) {

                        int applicantId, applicantRole, applicantUserId;
                        String applicantName = "", applicantProfileImage = "", applicantJobTitle = "", applicantGreenhouseAccessToken = "";

                        JSONObject applicantObject = applicantArray.getJSONObject(x);
                        applicantId = applicantObject.getInt("id");
                        applicantName = applicantObject.getString("name");
                        applicantRole = applicantObject.getInt("role");
                        applicantProfileImage = applicantObject.getString("profile_image");
                        applicantJobTitle = applicantObject.getString("job_title");
                        applicantUserId = applicantObject.getInt("user_id");
                        applicantGreenhouseAccessToken = applicantObject.getString("greenhouse_access_token");

                        ModelUser applicantDataModel = new ModelUser();
                        applicantDataModel.setId(applicantId);
                        applicantDataModel.setName(applicantName);
                        applicantDataModel.setRole(applicantRole);
                        applicantDataModel.setProfile_image(applicantProfileImage);
                        applicantDataModel.setJob_title(applicantJobTitle);
                        applicantDataModel.setUser_id(applicantUserId);
                        applicantDataModel.setGreenhouse_access_token(applicantGreenhouseAccessToken);

                        applicantsList.add(applicantDataModel);
                    }

                    dataModel.setApplicationsList(applicantsList);

                    list.add(dataModel);
                }
            }
            adminAllJobsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }

    void setPendingLayout() {
        selectedJobStatus = "1";

        pendingLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.white_curved_background);

        pending.setTextColor(getColor(R.color.white));
        approved.setTextColor(getColor(R.color.black));
        paused.setTextColor(getColor(R.color.black));
    }

    void setApprovedLayout() {
        selectedJobStatus = "2";
        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.white));
        paused.setTextColor(getColor(R.color.black));
    }


    void setPausedLayout() {
        selectedJobStatus = "3";

        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.app_color_curved_background);

        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.black));
        paused.setTextColor(getColor(R.color.white));

    }
}