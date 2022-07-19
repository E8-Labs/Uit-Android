package com.antizon.uit_android.company.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
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


public class CompanyJobsFragment extends BaseFragment implements AdminAllJobsAdapter.AdminAllJobsAdapterCallBack {
    Context context;
    TextView listed, filled, closed, title, flagged;
    ImageView profilePicture;
    ConstraintLayout listedLayout, filledLayout, closedLayout;
    RecyclerView jobsRecyclerview;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    AdminAllJobsAdapter adminAllJobsAdapter;
    List<ModelAllJobs> list;
    ModelAllJobs dataModel;

    EditText searchOffer;
    String selectedJobStatus = "1";
    String role;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_fragment_job, container, false);
        initViews(view);

        initialize();

        setListener();
        setUpListedJobsRecyclerView();
        sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        return view;
    }
    void initViews(View view) {

        flagged = view.findViewById(R.id.flagged);
        listed = view.findViewById(R.id.pending);
        filled = view.findViewById(R.id.approved);
        closed = view.findViewById(R.id.paused);
        title = view.findViewById(R.id.title);
        jobsRecyclerview = view.findViewById(R.id.listed_companies_recyclerview);
        listedLayout = view.findViewById(R.id.pendingLayout);
        filledLayout = view.findViewById(R.id.approvedLayout);
        closedLayout = view.findViewById(R.id.pausedLayout);
        searchOffer = view.findViewById(R.id.searchOffer);
        profilePicture = view.findViewById(R.id.profilePicture);
    }

    private void initialize() {
        title.setText(getString(R.string.allJobs));
        listed.setText(getString(R.string.listed));
        filled.setText(getString(R.string.filled));
        closed.setText(getString(R.string.closed));

        sessionManagement = new SessionManagement(getContext());
        role = sessionManagement.getRole();

        progressDialog = new ProgressDialog(getContext());
        loadProfile(getContext(), sessionManagement.getProfileImage(), profilePicture);

        list = new ArrayList<>();
    }



    private void setListener() {
        flagged.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminFlaggedJobsActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });
        listedLayout.setOnClickListener(view -> {
            setListedLayout();
            sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        });

        filledLayout.setOnClickListener(view -> {
            setFilledLayout();
            sendServerRequestGET(AppConstants.ADMIN_JOBS_LIST + "?job_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        });

        closedLayout.setOnClickListener(view -> {
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

                if (searchedString.isEmpty()) {
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

    private void setUpListedJobsRecyclerView() {
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        try {

            list.clear();
            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            if (status) {

                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataObject = dataArray.getJSONObject(i);
                    int id, locationStatus, employmentType, jobStatus, minSalary,
                            maxSalary, totalApplications, industryId;
                    String city, jobTitle, state,
                            createdAt, reasonToClose, match, industryName;

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

                    JSONObject industryObject = dataObject.getJSONObject("industry");
                    industryId = industryObject.getInt("id");
                    industryName = industryObject.getString("name");

                    ModelApplicantDepartment industryDataModel = new ModelApplicantDepartment();
                    industryDataModel.setId(industryId);
                    industryDataModel.setName(industryName);
                    dataModel.setIndustryDataModel(industryDataModel);


                    int userId, userRole, userUserId;
                    String userName, userProfileImage, userJobTitle, userGreenhouseAccessToken;

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
                    String companyName, companyProfileImage, companyJobTitle, companyGreenhouseAccessToken;

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
                        String applicantName, applicantProfileImage, applicantJobTitle, applicantGreenhouseAccessToken;

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
        } catch (JSONException ignored) {
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
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