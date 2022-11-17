package com.antizon.uit_android.adapters.applicant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplicantFeaturedJobsAdapter extends RecyclerView.Adapter<ApplicantFeaturedJobsAdapter.ViewHolder> {

    Context context;
    List<ApplicantHomeJobDataModel> featuredJobsList;
    ApplicantFeaturedJobsAdapterCallBack callBack;

    public ApplicantFeaturedJobsAdapter(Context context, List<ApplicantHomeJobDataModel> featuredJobsList, ApplicantFeaturedJobsAdapterCallBack callBack) {
        this.context = context;
        this.featuredJobsList = featuredJobsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_featured_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ApplicantHomeJobDataModel featuredJobModel = featuredJobsList.get(position );

        if (featuredJobModel != null) {

            if (Double.valueOf(featuredJobModel.getMatch()) != null){
                String match = featuredJobModel.getMatch() + "% match";
                holder.text_matchPercentage.setText(match);
            }

            Utilities.loadImage(context, featuredJobModel.getCompanyDataModel().getProfile_image(), holder.jobImage);
            holder.text_jobTitle.setText(featuredJobModel.getJob_title());
            holder.text_companyName.setText(featuredJobModel.getCompanyDataModel().getName());
            String location = featuredJobModel.getCity() + ", " + featuredJobModel.getState();
            holder.text_location.setText(location);
            showJobTagsRecyclerview(holder.recyclerview_jobTags, featuredJobModel);

            holder.itemView.setOnClickListener(v -> callBack.onFeaturedJobClicked(position));
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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView jobImage;
        TextView text_jobTitle, text_companyName, text_location, text_matchPercentage;
        RecyclerView recyclerview_jobTags;

        public ViewHolder(View itemView) {
            super(itemView);
            jobImage= itemView.findViewById(R.id.jobImage);
            text_jobTitle= itemView.findViewById(R.id.text_jobTitle);
            text_companyName= itemView.findViewById(R.id.text_companyName);
            text_location= itemView.findViewById(R.id.text_location);
            text_matchPercentage= itemView.findViewById(R.id.text_matchPercentage);
            recyclerview_jobTags= itemView.findViewById(R.id.recyclerview_jobTags);
        }
    }

    private void showJobTagsRecyclerview(RecyclerView recyclerView, ApplicantHomeJobDataModel jobDataModel){
        // set tags
        final List<TagModel> tags = new ArrayList<>();
        // salary range
        tags.add(new TagModel("$" + Utilities.getShortAmount(jobDataModel.getMin_salary()) + " - $" + Utilities.getShortAmount(jobDataModel.getMax_salary())));
        // location status
        tags.add(new TagModel(Utilities.getLocationStatus(jobDataModel.getLocation_status())));
        // Employment type
        tags.add(new TagModel(Utilities.getEmploymentType(jobDataModel.getEmployment_type())));

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        FeaturedJobTagAdapter jobTagAdapter = new FeaturedJobTagAdapter(context, tags);
        recyclerView.setAdapter(jobTagAdapter);
    }

    public interface ApplicantFeaturedJobsAdapterCallBack {
        void onFeaturedJobClicked(int position);
    }
}