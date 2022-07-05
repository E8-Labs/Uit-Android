package com.antizon.uit_android.generic.adapter;


import android.content.Context;
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
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.List;

public class SavedJobsAdapter extends RecyclerView.Adapter<SavedJobsAdapter.MyViewHolder> {

    private static final String TAG = "PendingCompanies: SavedJobsAdapter";

    List<ModelAllJobs> list;
    Context context;
    public SavedJobsAdapter(List<ModelAllJobs> list, Context context) {
        this.list = list;
        this.context = context;

    }
    @NonNull
    @Override
    public SavedJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_saved_job_list, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new SavedJobsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedJobsAdapter.MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final ModelAllJobs dataModel = list.get(getItemViewType(position));

        Log.d(TAG, "onBindViewHolder: list: " + list.size());
        Glide.with(context)
                .load(dataModel.getProfile_image())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                .into(holder.applicantImage);
        holder.uiDesign.setText(dataModel.getName());
        holder.google.setText(dataModel.getJob_title());
        holder.sanFrancisco.setText(dataModel.getCity()+", "+dataModel.getState());
        holder.fullTime.setText("");
        holder.dollar.setText("$"+dataModel.getMin_salary()+" - $"+dataModel.getMax_salary());
        holder.industry.setText(dataModel.getName());
        holder.match.setText("");

        holder.applicantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: accept: ");

            }
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView uiDesign, google, sanFrancisco,dollar,fullTime,industry,match;
        ImageView applicantImage;
        ConstraintLayout layoutApplicant;

        public MyViewHolder(View itemView) {
            super(itemView);

            uiDesign = itemView.findViewById(R.id.uiDesign);
            google = itemView.findViewById(R.id.google);
            sanFrancisco = itemView.findViewById(R.id.sanFrancisco);
            applicantImage = itemView.findViewById(R.id.applicantImage);
            dollar = itemView.findViewById(R.id.dollar);
            fullTime = itemView.findViewById(R.id.fullTime);
            industry = itemView.findViewById(R.id.industry);
            match = itemView.findViewById(R.id.match);

            layoutApplicant = itemView.findViewById(R.id.layoutApplicant);
        }
    }

//    public interface CompanyStatusInterface {
//        public void accept(ModelApplicantSavedJob dataModel);
//
//        public void decline(ModelApplicantSavedJob dataModel);
//    }


}