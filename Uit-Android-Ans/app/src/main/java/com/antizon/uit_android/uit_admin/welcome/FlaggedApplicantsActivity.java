package com.antizon.uit_android.uit_admin.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.FlaggedApplicantsAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.report.flaguser.ApplicantFlagDataModel;
import com.antizon.uit_android.models.report.flaguser.ApplicantFlaggedUsersResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlaggedApplicantsActivity extends BaseActivity implements FlaggedApplicantsAdapter.FlaggedApplicantsAdapterCallback {

    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    TextView text_heading;
    RelativeLayout btnBack;
    RecyclerView recyclerview_flaggedApplicants;
    List<ApplicantFlagDataModel> flaggedApplicantsList;
    FlaggedApplicantsAdapter flaggedApplicantsAdapter;
    EditText edSearch;
    RelativeLayout layout_noApplicants;
    String  role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_applicants);
        Utilities.setCustomStatusAndNavColor(FlaggedApplicantsActivity.this, R.color.white_dash, R.color.white_dash);
        context = FlaggedApplicantsActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        role = getIntent().getStringExtra("role");
        btnBack = findViewById(R.id.btnBack);
        text_heading = findViewById(R.id.text_heading);
        edSearch = findViewById(R.id.edSearch);
        recyclerview_flaggedApplicants = findViewById(R.id.recyclerview_flaggedApplicants);
        layout_noApplicants = findViewById(R.id.layout_noApplicants);


        String headerText;
        if (role.equals("5")){
            headerText = "Flagged Applicants";
        }else{
            headerText = "Flagged Members";
        }
        text_heading.setText(headerText);

        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        requestForFlaggedApplicants("Bearer " + sessionManagement.getToken(), role);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());

    }

    private void filter(String text) {
        ArrayList<ApplicantFlagDataModel> filteredList = new ArrayList<>();
        for (ApplicantFlagDataModel item : flaggedApplicantsList) {
            if (item.getFlaggedBy().getName().toLowerCase().contains(text.toLowerCase()) || item.getFlaggedPerson().getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.size() == 0){
            recyclerview_flaggedApplicants.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerview_flaggedApplicants.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            flaggedApplicantsAdapter.filterList(filteredList);
        }


    }

    public void requestForFlaggedApplicants(String token, String role){
        Call<ApplicantFlaggedUsersResponseModel> call = service.getFlaggedApplicants(token, role);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantFlaggedUsersResponseModel> call, @NonNull Response<ApplicantFlaggedUsersResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        flaggedApplicantsList = new ArrayList<>();
                        flaggedApplicantsList = response.body().getFlaggedApplicantsList();
                        recyclerview_flaggedApplicants.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);
                        showFlaggedApplicantsRecyclerView(recyclerview_flaggedApplicants, flaggedApplicantsList);
                    }
                } else {
                    CustomCookieToast.showFailureToast(FlaggedApplicantsActivity.this, response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApplicantFlaggedUsersResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(FlaggedApplicantsActivity.this, t.getMessage());
            }
        });
    }

    private void showFlaggedApplicantsRecyclerView(RecyclerView recyclerView, List<ApplicantFlagDataModel> applicantsList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        flaggedApplicantsAdapter = new FlaggedApplicantsAdapter(context, applicantsList, this);
        recyclerView.setAdapter(flaggedApplicantsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    public void onItemClick(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_admin_applicant_flagged, findViewById(R.id.bottom_sheet_option));

        RoundedImageView flaggedByApplicantProfile = sheetView.findViewById(R.id.flaggedByApplicantProfile);
        TextView text_flaggedByApplicantName = sheetView.findViewById(R.id.text_flaggedByApplicantName);
        TextView text_flaggedByPersonType = sheetView.findViewById(R.id.text_flaggedByPersonType);
        TextView btnMessageFlaggedByApplicant = sheetView.findViewById(R.id.btnMessageFlaggedByApplicant);

        RoundedImageView flaggedApplicantProfile = sheetView.findViewById(R.id.flaggedApplicantProfile);
        TextView text_flaggedApplicantName = sheetView.findViewById(R.id.text_flaggedApplicantName);
        TextView text_flaggedPersonType = sheetView.findViewById(R.id.text_flaggedPersonType);
        TextView btnMessageFlaggedApplicant = sheetView.findViewById(R.id.btnMessageFlaggedApplicant);

        TextView text_flaggedComment = sheetView.findViewById(R.id.text_flaggedComment);

        ApplicantFlagDataModel flaggedModel = flaggedApplicantsList.get(position);
        if (flaggedModel !=null){
            GenericApplicantDataModel flaggedByApplicantModel = flaggedModel.getFlaggedBy();
            GenericApplicantDataModel flaggedApplicantModel = flaggedModel.getFlaggedPerson();
            if (flaggedByApplicantModel !=null){
                Utilities.loadImage(context, flaggedByApplicantModel.getProfile_image(), flaggedByApplicantProfile);
                text_flaggedByPersonType.setText(Utilities.getPersonaType(flaggedByApplicantModel.getRole()));
                text_flaggedByApplicantName.setText(flaggedByApplicantModel.getName());

                btnMessageFlaggedByApplicant.setOnClickListener(v -> {
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+flaggedByApplicantModel.getUser_id());
                    intent.putExtra("second_user_id", String.valueOf(flaggedByApplicantModel.getUser_id()));
                    intent.putExtra("second_user_picture", flaggedByApplicantModel.getProfile_image());
                    intent.putExtra("second_user_name", flaggedByApplicantModel.getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                });
            }

            if (flaggedApplicantModel !=null){
                Utilities.loadImage(context, flaggedApplicantModel.getProfile_image(), flaggedApplicantProfile);
                text_flaggedPersonType.setText(Utilities.getPersonaType(flaggedApplicantModel.getRole()));
                text_flaggedApplicantName.setText(flaggedApplicantModel.getName());

                btnMessageFlaggedApplicant.setOnClickListener(v -> {
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+flaggedApplicantModel.getUser_id());
                    intent.putExtra("second_user_id", String.valueOf(flaggedApplicantModel.getUser_id()));
                    intent.putExtra("second_user_picture", flaggedApplicantModel.getProfile_image());
                    intent.putExtra("second_user_name", flaggedApplicantModel.getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                });
            }

            text_flaggedComment.setText(flaggedModel.getComment());


        }


        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}