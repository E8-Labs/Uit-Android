package com.antizon.uit_android.adapters.jobs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.utilities.Utilities;

import java.util.List;

public class JobPeopleAdapter extends RecyclerView.Adapter<JobPeopleAdapter.ViewHolder> {

    Context context;
    List<GenericApplicantDataModel> list;
    JobPeopleAdapterCallBack callBack;

    public JobPeopleAdapter(Context context, List<GenericApplicantDataModel> list, JobPeopleAdapterCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.design_job_people, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 6) {
            final GenericApplicantDataModel current = list.get(position);
            if (current == null) return;

            // show profile
            holder.ivProfile.setVisibility(View.VISIBLE);
            holder.tvNumber.setVisibility(View.GONE);
            Utilities.loadImage(context, current.getProfile_image(), holder.ivProfile);

            holder.itemView.setOnClickListener(v -> callBack.onJobPeopleItemClicked(position));
        }

        // show number
        else {
            holder.ivProfile.setVisibility(View.GONE);
            holder.tvNumber.setVisibility(View.VISIBLE);
            holder.tvNumber.setText("+" + (list.size() - 6));
        }


    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return Math.min(list.size(), 7);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

    public interface JobPeopleAdapterCallBack{
        void onJobPeopleItemClicked(int position);
    }
}
