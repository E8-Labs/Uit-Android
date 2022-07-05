package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.EducationAdapter;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.CustomSwipeHelper;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ApplicantEducationActivity extends AppCompatActivity implements EducationAdapter.EducationAdapterCallBack {
    Context context;

    SessionManagement sessionManagement;
    RecyclerView applicantEducationRecyclerview;
    EducationAdapter educationAdapter;
    ArrayList<ApplicantEducationDataModel> educationList;
    ImageView backIcon;
    ImageView plus;
    RoundedImageView menYellow;
    TextView next;

    String encodedImageData, employeStatus;

    int updatedPosition;
    AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        context =  ApplicantEducationActivity.this;

        Utilities.setWhiteBars(ApplicantEducationActivity.this);

        initViews();

    }


    void initViews() {
        encodedImageData = getIntent().getStringExtra("profilePic");
        employeStatus = getIntent().getStringExtra("employeStatus");

        educationList = new ArrayList<>();
        sessionManagement = new SessionManagement(context);

        backIcon = findViewById(R.id.backIcon);
        menYellow = findViewById(R.id.menYellow);
        plus = findViewById(R.id.plus);
        next = findViewById(R.id.next);
        applicantEducationRecyclerview = findViewById(R.id.applicant_education_recyclerview);

        backIcon.setOnClickListener(v -> onBackPressed());

        plus.setOnClickListener(v -> {
            Intent addEducationIntent = new Intent(context,  ApplicantAddEducationActivity.class);
            addEducationIntent.putExtra("type", "add");
            onEducationAddedLauncher.launch(addEducationIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        next.setOnClickListener(view -> {
            if (educationList.size() == 0) {
                CustomCookieToast.showRequiredToast(ApplicantEducationActivity.this, "Please add minimum one education");
            } else {
                openNextScreen();
            }
        });

        showAllEducationsRecyclerview(applicantEducationRecyclerview, educationList);

        Utilities.loadImage(ApplicantEducationActivity.this, sessionManagement.getProfileImage(), menYellow);

        new CustomSwipeHelper(this, applicantEducationRecyclerview) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new CustomSwipeHelper.UnderlayButton(ApplicantEducationActivity.this,
                        "delete",
                        R.drawable.delete_job_ic,
                        Color.parseColor("#00FFFFFF"),
                        pos -> showDeleteEducationLibraryPopup(pos)
                ));
            }

        };
    }


    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteEducationLibraryPopup(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantEducationActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantEducationActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);


        String deleteTest = "Delete Education?";
        String areYouSure = "Are you sure you want to delete this education";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            educationList.remove(position);
            educationAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(true);
    }

    private void showAllEducationsRecyclerview(RecyclerView recyclerView, ArrayList<ApplicantEducationDataModel> educationList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        educationAdapter = new EducationAdapter(context, educationList , this);
        recyclerView.setAdapter(educationAdapter);
    }

    void openNextScreen() {
        Intent intent = new Intent(ApplicantEducationActivity.this, ApplicantDepartmentActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("employeStatus", employeStatus);
        intent.putParcelableArrayListExtra("educationList", educationList);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    public void onEditClicked(int position) {
        updatedPosition = position;
        Intent addEducationIntent = new Intent(context,  ApplicantAddEducationActivity.class);
        addEducationIntent.putExtra("type", "edit");
        addEducationIntent.putExtra("education", educationList.get(position));
        onUpdateEducationItemLauncher.launch(addEducationIntent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    // Activity Launchers With CallBacks//

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onUpdateEducationItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ApplicantEducationDataModel applicantEducationDataModel = intent.getParcelableExtra("applicantEducationDataModel");
                educationList.get(updatedPosition).setApplicantDegreeData(applicantEducationDataModel.getApplicantDegreeData());
                educationList.get(updatedPosition).setFieldOfStudy(applicantEducationDataModel.getFieldOfStudy());
                educationList.get(updatedPosition).setSchoolName(applicantEducationDataModel.getSchoolName());
                educationList.get(updatedPosition).setStartDate(applicantEducationDataModel.getStartDate());
                educationList.get(updatedPosition).setEndDate(applicantEducationDataModel.getEndDate());
                educationAdapter.notifyDataSetChanged();
            }
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onEducationAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ApplicantEducationDataModel applicantEducationDataModel = intent.getParcelableExtra("applicantEducationDataModel");
                educationList.add(applicantEducationDataModel);
                educationAdapter.notifyDataSetChanged();
            }
        }
    });


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

}