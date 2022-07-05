package com.antizon.uit_android.uit_members.welcome;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.UitMemberDetailAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActivityMembers extends BaseActivity implements UitMemberDetailAdapter.UitMemberDetailAdapterCallBack {
    private static final String TAG = ActivityMembers.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView adminMemberRecyclerView;
    ImageView userProfile;
    UitMemberDetailAdapter driverAdapter;
    private List<ModelAdminApplicants> list;
    ModelAdminApplicants dataModel;
    TextView continueAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        Utilities.setCustomStatusAndNavColor(ActivityMembers.this, R.color.white_dash, R.color.white_dash);
        setIds();
        initialize();
        setListener();
        setUpAdminMembersRecyclerview();
        sendServerRequestGET(AppConstants.ADMIN_MEMBERS_LIST, sessionManagement.getToken());
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        adminMemberRecyclerView = findViewById(R.id.admin_members_recyclerView);
        continueAs = findViewById(R.id.continueAs);
        userProfile = findViewById(R.id.userProfile);
    }
    void initialize() {
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(ActivityMembers.this);
        list = new ArrayList<>();
        loadProfile(ActivityMembers.this, sessionManagement.getProfileImage(), userProfile);
    }
    void setListener() {
        Log.d(TAG, "setListener: ");

    }
    void setUpAdminMembersRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adminMemberRecyclerView.setLayoutManager(linearLayoutManager);
        driverAdapter = new UitMemberDetailAdapter(ActivityMembers.this, list, this);
        adminMemberRecyclerView.setAdapter(driverAdapter);
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
            driverAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ActivityMembers.this, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+list.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(list.get(position).getUser_id()));
        intent.putExtra("second_user_picture", list.get(position).getProfile_image());
        intent.putExtra("second_user_name", list.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}