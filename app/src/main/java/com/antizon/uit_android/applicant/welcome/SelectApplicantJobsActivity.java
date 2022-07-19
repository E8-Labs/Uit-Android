package com.antizon.uit_android.applicant.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.applicant.ApplicantSelectedJobsAdapter;
import com.antizon.uit_android.generic.adapter.ApplicantJobsAdapter;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobsListResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectApplicantJobsActivity extends AppCompatActivity implements ApplicantSelectedJobsAdapter.ApplicantSelectedJobsAdapterCallBack {
    Context context;
    GetDataService service;
    private static final String TAG = SelectApplicantJobsActivity.class.getSimpleName();

    ProgressDialog progressDialog;
    ImageView backIcon, menYellow;
    TextView next;
    EditText search;

    SessionManagement sessionManagement;

    MultiAutoCompleteTextView multiAutoCompleteTextView;
    RecyclerView applicantJobsRecyclerview, staggeredRecyclerview;
    ApplicantJobsAdapter applicantJobsAdapter;

    ArrayList<ApplicantJobDataModel> jobsList;
    ArrayList<String> dropDownJobsList = new ArrayList<>();
    String employeValue = "";
    int mPosition;

    TagContainerLayout tagContainerLayout;

    ApplicantSelectedJobsAdapter applicantSelectedJobsAdapter;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_applicant_jobs);
        Utilities.setWhiteBars(SelectApplicantJobsActivity.this);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        context = SelectApplicantJobsActivity.this;

        initViews();
    }


    void initViews() {

        backIcon = findViewById(R.id.backIcon);
        tagContainerLayout = findViewById(R.id.tag_container_industries);
        applicantJobsRecyclerview = findViewById(R.id.applicant_jobs_recyclerview);
        staggeredRecyclerview = findViewById(R.id.staggeredRecyclerview);
        search = findViewById(R.id.searchJob);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        multiAutoCompleteTextView = findViewById(R.id.searchJobAutoCompleteView);


        employeValue = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");

        progressDialog = new ProgressDialog(SelectApplicantJobsActivity.this);
        sessionManagement = new SessionManagement(SelectApplicantJobsActivity.this);
        Utilities.loadImage(SelectApplicantJobsActivity.this, sessionManagement.getProfileImage(), menYellow);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> openNextScreen());

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = search.getText().toString().toLowerCase(Locale.getDefault());
                applicantJobsAdapter.search(searchedString);
                Log.d(TAG, "onTextChanged: " + searchedString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedJobsList = new ArrayList<>();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantDepartments();

        setSelectedDepartmentsToRecyclerView(staggeredRecyclerview, selectedJobsList);

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
    }


    void openNextScreen() {
        if (selectedJobsList.size()==0){
            CustomCookieToast.showRequiredToast(SelectApplicantJobsActivity.this, "Please select minimum one job you are interested in");
        }else {
            Intent intent = new Intent(SelectApplicantJobsActivity.this, ApplicantCompanyDetailActivity.class);
            intent.putExtra("employeStatus", employeValue);
            intent.putParcelableArrayListExtra("educationList", educationList);
            intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
            intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
            onProfileUpdateLauncher.launch(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }

    }

    private void requestForGetApplicantDepartments() {
        Call<ApplicantJobsListResponseModel> call = service.getApplicantJobsList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantJobsListResponseModel> call, @NotNull Response<ApplicantJobsListResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            jobsList = new ArrayList<>();
                            dropDownJobsList = new ArrayList<>();
                            jobsList = response.body().getApplicantJobsList();

                            for (int i = 0; i < jobsList.size(); i++) {
                               dropDownJobsList.add(jobsList.get(i).getName());
                            }
                            setDepartmentsToRecyclerView(applicantJobsRecyclerview, jobsList);

                            applicantJobsAdapter.setFilterArrayListValue(jobsList);
                            setDropDownJobsListInitialize(dropDownJobsList);
                        }
                        else {
                            Toast.makeText(context, "Unsuccessful : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantJobsListResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDepartmentsToRecyclerView(RecyclerView recyclerView, List<ApplicantJobDataModel> jobsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        applicantJobsAdapter = new ApplicantJobsAdapter(context, jobsList);
        recyclerView.setAdapter(applicantJobsAdapter);
    }


    private void setSelectedDepartmentsToRecyclerView(RecyclerView recyclerView, List<ApplicantJobDataModel> jobsList) {
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        applicantSelectedJobsAdapter = new ApplicantSelectedJobsAdapter(context, jobsList, this);
        recyclerView.setAdapter(applicantSelectedJobsAdapter);
    }


    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    void setDropDownJobsListInitialize(List<String> dropDownJobsList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.tag_item, R.id.job_title, dropDownJobsList);
        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        multiAutoCompleteTextView.setOnTouchListener((v, event) -> {
            multiAutoCompleteTextView.showDropDown();
            return false;
        });


        multiAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            mPosition = position;
            tagContainerLayout.addTag(jobsList.get(position).getName());
            tagContainerLayout.setVisibility(View.VISIBLE);
            selectedJobsList.add(jobsList.get(position));
            multiAutoCompleteTextView.setText("");
            applicantSelectedJobsAdapter.notifyDataSetChanged();
        });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCrossBtnClicked(int position) {
        selectedJobsList.remove(position);
        applicantSelectedJobsAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}