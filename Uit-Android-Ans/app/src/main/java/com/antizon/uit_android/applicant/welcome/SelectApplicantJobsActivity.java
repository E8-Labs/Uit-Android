package com.antizon.uit_android.applicant.welcome;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantSelectJobActivity;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectApplicantJobsActivity extends AppCompatActivity{
    Context context;
    GetDataService service;

    ProgressDialog progressDialog;
    ImageView backIcon, menYellow;
    TextView next, btn_selectJobs;

    SessionManagement sessionManagement;

    TagContainerLayout tagContainerLayout;

    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    List<NameIdModel> applicantJobsList;

    String employeValue = "", from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_applicant_jobs);
        Utilities.setWhiteBars(SelectApplicantJobsActivity.this);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        context = SelectApplicantJobsActivity.this;

        initViews();
    }


    private void initViews() {

        backIcon = findViewById(R.id.backIcon);
        tagContainerLayout = findViewById(R.id.tag_container_industries);
        btn_selectJobs = findViewById(R.id.btn_selectJobs);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")) {
            applicantJobsList = new ArrayList<>();
            applicantJobsList = getIntent().getParcelableArrayListExtra("applicantJobsList");

        }else {
            employeValue = getIntent().getStringExtra("employeStatus");
            educationList = getIntent().getParcelableArrayListExtra("educationList");
            selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");

        }

        progressDialog = new ProgressDialog(SelectApplicantJobsActivity.this);
        sessionManagement = new SessionManagement(SelectApplicantJobsActivity.this);
        Utilities.loadImage(SelectApplicantJobsActivity.this, sessionManagement.getProfileImage(), menYellow);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> openNextScreen());


        selectedJobsList = new ArrayList<>();

        if (from.equals("edit")){
            for (int i = 0; i < applicantJobsList.size(); i++) {
                NameIdModel nameIdModel = applicantJobsList.get(i);
                selectedJobsList.add(new ApplicantJobDataModel(nameIdModel.getId(), nameIdModel.getName()));
                tagContainerLayout.addTag(nameIdModel.getName());
                tagContainerLayout.setVisibility(View.VISIBLE);
            }
        }


        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {
            }

            @Override
            public void onSelectedTagDrag(int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
                if (selectedJobsList != null && selectedJobsList.size() > position) {
                    tagContainerLayout.removeTag(position);
                    selectedJobsList.remove(position);

                    if (selectedJobsList.size() == 0){
                        tagContainerLayout.setVisibility(View.GONE);
                    }else{
                        tagContainerLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btn_selectJobs.setOnClickListener(v -> {
            Intent selectDegreeIntent = new Intent(context, ApplicantSelectJobActivity.class);
            onSelectedLauncher.launch(selectDegreeIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });
    }

    ActivityResultLauncher<Intent> onSelectedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ApplicantJobDataModel applicantJobDataModel = intent.getParcelableExtra("applicantJobDataModel");
                tagContainerLayout.setVisibility(View.VISIBLE);
                tagContainerLayout.addTag(applicantJobDataModel.getName());
                selectedJobsList.add(applicantJobDataModel);
            }
        }
    });

    private void openNextScreen() {

        if (selectedJobsList.size()==0){
            CustomCookieToast.showRequiredToast(SelectApplicantJobsActivity.this, "Please select minimum one job you are interested in");
        }else {
            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                requestForUpdateApplicantJobsList(selectedJobsList);
            }else{
                Intent intent = new Intent(SelectApplicantJobsActivity.this, ApplicantCompanyDetailActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("employeStatus", employeValue);
                intent.putParcelableArrayListExtra("educationList", educationList);
                intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
                intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
                onProfileUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }

        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }



    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    private void requestForUpdateApplicantJobsList(ArrayList<ApplicantJobDataModel> selectedJobsList) {

        JsonArray jobsList = new JsonArray();
        applicantJobsList = new ArrayList<>();

        for (int i = 0; i < selectedJobsList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("job_id", selectedJobsList.get(i).getId());
            jobsList.add(jsonObject);

            applicantJobsList.add(new NameIdModel(selectedJobsList.get(i).getId(), selectedJobsList.get(i).getName()));
        }

        JsonObject mainObject = new JsonObject();
        mainObject.add("jobs", jobsList);

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<MainResponseModel> call = service.updateProfessionalInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("applicantJobsList", (ArrayList<? extends Parcelable>) applicantJobsList);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(SelectApplicantJobsActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(SelectApplicantJobsActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(SelectApplicantJobsActivity.this, "Failure!", t.getMessage());
            }
        });
    }
}