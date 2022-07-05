package com.antizon.uit_android.uit_members.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.fragment.BaseFragment;
import com.antizon.uit_android.generic.adapter.AdminMembersAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MemberTeam extends BaseFragment {
    private static final String TAG = MemberTeam.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView adminMemberRecyclerView;
    AdminMembersAdapter adminMembersAdapter;
    List<ModelAdminApplicants> list;
    ModelAdminApplicants dataModel;
    TextView continueAs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_team, container, false);

        setIds(view);
        initialize();
        setListener();
        setUpAdminMembersRecyclerview();
        sendServerRequestGET(AppConstants.ADMIN_MEMBERS_LIST, sessionManagement.getToken());
        return view;

    }

    void setIds(View view) {
        Log.d(TAG, "setIds: ");
        adminMemberRecyclerView = view.findViewById(R.id.admin_members_recyclerView);
        continueAs = view.findViewById(R.id.continueAs);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        continueAs.setText("UIT Team Members");
        progressDialog = new ProgressDialog(getContext());
        sessionManagement = new SessionManagement(getContext());
        list = new ArrayList<>();

    }

    void setListener() {
        Log.d(TAG, "setListener: ");
    }
    void setUpAdminMembersRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adminMemberRecyclerView.setLayoutManager(linearLayoutManager);
        adminMembersAdapter = new AdminMembersAdapter(list, getContext());
        adminMemberRecyclerView.setAdapter(adminMembersAdapter);
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
            JSONObject jsonObject = new JSONObject(response);

            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    list.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);

                        int id = dataObject.getInt("id");
                        int user_id = dataObject.getInt("user_id");
                        String name = dataObject.getString("name");
                        String email = dataObject.getString("email");
                        String job_title = dataObject.getString("job_title");
                        String profile_image = dataObject.getString("profile_image");
                        String city = dataObject.getString("city");
                        String state = dataObject.getString("state");
                        String professional_info = dataObject.getString("professional_info");
                        String industries = dataObject.getString("industries");

                        dataModel = new ModelAdminApplicants();
                        dataModel.setId(id);
                        dataModel.setUser_id(user_id);
                        dataModel.setName(name);
                        dataModel.setEmail(email);
                        dataModel.setJob_title(job_title);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setCity(city);
                        dataModel.setState(state);
                        dataModel.setProfessional_info(professional_info);
                        dataModel.setIndustries(industries);

                        list.add(dataModel);
                    }
                }
            }
            adminMembersAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }
}