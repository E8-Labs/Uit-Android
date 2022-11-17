package com.antizon.uit_android.adapters.jobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.applicant.ApplicantFeaturedJobsAdapter;
import com.antizon.uit_android.adapters.applicant.ApplicantRecommendedJobsAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ApplicantHomeAdapter extends RecyclerView.Adapter<ApplicantHomeAdapter.ViewHolder> implements ApplicantFeaturedJobsAdapter.ApplicantFeaturedJobsAdapterCallBack {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_LOADING = 2;
    boolean isLoadingAdded = false;
    Context context;
    List<ApplicantHomeJobDataModel> latestJobsList, featuredJobsList, recommendedJobsList;
    ApplicantHomeAdapterCallBack callBack;
    String applicantName;

    ApplicantFeaturedJobsAdapter featuredJobsAdapter;
    ApplicantRecommendedJobsAdapter recommendedJobsAdapter;

    public ApplicantHomeAdapter(Context context, List<ApplicantHomeJobDataModel> latestJobsList, List<ApplicantHomeJobDataModel> featuredJobsList, List<ApplicantHomeJobDataModel> recommendedJobsList, String applicantName, ApplicantHomeAdapterCallBack callBack) {
        this.context = context;
        this.latestJobsList = latestJobsList;
        this.featuredJobsList = featuredJobsList;
        this.recommendedJobsList = recommendedJobsList;
        this.applicantName = applicantName;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_TYPE_LOADING) {
            v = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
        } else if (viewType == VIEW_TYPE_HEADER) {
            v = LayoutInflater.from(context).inflate(R.layout.applicant_home_header_layout, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.list_item_recommended_job, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType() != VIEW_TYPE_LOADING) {
            if (position == 0) {
                holder.text_applicantName.setText(applicantName);
                SessionManagement sessionManagement = new SessionManagement(context);
                if (sessionManagement.getKeyUnreadNotifications().equals("0")){
                    holder.btnNotification.setImageResource(R.drawable.notification_all_read_ic);
                }else {
                    holder.btnNotification.setImageResource(R.drawable.notifications_ic);
                }

                holder.btnNotification.setOnClickListener(v -> callBack.onNotificationClicked());
                holder.btnMyJobs.setOnClickListener(v -> callBack.onMyJobsClicked());
                holder.btnSearch.setOnClickListener(v -> callBack.onSearchJobClicked());
                holder.btnFilterJobs.setOnClickListener(v -> callBack.onApplyFilterJobClicked());

                if (featuredJobsList.size() > 0) {
                    holder.recyclerview_featuredJobs.setVisibility(View.VISIBLE);
                    holder.layout_noFeaturedJobs.setVisibility(View.GONE);
                    showFeaturedJobsRecyclerView(holder.recyclerview_featuredJobs, featuredJobsList);
                } else {
                    holder.recyclerview_featuredJobs.setVisibility(View.GONE);
                    holder.layout_noFeaturedJobs.setVisibility(View.VISIBLE);
                }

                if (recommendedJobsList.size() > 0) {
                    holder.recyclerview_recommendedJobs.setVisibility(View.VISIBLE);
                    holder.layout_noRecommendedJobs.setVisibility(View.GONE);
                    showRecommendedJobsRecyclerView(holder.recyclerview_recommendedJobs, recommendedJobsList);
                } else {
                    holder.recyclerview_recommendedJobs.setVisibility(View.GONE);
                    holder.layout_noRecommendedJobs.setVisibility(View.VISIBLE);
                }


                if (latestJobsList.size() > 0) {
                    holder.layout_noLatestJobs.setVisibility(View.GONE);
                } else {
                    holder.layout_noLatestJobs.setVisibility(View.VISIBLE);
                }
            } else {
                ApplicantHomeJobDataModel latestJobModel = latestJobsList.get(position - 1);
                if (latestJobModel != null) {

                    holder.text_companyName.setText(latestJobModel.getCompanyDataModel().getName());
                    holder.text_matchPercentage.setVisibility(View.GONE);
                    Utilities.loadImage(context, latestJobModel.getCompanyDataModel().getProfile_image(), holder.jobImage);
                    holder.text_jobTitle.setText(latestJobModel.getJob_title());
                    String location = latestJobModel.getCity() + ", " + latestJobModel.getState();
                    holder.text_location.setText(location);

                    showJobTagsRecyclerview(holder.recyclerview_jobTags, latestJobModel);

                    holder.itemView.setOnClickListener(v -> callBack.onLatestJobItemClicked(position-1));
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        if (latestJobsList.size() == 0) {
            return 1;
        } else {
            return latestJobsList.size() + 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == latestJobsList.size() && isLoadingAdded) {
            return VIEW_TYPE_LOADING;
        } else if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onFeaturedJobClicked(int position) {
        callBack.onFeaturedJobClicked(featuredJobsAdapter, position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView jobImage;
        TextView text_jobTitle, text_companyName, text_location, text_matchPercentage;
        RecyclerView recyclerview_jobTags;

        // Header views
        RecyclerView recyclerview_featuredJobs, recyclerview_recommendedJobs;
        LinearLayout layout_noFeaturedJobs, layout_noRecommendedJobs, layout_noLatestJobs;
        ImageView btnNotification, btnFilterJobs;
        TextView text_applicantName, btnSearch, btnMyJobs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobImage = itemView.findViewById(R.id.jobImage);
            text_jobTitle = itemView.findViewById(R.id.text_jobTitle);
            text_companyName = itemView.findViewById(R.id.text_companyName);
            text_location = itemView.findViewById(R.id.text_location);
            text_matchPercentage = itemView.findViewById(R.id.text_matchPercentage);
            recyclerview_jobTags = itemView.findViewById(R.id.recyclerview_jobTags);

            // Header views
            btnNotification = itemView.findViewById(R.id.btnNotification);
            text_applicantName = itemView.findViewById(R.id.text_applicantName);
            btnMyJobs = itemView.findViewById(R.id.btnMyJobs);
            btnSearch = itemView.findViewById(R.id.btnSearch);
            btnFilterJobs = itemView.findViewById(R.id.btnFilterJobs);
            recyclerview_featuredJobs = itemView.findViewById(R.id.recyclerview_featuredJobs);
            layout_noFeaturedJobs = itemView.findViewById(R.id.layout_noFeaturedJobs);
            recyclerview_recommendedJobs = itemView.findViewById(R.id.recyclerview_recommendedJobs);
            layout_noRecommendedJobs = itemView.findViewById(R.id.layout_noRecommendedJobs);
            layout_noLatestJobs = itemView.findViewById(R.id.layout_noLatestJobs);
        }
    }

    public interface ApplicantHomeAdapterCallBack {
        void onLatestJobItemClicked(int position);
        void onMyJobsClicked();
        void onSearchJobClicked();
        void onApplyFilterJobClicked();
        void onNotificationClicked();
        void onFeaturedJobClicked(ApplicantFeaturedJobsAdapter featuredJobsAdapter, int position);
    }

    private void showFeaturedJobsRecyclerView(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> featuredJobsList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        featuredJobsAdapter = new ApplicantFeaturedJobsAdapter(context, featuredJobsList, this);
        recyclerView.setAdapter(featuredJobsAdapter);
    }

    private void showRecommendedJobsRecyclerView(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> recommendedJobsList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recommendedJobsAdapter = new ApplicantRecommendedJobsAdapter(context, recommendedJobsList);
        recyclerView.setAdapter(recommendedJobsAdapter);
    }

    private void showJobTagsRecyclerview(RecyclerView recyclerView, ApplicantHomeJobDataModel jobDataModel) {
        // set tags
        final List<TagModel> tags = new ArrayList<>();
        // salary range
        tags.add(new TagModel("$" + Utilities.getShortAmount(jobDataModel.getMin_salary()) + " - $" + Utilities.getShortAmount(jobDataModel.getMax_salary())));
        // location status
        tags.add(new TagModel(Utilities.getLocationStatus(jobDataModel.getLocation_status())));
        // Employment type
        tags.add(new TagModel(Utilities.getEmploymentType(jobDataModel.getEmployment_type())));

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        JobTagAdapter jobTagAdapter = new JobTagAdapter(context, tags);
        recyclerView.setAdapter(jobTagAdapter);
    }


}
