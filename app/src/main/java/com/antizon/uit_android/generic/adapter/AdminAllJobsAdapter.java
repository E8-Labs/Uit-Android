package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.activities.AdminJobDetailActivity;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import net.time4j.Moment;
import net.time4j.PrettyTime;
import net.time4j.format.expert.Iso8601Format;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class AdminAllJobsAdapter extends RecyclerView.Adapter<AdminAllJobsAdapter.MyViewHolder> {

    private static final String TAG = "PendingCompanies: AdminAllJobsAdapter";

    Context context;
    List<ModelAllJobs> list;
    AdminAllJobsAdapterCallBack callBack;

    public AdminAllJobsAdapter( Context context, List<ModelAllJobs> list, AdminAllJobsAdapterCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public AdminAllJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recruiter_listed_job, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminAllJobsAdapter.MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final ModelAllJobs dataModel = list.get(getItemViewType(position));

        holder.jobPostedByTitle.setText("By " + dataModel.getUserDataModel().getName());
        holder.postedByRole.setText("Role " + dataModel.getUserDataModel().getRole());


        if (dataModel.getCreated_at() !=null){
            Moment moment;
            try {
                moment = Iso8601Format.EXTENDED_DATE_TIME_OFFSET.parse(dataModel.getCreated_at());
                String ago = PrettyTime.of(Locale.getDefault()).printRelativeInStdTimezone(moment);
                ago = ago.replace("moments", "sec");
                ago = ago.replace("minutes", "min");
                ago = ago.replace("seconds", "sec");
                ago = ago.replace("hours", "hrs");
                ago = ago.replace("days", "d");
                holder.timeAgo.setText(ago);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        holder.jobTitle.setText(dataModel.getJob_title());
        holder.companyTitle.setText(dataModel.getCompanyDataModel().getName());
        holder.address.setText(dataModel.getCity() + ", " + dataModel.getState());

        holder.salary.setText("$" + prettyCount(dataModel.getMin_salary()) + " - $" + prettyCount(dataModel.getMin_salary()));

        showImage(holder.postedByImage, dataModel.getUserDataModel().getProfile_image());
        showImage(holder.companyLogo, dataModel.getCompanyDataModel().getProfile_image());

        setApplicants(holder, dataModel);

        holder.itemView.setOnClickListener(view -> {
            callBack.onItemClick(position);
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView jobPostedByTitle, postedByRole, timeAgo, address, salary, jobType, jobTitle, companyTitle, timeRange, totalApplicantsCount,
                hiredText, moreHiredCountTitle, feature, filled, closed;
        ImageView postedByImage, companyLogo, firstApplicant, secondApplicant, thirdApplicant, fourthApplicant, fifthApplicant, sixthApplicant, seventhApplicant, totalApplicantCount, hiredApplicant, moreHiredCount;
        ConstraintLayout applicantList, totalApplicantsLayout;

        public MyViewHolder(View itemView) {
            super(itemView);


            hiredText = itemView.findViewById(R.id.hiredText);
            moreHiredCountTitle = itemView.findViewById(R.id.moreHiredCountTitle);
            hiredApplicant = itemView.findViewById(R.id.hiredApplicant);
            moreHiredCount = itemView.findViewById(R.id.moreHiredCount);
            filled = itemView.findViewById(R.id.filled);
            closed = itemView.findViewById(R.id.closed);
            feature = itemView.findViewById(R.id.feature);

            timeAgo = itemView.findViewById(R.id.timeAgo);
            totalApplicantsCount = itemView.findViewById(R.id.totalApplicantsCount);
            postedByImage = itemView.findViewById(R.id.postedByImage);
            companyLogo = itemView.findViewById(R.id.companyLogo);

            firstApplicant = itemView.findViewById(R.id.firstApplicant);
            secondApplicant = itemView.findViewById(R.id.secondApplicant);
            thirdApplicant = itemView.findViewById(R.id.thirdApplicant);
            fourthApplicant = itemView.findViewById(R.id.fourthApplicant);
            fifthApplicant = itemView.findViewById(R.id.fifthApplicant);
            sixthApplicant = itemView.findViewById(R.id.sixthApplicant);
            seventhApplicant = itemView.findViewById(R.id.seventhApplicant);
            totalApplicantCount = itemView.findViewById(R.id.totalApplicantCount);

            jobPostedByTitle = itemView.findViewById(R.id.jobPostedByTitle);
            postedByRole = itemView.findViewById(R.id.postedByRole);

            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyTitle = itemView.findViewById(R.id.companyTitle);
            address = itemView.findViewById(R.id.address);

            salary = itemView.findViewById(R.id.salary);
            timeRange = itemView.findViewById(R.id.timeRange);
            jobType = itemView.findViewById(R.id.jobType);

            applicantList = itemView.findViewById(R.id.applicantList);
            totalApplicantsLayout = itemView.findViewById(R.id.totalApplicantsLayout);


        }
    }

    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    private void setApplicants(AdminAllJobsAdapter.MyViewHolder holder, ModelAllJobs dataModel) {

        if (dataModel.getSelectedJobStatus().equalsIgnoreCase("1")) {
            holder.totalApplicantsLayout.setVisibility(View.GONE);
            holder.applicantList.setVisibility(View.VISIBLE);
            holder.feature.setVisibility(View.VISIBLE);
            holder.filled.setVisibility(View.GONE);
            holder.closed.setVisibility(View.GONE);

            for (int i = 0; i < dataModel.getApplicationsList().size(); i++) {
                if (i == 0) {
                    showImage(holder.firstApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 1) {
                    showImage(holder.secondApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 2) {
                    showImage(holder.thirdApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 3) {
                    showImage(holder.fourthApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 4) {
                    showImage(holder.fifthApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 5) {
                    showImage(holder.sixthApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 6) {
                    showImage(holder.seventhApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 8) {
                    holder.totalApplicantsCount.setText("+3" + dataModel.getApplicationsList().size());
                }
            }

        } else if (dataModel.getSelectedJobStatus().equalsIgnoreCase("2")) {

            holder.totalApplicantsLayout.setVisibility(View.VISIBLE);
            holder.applicantList.setVisibility(View.GONE);
            holder.filled.setVisibility(View.VISIBLE);
            holder.closed.setVisibility(View.GONE);
            holder.feature.setVisibility(View.GONE);
            showImage(holder.hiredApplicant, dataModel.getUserDataModel().getProfile_image());
            holder.moreHiredCountTitle.setText("+3" + dataModel.getApplicationsList().size());

        } else if (dataModel.getSelectedJobStatus().equalsIgnoreCase("3")) {

            holder.totalApplicantsLayout.setVisibility(View.GONE);
            holder.applicantList.setVisibility(View.VISIBLE);
            holder.filled.setVisibility(View.GONE);
            holder.closed.setVisibility(View.VISIBLE);
            holder.feature.setVisibility(View.GONE);
            for (int i = 0; i < dataModel.getApplicationsList().size(); i++) {
                if (i == 0) {
                    showImage(holder.firstApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 1) {
                    showImage(holder.secondApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 2) {
                    showImage(holder.thirdApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 3) {
                    showImage(holder.fourthApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 4) {
                    showImage(holder.fifthApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 5) {
                    showImage(holder.sixthApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 6) {
                    showImage(holder.seventhApplicant, dataModel.getApplicationsList().get(i).getProfile_image());
                } else if (i == 8) {

                    holder.totalApplicantsCount.setText("+3" + dataModel.getApplicationsList().size());
                }
            }
        }
    }

    private void showImage(ImageView imageView, String imageUrl) {

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                .into(imageView);

    }

    public interface AdminAllJobsAdapterCallBack{
        void onItemClick(int position);
    }
}