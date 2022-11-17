package com.antizon.uit_android.activities.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.adapters.applicant.CompanyHiredApplicantAdapter;
import com.antizon.uit_android.adapters.jobs.JobTagAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.ArfiDeveloperTime;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;
import java.util.List;


public class FilledJobDetailActivity extends AppCompatActivity implements CompanyHiredApplicantAdapter.HiredApplicantAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;

    ImageView ivClose, ivProfile;
    TextView tvTitle, tvCompany, tvAddress, postedByName, postedByJobTitle, tvTime;

    RecyclerView recyclerview_hiredApplicants, recyclerViewTags;
    List<GenericApplicantDataModel> hiredApplicantsList;
    CompanyHiredApplicantAdapter hiredApplicantAdapter;

    LinearLayout layout_hiredApplicants;

    SingleJobDataModel jobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filled_job_detail);
        Utilities.setCustomStatusAndNavColor(FilledJobDetailActivity.this, R.color.white, R.color.white_dash);
        context = FilledJobDetailActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        jobDataModel = getIntent().getParcelableExtra("jobDataModel");

        ivClose = findViewById(R.id.iv_close);
        ivProfile = findViewById(R.id.iv_profile);
        tvTitle = findViewById(R.id.tv_description);
        tvAddress = findViewById(R.id.tv_address);
        tvCompany = findViewById(R.id.tv_company);
        postedByName = findViewById(R.id.postedByName);
        postedByJobTitle = findViewById(R.id.postedByJobTitle);
        tvTime = findViewById(R.id.tv_time);
        recyclerViewTags = findViewById(R.id.rv_tags);
        recyclerview_hiredApplicants = findViewById(R.id.recyclerview_hiredApplicants);
        layout_hiredApplicants = findViewById(R.id.layout_hiredApplicants);

        showFilledJobDetail();

        ivClose.setOnClickListener(v -> onBackPressed());
    }


    private void showFilledJobDetail() {
        final GenericApplicantDataModel user = jobDataModel.getUser();
        final GenericApplicantDataModel company = jobDataModel.getCompany();
        if (company != null) {
            String companyLocation =  company.getCity() + ", " + company.getState();
            Utilities.loadImage(context, company.getProfile_image(), ivProfile);
            tvCompany.setText(company.getName());
            tvAddress.setText(companyLocation);

        }
        if (user != null) {
            String postedBy = "Posted by " + user.getName();
            postedByName.setText(postedBy);
            postedByJobTitle.setText(user.getJob_title());
        }

        tvTitle.setText(jobDataModel.getJobTitle());
        tvTime.setText(new ArfiDeveloperTime().getPrettyTimeFromCreatedAt(jobDataModel.getCreatedAt()));

        // set tags
        final List<TagModel> tags = new ArrayList<>();
        // salary range
        tags.add(new TagModel("$" + Utilities.getShortAmount(jobDataModel.getMinSalary()) + " - $" + Utilities.getShortAmount(jobDataModel.getMaxSalary())));
        // location status
        tags.add(new TagModel(Utilities.getLocationStatus(jobDataModel.getLocationStatus())));
        // Employment type
        tags.add(new TagModel(Utilities.getEmploymentType(jobDataModel.getEmploymentType())));

        recyclerViewTags.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        JobTagAdapter jobTagAdapter = new JobTagAdapter(context, tags);
        recyclerViewTags.setAdapter(jobTagAdapter);

        // set people
        hiredApplicantsList = new ArrayList<>();
        hiredApplicantsList = jobDataModel.getApplicantsList();
        if (hiredApplicantsList != null && !hiredApplicantsList.isEmpty()) {
            showHiredApplicantsRecyclerView(recyclerview_hiredApplicants, hiredApplicantsList);
        } else {
            layout_hiredApplicants.setVisibility(View.GONE);
        }

    }



    private void showHiredApplicantsRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> uitMembersList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        hiredApplicantAdapter = new CompanyHiredApplicantAdapter(context, uitMembersList, this);
        recyclerView.setAdapter(hiredApplicantAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onMessageClicked(int position) {

        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+hiredApplicantsList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(hiredApplicantsList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", hiredApplicantsList.get(position).getProfile_image());
        intent.putExtra("second_user_name", hiredApplicantsList.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}