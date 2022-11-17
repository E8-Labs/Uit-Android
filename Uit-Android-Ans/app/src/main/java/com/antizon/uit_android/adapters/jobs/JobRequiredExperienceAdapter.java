package com.antizon.uit_android.adapters.jobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.jobs.JobExperienceDataModel;

import java.util.List;

public class JobRequiredExperienceAdapter  extends RecyclerView.Adapter<JobRequiredExperienceAdapter.ViewHolder> {

    List<JobExperienceDataModel> experienceList;
    Context context;

    public JobRequiredExperienceAdapter (Context context, List<JobExperienceDataModel> experienceList) {
        this.experienceList = experienceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_add_job_experience_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobExperienceDataModel experienceModel = experienceList.get(position);
        if (experienceModel !=null){
            String experienceName = experienceModel.getYears() + " years";

            holder.text_experience.setText(experienceName);
            holder.text_experience_industry.setText(experienceModel.getIndustry().getName());

            if (experienceModel.getRequirementStatus() == 1){
                holder.text_experience_type.setText(context.getString(R.string.required));
            }else {
                holder.text_experience_type.setText(context.getString(R.string.preferred));
            }

        }
    }

    @Override
    public int getItemCount() {
        return experienceList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_experience, text_experience_industry, text_experience_type, text_remove;

        public ViewHolder(View itemView) {
            super(itemView);

            text_experience = itemView.findViewById(R.id.text_experience);
            text_experience_industry = itemView.findViewById(R.id.text_experience_industry);
            text_experience_type = itemView.findViewById(R.id.text_experience_type);
            text_remove = itemView.findViewById(R.id.text_remove);


            //Not Needed for removing on job Detail
            text_remove.setVisibility(View.GONE);
        }
    }


}