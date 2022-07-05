package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.activities.ActivityApplicantJobDetail;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class FeatureJobAdapter extends RecyclerView.Adapter<FeatureJobAdapter.MyViewHolder> {

    Context context;
    List<ApplicantHomeJobDataModel> featuredJobsList;
    FeatureJobAdapterCallBack callBack;
    public FeatureJobAdapter(Context context, List<ApplicantHomeJobDataModel> featuredJobsList, FeatureJobAdapterCallBack callBack) {
        this.context = context;
        this.featuredJobsList = featuredJobsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public FeatureJobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeatureJobAdapter.MyViewHolder holder, final int position) {
        ApplicantHomeJobDataModel featuredJobDataModel = featuredJobsList.get(getItemViewType(position));

        if (featuredJobDataModel !=null){
            String address= featuredJobDataModel.getCity()+", "+featuredJobDataModel.getState();
            String salaryRange = "$"+featuredJobDataModel.getMin_salary()+" - $"+featuredJobDataModel.getMax_salary();

            Glide.with(context)
                    .load(featuredJobDataModel.getCompanyDataModel().getProfile_image())
                    .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                    .into(holder.jobImage);
            holder.jobTitle.setText(featuredJobDataModel.getJob_title());
            holder.company.setText(featuredJobDataModel.getCompanyDataModel().getName());
            holder.address.setText(address);
            holder.matchPercent.setText("");
            holder.salaryRange.setText(salaryRange);
            holder.jobType.setText(featuredJobDataModel.getIndustryDataModel().getName());
            holder.timing.setText("");

            holder.itemView.setOnClickListener(view -> callBack.onItemClick(featuredJobDataModel));
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return featuredJobsList.size();

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

    public interface FeatureJobAdapterCallBack{
        void onItemClick(ApplicantHomeJobDataModel jobDataModel);
    }
}