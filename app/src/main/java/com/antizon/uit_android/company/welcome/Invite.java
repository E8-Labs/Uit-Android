package com.antizon.uit_android.company.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.CompanyInviteAdapter;
import com.antizon.uit_android.generic.model.ModelCompanyInvite;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Invite extends BaseActivity {

    private static final String TAG = Invite.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    EditText name, email;
    TextView sendInvitation, skip, addInvitation;
    ImageView backIcon, welcome, redNoah;
    String nameValue;
    ConstraintLayout inviteMember;

    RecyclerView companyInviteRecyclerview;
    CompanyInviteAdapter driverAdapter;
    private List<ModelCompanyInvite> list;
    private ModelCompanyInvite dataModel;

    ArrayList<ModelCompanySize> industriesList;
    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "", passwordValue = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "", genderValue = "", orientationValue = "", inviteValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        setIds();
        getIntentData();
        initialize();
        setListener();
        setUpCompanyInviteRecyclerview();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        companyInviteRecyclerview = findViewById(R.id.companyInviteRecyclerview);
        sendInvitation = findViewById(R.id.sendInvitation);
        skip = findViewById(R.id.skip);
        backIcon = findViewById(R.id.backIcon);
        redNoah = findViewById(R.id.redNoah);
        welcome = findViewById(R.id.welcome);
        inviteMember = findViewById(R.id.inviteMember);
    }

    void getIntentData() {

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            selectedStageId = getIntent().getStringExtra("stageId");
            selectedStageName = getIntent().getStringExtra("stageName");
            sizeValue = getIntent().getStringExtra("size");
            headquarterValue = getIntent().getStringExtra("headquarter");
            websiteValue = getIntent().getStringExtra("website");
            emailValue = getIntent().getStringExtra("email");
            verificationValue = getIntent().getStringExtra("verification");
            passwordValue = getIntent().getStringExtra("password");
            deiStatementValue = getIntent().getStringExtra("deiStatement");
            leadValue = getIntent().getStringExtra("lead");
            ergValue = getIntent().getStringExtra("erg");
            programmingValue = getIntent().getStringExtra("programming");
            trainingValue = getIntent().getStringExtra("training");
            genderValue = getIntent().getStringExtra("genderProportion");
            orientationValue = getIntent().getStringExtra("genderOrientation");

            Log.d(TAG, "getIntentData: image: " + encodedImageData);
            Log.d(TAG, "getIntentData: companyName: " + companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: " + typeHereValue);
            Log.d(TAG, "getIntentData: stageName: " + selectedStageName);
            Log.d(TAG, "getIntentData: stageId: " + selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: " + headquarterValue);
            Log.d(TAG, "getIntentData: website: " + websiteValue);
            Log.d(TAG, "getIntentData: email: " + emailValue);
            Log.d(TAG, "getIntentData: deiStatement: " + deiStatementValue);
            Log.d(TAG, "getIntentData: lead: " + leadValue);
            Log.d(TAG, "getIntentData: erg: " + ergValue);
            Log.d(TAG, "getIntentData: programming: " + programmingValue);
            Log.d(TAG, "getIntentData: training: " + trainingValue);
            Log.d(TAG, "getIntentData: genderProportion: " + genderValue);
            Log.d(TAG, "getIntentData: genderOrientation: " + orientationValue);


            Bundle bundle = getIntent().getExtras();

            industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Invite.this);
        sessionManagement = new SessionManagement(Invite.this);
        list = new ArrayList<>();
        loadProfile(Invite.this, encodedImageData, redNoah);
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

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTeamMemberBottomSheet();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextScreen();

            }
        });

        sendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size() > 0) {

                    attachParams();
                } else {
                    Toast.makeText(Invite.this, "Please add members to invite.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void setUpCompanyInviteRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Invite.this);
        companyInviteRecyclerview.setLayoutManager(linearLayoutManager);

        driverAdapter = new CompanyInviteAdapter(list, Invite.this, new CompanyInviteAdapter.SelectionListener() {
            @Override
            public void edit(ModelCompanyInvite dataModel) {
                addTeamMemberBottomSheet(dataModel);
            }

            @Override
            public void delete(int position) {
                list.remove(position);
                driverAdapter.notifyDataSetChanged();
            }
        });
        companyInviteRecyclerview.setAdapter(driverAdapter);
    }

    void addTeamMemberBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.activity_add_team);
        name = bottomSheetDialog.findViewById(R.id.name);
        email = bottomSheetDialog.findViewById(R.id.email);
        addInvitation = bottomSheetDialog.findViewById(R.id.addInvitation);
        inviteMember = bottomSheetDialog.findViewById(R.id.inviteMember);

        inviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        addInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {
                } else {
                    dataModel = new ModelCompanyInvite();
                    dataModel.setName(nameValue);
                    dataModel.setEmail(emailValue);
                    list.add(dataModel);

                    bottomSheetDialog.dismiss();
                }
                driverAdapter.notifyDataSetChanged();
            }
        });
        bottomSheetDialog.show();
    }

    void addTeamMemberBottomSheet(ModelCompanyInvite editDataModel) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.activity_add_team);
        name = bottomSheetDialog.findViewById(R.id.name);
        email = bottomSheetDialog.findViewById(R.id.email);
        addInvitation = bottomSheetDialog.findViewById(R.id.addInvitation);
        inviteMember = bottomSheetDialog.findViewById(R.id.inviteMember);
        name.setText(editDataModel.getName());
        email.setText(editDataModel.getEmail());

        inviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        addInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {

                } else {
                    dataModel = new ModelCompanyInvite();
                    dataModel.setName(nameValue);
                    dataModel.setEmail(emailValue);
                    list.add(dataModel);

                    bottomSheetDialog.dismiss();
                }
                driverAdapter.notifyDataSetChanged();
            }
        });
        bottomSheetDialog.show();

    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        nameValue = name.getText().toString().trim();
        emailValue = email.getText().toString().trim();

        boolean valid = true;
        if (nameValue.isEmpty()) {
            name.setError("Please enter your name");
            valid = false;
        }
        if (emailValue.isEmpty()) {
            email.setError("Please enter your email");
            valid = false;
        }
        return valid;
    }

    void attachParams() {

//        {"invitations": [{"name": Naeem, "email": naeem@gmail.com}, {"name": Salman, "email": salman@gmail.com}]}
//        HashMap<String, JSONArray> params = new HashMap<String, JSONArray>();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", list.get(i).getName());
                jsonObject.put("email", list.get(i).getEmail());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Log.d(TAG, "attachParams: exception: " + e.getMessage());
            }
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("invitations", "" + jsonArray);

        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.SEND_INVITES, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: ");
        progressDialog.setMessage("Sending invitations...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        Log.d(TAG, "onResponseReceived: ");
        progressDialog.dismiss();
        JSONObject jsonObject = null;

        try {
            Log.d(TAG, "onResponseReceived: response: " + response);
            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);

            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {

                Toast.makeText(Invite.this, "Invitation Sent.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        Log.d(TAG, "requestEndedWithError: ");

        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(Invite.this, Summary.class);

        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" + selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
        intent.putExtra("verification", verificationValue);
        intent.putExtra("password", passwordValue);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        intent.putExtra("erg", ergValue);
        intent.putExtra("programming", programmingValue);
        intent.putExtra("training", trainingValue);
        intent.putExtra("genderProportion", genderValue);
        intent.putExtra("genderOrientation", orientationValue);
        intent.putExtra("invite", inviteValue);
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}