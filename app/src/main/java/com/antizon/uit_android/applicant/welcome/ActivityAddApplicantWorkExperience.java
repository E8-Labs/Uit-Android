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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.ApplicantReferenceAdapter;
import com.antizon.uit_android.generic.adapter.EducationAdapter;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.models.work.ApplicantReferenceModel;
import com.antizon.uit_android.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddApplicantWorkExperience extends AppCompatActivity implements ApplicantReferenceAdapter.ApplicantReferenceAdapterCallBack {
    Context context;
    TextView text_addWorkExperience;

    RecyclerView recyclerview_references;
    ArrayList<ApplicantReferenceModel> referencesList;
    ApplicantReferenceAdapter applicantReferenceAdapter;

    AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_applicant_work_experience);
        context = ActivityAddApplicantWorkExperience.this;
        Utilities.setCustomStatusAndNavColor(ActivityAddApplicantWorkExperience.this, R.color.white_dash, R.color.white_dash);

        text_addWorkExperience = findViewById(R.id.text_addWorkExperience);
        recyclerview_references = findViewById(R.id.recyclerview_references);

        referencesList = new ArrayList<>();

        text_addWorkExperience.setOnClickListener(v -> {
            Intent addEducationIntent = new Intent(context, AddApplicantReferenceActivity.class);
            onApplicantAddReference.launch(addEducationIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        showAllReferencesRecyclerview(recyclerview_references, referencesList);
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onApplicantAddReference = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent !=null){
                ApplicantReferenceModel applicantReferenceModel = intent.getParcelableExtra("applicantReferenceModel");
                referencesList.add(applicantReferenceModel);
                applicantReferenceAdapter.notifyDataSetChanged();
            }
        }
    });

    private void showAllReferencesRecyclerview(RecyclerView recyclerView, ArrayList<ApplicantReferenceModel> referencesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(context ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        applicantReferenceAdapter = new ApplicantReferenceAdapter(context, referencesList , this);
        recyclerView.setAdapter(applicantReferenceAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    @Override
    public void onRemoveClicked(int position) {
        showRemoveReferencePopup(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showRemoveReferencePopup(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddApplicantWorkExperience.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ActivityAddApplicantWorkExperience.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);


        String deleteTest = "Delete Reference?";
        String areYouSure = "Are you sure you want to delete this reference";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            referencesList.remove(position);
            applicantReferenceAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(true);
    }
}