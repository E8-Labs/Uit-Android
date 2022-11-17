package com.antizon.uit_android.generic.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ApplicantJobsAdapter extends RecyclerView.Adapter<ApplicantJobsAdapter.MyViewHolder> {

    private static final String TAG = ApplicantJobsAdapter.class.getSimpleName();
    List<ApplicantJobDataModel> list;
    Context context;
    ArrayList<ApplicantJobDataModel> filterArrayList = new ArrayList<>();

    public ApplicantJobsAdapter( Context context , List<ApplicantJobDataModel> list) {

        this.list = list;
        this.context = context;
        if (list != null) {
            filterArrayList.addAll(list);
        }
    }

    @NonNull
    @Override
    public ApplicantJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_jobs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicantJobsAdapter.MyViewHolder holder, final int position) {
        final ApplicantJobDataModel dataModel = list.get(getItemViewType(position));
        holder.applicantJobs.setText(dataModel.getName());
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
        TextView applicantJobs;
        ConstraintLayout applicantJobsLayout;
        public MyViewHolder(View itemView) {
            super(itemView);

            applicantJobs = itemView.findViewById(R.id.applicant_jobs);
            applicantJobsLayout = itemView.findViewById(R.id.applicant_jobs_layout);
        }
    }

    public void setFilterArrayListValue(List<ApplicantJobDataModel> list) {

        if (list != null){
            filterArrayList.clear();
            filterArrayList.addAll(list);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void search(String title) {

        Log.d(TAG, "searchFilter: searched Value is " + title);
        title = title.toLowerCase(Locale.getDefault());
        list.clear();
        if (title.length() == 0) {

            Log.d(TAG, "searchFilter: searched value is 0");
            list.addAll(filterArrayList);
        } else {


            Log.d(TAG, "searchFilter: Else Part");
            for (ApplicantJobDataModel dataModel : filterArrayList) {

                if (dataModel.getName() != null) {

                    if (dataModel.getName().toLowerCase(Locale.getDefault()).contains(title)) {

                        Log.d(TAG, "searchFilter: title " + title + " and adding to product list");
                        list.add(dataModel);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ApplicantJobDataModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}