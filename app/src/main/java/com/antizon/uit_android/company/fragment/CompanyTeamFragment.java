package com.antizon.uit_android.company.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.AcceptedMemberAdapter;
import com.antizon.uit_android.generic.adapter.PendingMemberAdapter;
import com.antizon.uit_android.generic.dialog.AddTeam;
import com.antizon.uit_android.generic.model.CompanyMemberModel;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CompanyTeamFragment extends BaseFragment {
    private static final String TAG = CompanyTeamFragment.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    TextView pending, approved, paused, title;
    ImageView welcome, dashboardMen;
    ConstraintLayout pendingLayout, approvedLayout, pausedLayout;
    RecyclerView pendingCompaniesRecyclerview, acceptedCompaniesRecyclerview, invitedCompaniesRecyclerview;

    PendingMemberAdapter pendingAdapter;
    private List<CompanyMemberModel> pendingList;
    private CompanyMemberModel pendingDataModel;

    AcceptedMemberAdapter acceptedAdapter;
    private List<CompanyMemberModel> acceptedList;
    private CompanyMemberModel acceptedDataModel;

    String progressMessage = "", accessToken = "";
    Boolean isAcceptedSelected = true;
    String selectedJobStatus = "1";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        setIds(view);
        initialize();
        setListener();
        acceptedRecyclerview();
        sendServerRequestGET(AppConstants.COMPANY_TEAM_MEMBERS + "?account_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
        return view;
    }


    void setIds(View view) {
        Log.d(TAG, "setIds: ");

        pending = view.findViewById(R.id.pending);
        approved = view.findViewById(R.id.approved);
        paused = view.findViewById(R.id.paused);
        pendingLayout = view.findViewById(R.id.pendingLayout);
        approvedLayout = view.findViewById(R.id.approvedLayout);
        pausedLayout = view.findViewById(R.id.pausedLayout);
        pendingCompaniesRecyclerview = view.findViewById(R.id.pending_companies_recyclerview);
        acceptedCompaniesRecyclerview = view.findViewById(R.id.accepted_companies_recyclerview);
        invitedCompaniesRecyclerview = view.findViewById(R.id.invited_companies_recyclerview);
        title = view.findViewById(R.id.title);
        welcome = view.findViewById(R.id.welcome);
        dashboardMen = view.findViewById(R.id.dashboardMen);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        title.setText("Team Members ");
        pending.setText("Accepted");
        approved.setText("Pending");
        paused.setText("Invited");
//        pendingList = new ArrayList<>();
        acceptedList = new ArrayList<>();
        sessionManagement = new SessionManagement(getContext());
        Log.d(TAG, "initialize: token: " + sessionManagement.getToken());
        progressDialog = new ProgressDialog(getContext());
        accessToken = sessionManagement.getUserDetails().get("access_token");

        Utilities.loadCircleImage(getContext(), sessionManagement.getProfileImage(), dashboardMen);
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                selectedJobStatus = "1";
                setPendingLayout();
//                getCompanyAccepted("");
                sendServerRequestGET(AppConstants.COMPANY_TEAM_MEMBERS + "?account_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
            }
        });

        approvedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");
                selectedJobStatus = "1";
                setApprovedLayout();
                sendServerRequestGET(AppConstants.COMPANY_TEAM_MEMBERS + "?account_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
            }
        });

        pausedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");
                selectedJobStatus = "1";
                setPausedLayout();
                sendServerRequestGET(AppConstants.COMPANY_TEAM_MEMBERS + "?account_status=" + selectedJobStatus + "&off_set=0", sessionManagement.getToken());
            }
        });

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTeam rateOurApp = new AddTeam(getContext());
                rateOurApp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                rateOurApp.show();

            }
        });
    }

