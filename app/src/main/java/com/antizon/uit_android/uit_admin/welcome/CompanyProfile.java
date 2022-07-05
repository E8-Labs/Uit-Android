package com.antizon.uit_android.uit_admin.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.activities.ActivityDemoInfo;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CompanyProfile extends BaseActivity {
    private static final String TAG = CompanyProfile.class.getSimpleName();
    ImageView noahImage, teamMemberImage, grayBack, arrowOne, arrowTwo, arrow, teamMember, teamMemberTwo;
    TextView noahEmail, userAddress, roleText, employ, phoneNumber, series2, pause, reject, hi, product, finance,message;
    String userIdValue = "";
    ConstraintLayout teamMemberLayout;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ModelUitAdminApproved dataModel;
    String role ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        setIds();
        getIntentData();
        initialize();
        initializeViewsAsPerRole();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        companyProfile();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        noahImage = findViewById(R.id.noahImage);
        teamMemberLayout = findViewById(R.id.teamMemberLayout);
        teamMemberTwo = findViewById(R.id.teamMemberTwo);
        teamMemberImage = findViewById(R.id.teamMemberImage);
        teamMember = findViewById(R.id.teamMember);
        grayBack = findViewById(R.id.grayBack);
        arrowOne = findViewById(R.id.arrowOne);
        arrowTwo = findViewById(R.id.arrowTwo);
        arrow = findViewById(R.id.arrow);
        noahEmail = findViewById(R.id.noahEmail);
        userAddress = findViewById(R.id.userAddress);
        roleText = findViewById(R.id.roleText);
        employ = findViewById(R.id.employ);
        phoneNumber = findViewById(R.id.phoneNumber);
        series2 = findViewById(R.id.series2);
        pause = findViewById(R.id.pause);
        reject = findViewById(R.id.reject);
        hi = findViewById(R.id.hi);
        product = findViewById(R.id.product);
        finance = findViewById(R.id.finance);
        message = findViewById(R.id.message);

    }

    void getIntentData() {
        if (getIntent().getExtras().getSerializable("dataModel") != null) {
            dataModel = (ModelUitAdminApproved) getIntent().getExtras().getSerializable("dataModel");
            noahEmail.setText(dataModel.getEmail());
            Glide.with(CompanyProfile.this)
                    .load(dataModel.getProfile_image())
                    .circleCrop()
                    .into(noahImage);
            hi.setText(dataModel.getName());

            Glide.with(CompanyProfile.this)
                    .load(dataModel.getProfile_image())
                    .circleCrop()
                    .into(teamMemberImage);

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(CompanyProfile.this);

        role=sessionManagement.getRole();
        loadProfile(CompanyProfile.this, sessionManagement.getProfileImage(), noahImage);

        message.setText("Message Owner");
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


        } else if (role.equalsIgnoreCase("2")) {

        } else if (role.equalsIgnoreCase("3")) {

            reject.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);

        } else if (role.equalsIgnoreCase("4")) {

        } else if (role.equalsIgnoreCase("5")) {

        }
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        arrowOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProfile.this, AllListedJobs.class);
                startActivity(intent);

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProfile.this, AdminMessage.class);
                startActivity(intent);

            }
        });
        arrowTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProfile.this, ActivityDemoInfo.class);
                intent.putExtra("dataModel", dataModel);
                startActivity(intent);

            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProfile.this, DeiInformation.class);

                intent.putExtra("dataModel", dataModel);

                startActivity(intent);

            }
        });
        teamMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProfile.this, CompanyTeam.class);

                intent.putExtra("dataModel", dataModel);

                startActivity(intent);

            }
        });
    }
    void companyProfile() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "6");
        sendServerRequestPOST(AppConstants.GET_PROFILE, params, sessionManagement.getToken());
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
                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());
                String bio = dataObject.getString("bio");
                Log.d(TAG, "onResponse: bio " + bio);
                String address = dataObject.getString("address");
                Log.d(TAG, "onResponseReceived: "+address);
                String state = dataObject.getString("state");
                Log.d(TAG, "onResponseReceived: "+state);
                String phone = dataObject.getString("phone");
                Log.d(TAG, "onResponseReceived: "+phone);
                dataModel.setState(state);
                dataModel.setPhone(phone);

                String website = dataObject.getString("website");
                Log.d(TAG, "onResponse: website " + website);

                roleText.setText(bio);
                phoneNumber.setText(website);
                userAddress.setText(address);

                JSONObject dei_statementObject = dataObject.getJSONObject("dei_statement");
                Log.d(TAG, "onResponse: dei_statement: size: " + dei_statementObject.length());


                String deistatement = dei_statementObject.getString("deistatement");
                int team_lead = dei_statementObject.getInt("team_lead");

                int programming = dei_statementObject.getInt("programming");
                int training = dei_statementObject.getInt("training");
                int men = dei_statementObject.getInt("men");
                int women = dei_statementObject.getInt("women");
                int lgbt = dei_statementObject.getInt("lgbt");
                int erg = dei_statementObject.getInt("erg");

                dataModel.setDeistatement(deistatement);
                dataModel.setTeam_lead(team_lead);
                dataModel.setProgramming(programming);
                dataModel.setTraining(training);
                dataModel.setMen(men);
                dataModel.setWomen(women);
                dataModel.setLgbt(lgbt);
                dataModel.setErg(erg);


                JSONArray user_sizesArray = dataObject.getJSONArray("user_sizes");
                Log.d(TAG, "onResponse: user_sizes: size: " + user_sizesArray.length());
                for (int i = 0; i < user_sizesArray.length(); i++) {

                    JSONObject jsonObject1 = user_sizesArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    employ.setText(name);
                }
                JSONArray user_stagesArray = dataObject.getJSONArray("user_stages");
                Log.d(TAG, "onResponse: user_stages: size: " + user_stagesArray.length());
                for (int i = 0; i < user_stagesArray.length(); i++) {

                    JSONObject jsonObject1 = user_stagesArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    series2.setText(name);
                }
                JSONArray industriesArray = dataObject.getJSONArray("industries");
                Log.d(TAG, "onResponse: industries: size: " + industriesArray.length());
                for (int i = 0; i < industriesArray.length(); i++) {

                    JSONObject jsonObject1 = industriesArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    product.setText(name);
                }
                JSONArray team_membersArray = dataObject.getJSONArray("team_members");
                Log.d(TAG, "onResponse: team_members: size: " + team_membersArray.length());
                for (int i = 0; i < team_membersArray.length(); i++) {

                    JSONObject jsonObject1 = team_membersArray.getJSONObject(i);
                    String profile_image = jsonObject1.getString("profile_image");
                    Log.d(TAG, "onResponse: profile_image " + profile_image);

                    setMemberImageView(i, profile_image);
                }
            }
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

    void setMemberImageView(int position, String imageUrl) {
        if (position == 1) {
            applyImage(teamMemberImage, imageUrl);
        } else if (position == 2) {
            applyImage(teamMember, imageUrl);
        } else if (position == 3) {
            applyImage(teamMemberTwo, imageUrl);
        }
    }

    void applyImage(ImageView imageView, String imageUrl) {
        Glide.with(CompanyProfile.this)
                .load(imageUrl)
                .circleCrop()
                .into(imageView);
    }

}