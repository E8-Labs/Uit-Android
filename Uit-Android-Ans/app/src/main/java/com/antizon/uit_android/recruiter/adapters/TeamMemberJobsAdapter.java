package com.antizon.uit_android.recruiter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.jobs.JobPeopleAdapter;
import com.antizon.uit_android.adapters.jobs.JobTagAdapter;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.utilities.ArfiDeveloperTime;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;
import java.util.List;

public class TeamMemberJobsAdapter extends RecyclerView.Adapter<TeamMemberJobsAdapter.ViewHolder> implements JobPeopleAdapter.JobPeopleAdapterCallBack {

    Context context;
    List<SingleJobDataModel> jobsList;
    TeamMemberJobsAdapterCallBack callBack;

    public TeamMemberJobsAdapter(Context context, List<SingleJobDataModel> jobsList, TeamMemberJobsAdapterCallBack callBack) {
        this.context = context;
        this.jobsList = jobsList;
        this.callBack = callBack;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.team_member_job_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SingleJobDataModel current = jobsList.get(position);
        if (current != null){
            final GenericApplicantDataModel user = current.getUser();
            final GenericApplicantDataModel company = current.getCompany();
            if (user != null) {
                holder.tvName.setText(user.getName());
                holder.tvProfession.setText(user.getJob_title());
                Utilities.loadImage(context, user.getProfile_image(), holder.ivProfile);
            }
            if (company !=null){
                Utilities.loadImage(context, company.getProfile_image(), holder.ivSubProfile);
                holder.tvCompany.setText(company.getName());
            }

            holder.tvTime.setText(new ArfiDeveloperTime().getPrettyTimeFromCreatedAt(current.getCreatedAt()));
            holder.tvDescription.setText(current.getJobTitle());
            String location = current.getCity() + ", " + current.getState();
            holder.tvAddress.setText(location);


            // set tags
            final List<TagModel> tags = new ArrayList<>();

            // salary range
            tags.add(new TagModel("$" + Utilities.getShortAmount(current.getMinSalary()) + " - $" + Utilities.getShortAmount(current.getMaxSalary())));
            // location status
            tags.add(new TagModel(Utilities.getLocationStatus(current.getLocationStatus())));
            // Employment type
            tags.add(new TagModel(Utilities.getEmploymentType(current.getEmploymentType())));

            holder.rvTags.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            JobTagAdapter jobTagAdapter = new JobTagAdapter(context, tags);
            holder.rvTags.setAdapter(jobTagAdapter);


            // set people
            final List<GenericApplicantDataModel> applications = current.getApplicantsList();
            if (applications != null && !applications.isEmpty()) {
                holder.rvPeople.setVisibility(View.VISIBLE);
                holder.tvNoPeople.setVisibility(View.GONE);
                holder.rvPeople.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                JobPeopleAdapter jobPeopleAdapter = new JobPeopleAdapter(context, applications, this);
                holder.rvPeople.setAdapter(jobPeopleAdapter);
            } else {
                holder.rvPeople.setVisibility(View.GONE);
                holder.tvNoPeople.setVisibility(View.VISIBLE);
            }


            holder.itemView.setOnClickListener(v -> callBack.onJobItemClicked(position));
        }


    }

    @Override
    public int getItemCount() {
        return jobsList == null ? 0 : jobsList.size();
    }

    @Override
    public void onJobPeopleItemClicked(int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile, ivSubProfile;
        TextView tvName, tvProfession, tvTime;
        TextView tvDescription, tvCompany, tvAddress, tvNoPeople;
        RecyclerView rvTags, rvPeople;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivSubProfile = itemView.findViewById(R.id.iv_sub_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvProfession = itemView.findViewById(R.id.tv_profession);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvAddress = itemView.findViewById(R.id.tv_address);
            rvTags = itemView.findViewById(R.id.rv_tags);
            rvPeople = itemView.findViewById(R.id.rv_people);
            tvNoPeople = itemView.findViewById(R.id.tv_no_people);
        }
    }


    public interface TeamMemberJobsAdapterCallBack{
        void onJobItemClicked(int position);
    }
}