//
//    void acceptedRecyclerview() {
//        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        pendingCompaniesRecyclerview.setLayoutManager(linearLayoutManager);
//
//        pendingAdapter = new PendingMemberAdapter(pendingList, getContext(),
//                new PendingMemberAdapter.CompanyStatusInterface() {
//                    @Override
//                    public void accept(CompanyMemberModel dataModel) {
//                        Log.d(TAG, "accept: Accept Company Members");
//                        progressMessage = "Accepting...";
//
//                        HashMap<String, String> params = new HashMap<String, String>();
//                        params.put("team_id", "51");
//
//                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.APPROVE_COMPANY_MEMBER, params, sessionManagement.getUserDetails().get("access_token"));
////                        sendServerRequestPOST(AppConstants.APPROVE_COMPANY_MEMBER, params, accessToken);
//                    }
//
//                    @Override
//                    public void decline(CompanyMemberModel dataModel) {
//                        Log.d(TAG, "decline: Decline Company");
//                        progressMessage = "Declining...";
//
//                        HashMap<String, String> params = new HashMap<String, String>();
//                        params.put("team_id", "51" );
//
//                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.REJECT_COMPANY_MEMBER, params, sessionManagement.getUserDetails().get("access_token"));
//                        sendServerRequestPOST(AppConstants.REJECT_COMPANY_MEMBER, params, accessToken);
//                    }
//                });
//        pendingCompaniesRecyclerview.setAdapter(pendingAdapter);
//    }

    void acceptedRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        acceptedCompaniesRecyclerview.setLayoutManager(linearLayoutManager);

        acceptedAdapter = new AcceptedMemberAdapter(acceptedList, getContext());
        acceptedCompaniesRecyclerview.setAdapter(acceptedAdapter);
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

        Log.d(TAG, "onResponse: " + response);
        try {
            progressDialog.dismiss();
            acceptedList.clear();

            JSONObject jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");

            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "onResponse: dataArray: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    Log.d(TAG, "onResponse: " + dataObject);
                    int id = dataObject.getInt("id");
                    int activeMembers = dataObject.getInt("active_members");
                    int user_id = dataObject.getInt("user_id");
                    String name = dataObject.getString("name");
                    String email = dataObject.getString("email");
                    String job_title = dataObject.getString("job_title");
                    String profile_image = dataObject.getString("profile_image");
                    String city = dataObject.getString("city");
                    String state = dataObject.getString("state");
                    String greenhouseAccessToken = dataObject.getString("greenhouse_access_token");

                    acceptedDataModel = new CompanyMemberModel();
                    acceptedDataModel.setId(id);
                    acceptedDataModel.setName(name);
                    acceptedDataModel.setActive_members(activeMembers);
                    acceptedDataModel.setUser_id(user_id);
                    acceptedDataModel.setJob_title(job_title);
                    acceptedDataModel.setCity(city);
                    acceptedDataModel.setState(state);
                    acceptedDataModel.setGreenhouse_access_token(greenhouseAccessToken);
                    acceptedDataModel.setProfile_image(profile_image);
                    acceptedDataModel.setEmail(email);

                    acceptedList.add(acceptedDataModel);
                }
                Log.d(TAG, "onResponse: pendingList: " + acceptedList.size());
                acceptedAdapter.notifyDataSetChanged();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }

    void setPendingLayout() {
        acceptedCompaniesRecyclerview.setVisibility(View.VISIBLE);
        pendingCompaniesRecyclerview.setVisibility(View.GONE);
        invitedCompaniesRecyclerview.setVisibility(View.GONE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));

        pending.setTextColor(getResources().getColor(R.color.white));
        approved.setTextColor(getResources().getColor(R.color.black));
        paused.setTextColor(getResources().getColor(R.color.black));
    }

    void setApprovedLayout() {
        acceptedCompaniesRecyclerview.setVisibility(View.GONE);
        pendingCompaniesRecyclerview.setVisibility(View.VISIBLE);
        invitedCompaniesRecyclerview.setVisibility(View.GONE);
        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.white));
        paused.setTextColor(getResources().getColor(R.color.black));
    }


    void setPausedLayout() {

        acceptedCompaniesRecyclerview.setVisibility(View.GONE);
        pendingCompaniesRecyclerview.setVisibility(View.GONE);
        invitedCompaniesRecyclerview.setVisibility(View.VISIBLE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));

        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.black));
        paused.setTextColor(getResources().getColor(R.color.white));
    }



}