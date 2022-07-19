package com.antizon.uit_android.generic.activities;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.HiredApplicantAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import net.time4j.Moment;
import net.time4j.PrettyTime;
import net.time4j.format.expert.Iso8601Format;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminJobDetailActivity extends BaseActivity implements HiredApplicantAdapter.HiredApplicantsAdapterCallBack {

    private static final String TAG = AdminJobDetailActivity.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView hiredApplicantsRecyclerView;
    ImageView backIcon;
    RoundedImageView reminder;
    TextView senior, airBnb, sanFrancisco, product, design, dollar, fullTime, text_time;
    HiredApplicantAdapter hiredApplicantAdapter;
    List<ModelAdminApplicants> list;
    ModelAllJobs modelAllJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_job_detail);
        Utilities.setWhiteBars(AdminJobDetailActivity.this);
        setIds();
        initialize();
        getIntentData();
        setUpAdminApplicantsRecyclerview();
        sendServerRequestGET(AppConstants.ADMIN_APPLICANTS_LIST, sessionManagement.getToken());
    }


    private void setIds() {
        Log.d(TAG, "setIds: ");
        hiredApplicantsRecyclerView = findViewById(R.id.hired_applicants_recyclerview);
        reminder = findViewById(R.id.reminder);
        senior = findViewById(R.id.senior);
        airBnb = findViewById(R.id.airBnb);
        sanFrancisco = findViewById(R.id.sanFrancisco);
        product = findViewById(R.id.product);
        design = findViewById(R.id.design);
        dollar = findViewById(R.id.dollar);
        fullTime = findViewById(R.id.fullTime);
        backIcon = findViewById(R.id.backIcon);
        text_time = findViewById(R.id.text_time);

        backIcon.setOnClickListener(view -> onBackPressed());
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(AdminJobDetailActivity.this);
        list = new ArrayList<>();
    }

    void getIntentData() {
        if (getIntent() != null) {
            modelAllJobs = getIntent().getParcelableExtra("dataModel");

            String salaryRange = "$" + modelAllJobs.getMin_salary() + " - $" + modelAllJobs.getMax_salary();
            String postedBy = "By " +modelAllJobs.getIndustryDataModel().getName();
            sanFrancisco.setText(modelAllJobs.getCity());

            Glide.with(AdminJobDetailActivity.this).load(modelAllJobs.getUserDataModel().getProfile_image()).into(reminder);
            dollar.setText(salaryRange);
            airBnb.setText( modelAllJobs.getUserDataModel().getName());
            product.setText(postedBy);
            design.setText(modelAllJobs.getJob_title());

            if (modelAllJobs.getCreated_at() !=null){
                Moment moment;
                try {
                    moment = Iso8601Format.EXTENDED_DATE_TIME_OFFSET.parse(modelAllJobs.getCreated_at());
                    String ago = PrettyTime.of(Locale.getDefault()).printRelativeInStdTimezone(moment);
                    ago = ago.replace("moments", "sec");
                    ago = ago.replace("minutes", "min");
                    ago = ago.replace("seconds", "sec");
                    ago = ago.replace("hours", "hrs");
                    ago = ago.replace("days", "d");
                    text_time.setText(ago);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void setUpAdminApplicantsRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        hiredApplicantsRecyclerView.setLayoutManager(linearLayoutManager);
        hiredApplicantAdapter = new HiredApplicantAdapter(AdminJobDetailActivity.this, list, this);
        hiredApplicantsRecyclerView.setAdapter(hiredApplicantAdapter);
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

                        ModelAdminApplicants dataModel = new ModelAdminApplicants();
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
            hiredApplicantAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(AdminJobDetailActivity.this, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+list.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(list.get(position).getUser_id()));
        intent.putExtra("second_user_picture", list.get(position).getProfile_image());
        intent.putExtra("second_user_name", list.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}