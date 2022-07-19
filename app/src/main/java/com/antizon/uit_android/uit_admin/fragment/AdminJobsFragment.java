package com.antizon.uit_android.uit_admin.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.fragment.BaseFragment;
import com.antizon.uit_android.company.welcome.AdminFlaggedJobsActivity;
import com.antizon.uit_android.generic.activities.AdminJobDetailActivity;
import com.antizon.uit_android.generic.adapter.AdminAllJobsAdapter;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.antizon.uit_android.generic.model.ModelApplicantDepartment;
import com.antizon.uit_android.generic.model.ModelUser;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AdminJobsFragment extends BaseFragment implements AdminAllJobsAdapter.AdminAllJobsAdapterCallBack {
    Context context;

    private static final String TAG = AdminJobsFragment.class.getSimpleName();

    TextView listed, filled, closed, flagged;
    ConstraintLayout listedLayout, filledLayout, closedLayout;
    RecyclerView jobsRecyclerview;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    AdminAllJobsAdapter adminAllJobsAdapter;
    List<ModelAllJobs> list;
    ModelAllJobs dataModel;

    EditText searchOffer;
    Boolean isPendingSelected = true;
    String selectedJobStatus = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_jobs, container, false);
        context = requireActivity();

        setIds(view);
        initialize();
        setListener();
        sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        setUpListedJobsRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        case StatusOpened = 1;        // we are assuming listed means open
//        case StatusClosed = 2;
//        case StatusFilled = 3;
//        case StatusPaused = 4;
        Log.d(TAG, "onResume: ");


    }

    void setIds(View view) {
        Log.d(TAG, "setIds: ");

        flagged = view.findViewById(R.id.flagged);
        listed = view.findViewById(R.id.pending);
        filled = view.findViewById(R.id.approved);
        closed = view.findViewById(R.id.paused);
        jobsRecyclerview = view.findViewById(R.id.listed_companies_recyclerview);
        listedLayout = view.findViewById(R.id.pendingLayout);
        filledLayout = view.findViewById(R.id.approvedLayout);
        closedLayout = view.findViewById(R.id.pausedLayout);
        searchOffer = view.findViewById(R.id.searchOffer);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        listed.setText("Listed");
        filled.setText("Filled");
        closed.setText("Closed");

        sessionManagement = new SessionManagement(getContext());
        Log.d(TAG, "initialize: token: " + sessionManagement.getToken());
        progressDialog = new ProgressDialog(getContext());

        list = new ArrayList<>();
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        flagged.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminFlaggedJobsActivity.class);
            startActivity(intent);

        });
        listedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");

            setListedLayout();
            sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        });

        filledLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: filled: ");

            setFilledLayout();
            sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        });

        closedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: filled: ");

            setClosedLayout();
            sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        jobsRecyclerview.setLayoutManager(linearLayoutManager);

        adminAllJobsAdapter = new AdminAllJobsAdapter(requireContext(), list, this);
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

//                    "applications": [
//                    {
//                        "id": 50,
//                            "name": "Tal Rozenberg",
//                            "role": 5,
//                            "profile_image": "http://www.zorroapp.tech/uit/storage/app/Images/vIsqDCSwHxq3mbazLkQbcuXHlo3TA6T4Qfi3jrg5.jpg",
//                            "job_title": "",
//                            "user_id": 137,
//                            "greenhouse_access_token": null
//                    }
//            ],

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

    void setListedLayout() {
        selectedJobStatus = "1";
        listedLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        filledLayout.setBackgroundResource(R.drawable.white_curved_background);
        closedLayout.setBackgroundResource(R.drawable.white_curved_background);

        listed.setTextColor(context.getColor(R.color.white));
        filled.setTextColor(context.getColor(R.color.black));
        closed.setTextColor(context.getColor(R.color.black));
    }

    void setFilledLayout() {
        selectedJobStatus = "2";
        listedLayout.setBackgroundResource(R.drawable.white_curved_background);
        filledLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        closedLayout.setBackgroundResource(R.drawable.white_curved_background);
        listed.setTextColor(context.getColor(R.color.black));
        filled.setTextColor(context.getColor(R.color.white));
        closed.setTextColor(context.getColor(R.color.black));
    }

    void setClosedLayout() {
        selectedJobStatus = "3";
        listedLayout.setBackgroundResource(R.drawable.white_curved_background);
        filledLayout.setBackgroundResource(R.drawable.white_curved_background);
        closedLayout.setBackgroundResource(R.drawable.app_color_curved_background);

        listed.setTextColor(context.getColor(R.color.black));
        filled.setTextColor(context.getColor(R.color.black));
        closed.setTextColor(context.getColor(R.color.white));

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, AdminJobDetailActivity.class);
        intent.putExtra("dataModel", list.get(position));
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}