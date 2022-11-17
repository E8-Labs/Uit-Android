package com.antizon.uit_android.generic.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

public class LatestJobAdapter extends RecyclerView.Adapter<LatestJobAdapter.MyViewHolder> {

    Context context;
    List<ApplicantHomeJobDataModel> latestJobsList;
    LatestJobAdapterCallBack callBack;

    public LatestJobAdapter(Context context, List<ApplicantHomeJobDataModel> latestJobsList, LatestJobAdapterCallBack callBack) {
        this.context = context;
        this.latestJobsList = latestJobsList;
        this.callBack = callBack;

    }

    @NonNull
    @Override
    public LatestJobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_latest_job, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LatestJobAdapter.MyViewHolder holder, final int position) {
        ApplicantHomeJobDataModel latestJobDataModel = latestJobsList.get(getItemViewType(position));

        if (latestJobDataModel != null){
            String jobAddress = latestJobDataModel.getCity() + ", " + latestJobDataModel.getState();
            String jobSalary = "$" + latestJobDataModel.getMin_salary() + " - $" + latestJobDataModel.getMax_salary();

            Glide.with(context)
                    .load(latestJobDataModel.getCompanyDataModel().getProfile_image())
                    .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                    .into(holder.jobImage);
            holder.jobTitle.setText(latestJobDataModel.getJob_title());
            holder.company.setText(latestJobDataModel.getCompanyDataModel().getName());

            holder.address.setText(jobAddress);
            holder.matchPercent.setText("");
            holder.salaryRange.setText(jobSalary);
            holder.jobType.setText(latestJobDataModel.getIndustryDataModel().getName());
            holder.timing.setText("");

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(latestJobDataModel));
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ApplicantHomeJobDataModel> filteredList) {
        latestJobsList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return latestJobsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout itemLayout;
        ImageView jobImage;
        TextView jobTitle, address, company, matchPercent, salaryRange, jobType, timing;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.itemLayout);
            jobImage = itemView.findViewById(R.id.jobImage);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            company = itemView.findViewById(R.id.companyName);
            address = itemView.findViewById(R.id.address);
            matchPercent = itemView.findViewById(R.id.match);
            salaryRange = itemView.findViewById(R.id.salaryRange);
            jobType = itemView.findViewById(R.id.jobType);
            timing = itemView.findViewById(R.id.timing);
        }
    }

    public interface LatestJobAdapterCallBack{
        void onItemClick(ApplicantHomeJobDataModel jobDataModel);
    }
}

