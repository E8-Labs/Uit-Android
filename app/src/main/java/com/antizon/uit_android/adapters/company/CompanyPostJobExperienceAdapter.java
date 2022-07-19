package com.antizon.uit_android.adapters.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.company.JobExperienceModel;
import java.util.List;

public class CompanyPostJobExperienceAdapter extends RecyclerView.Adapter<CompanyPostJobExperienceAdapter.ViewHolder> {

    List<JobExperienceModel> experienceList;
    Context context;
    CompanyPostJobExperienceAdapterCallBack callBack;
    String from;
    public CompanyPostJobExperienceAdapter(Context context, List<JobExperienceModel> experienceList, String from, CompanyPostJobExperienceAdapterCallBack callBack) {
        this.experienceList = experienceList;
        this.context = context;
        this.from = from;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_add_job_experience_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobExperienceModel experienceModel = experienceList.get(position);
        if (experienceModel !=null){
            String experienceName = experienceModel.getTotalExperience() + " years";

            holder.text_experience.setText(experienceName);
            holder.text_experience_industry.setText(experienceModel.getSelectedIndustryModel().getName());

            if (experienceModel.isRequired()){
                holder.text_experience_type.setText(context.getString(R.string.required));
            }else {
                holder.text_experience_type.setText(context.getString(R.string.preferred));
            }

            if (from.equals("add")){
                holder.text_remove.setVisibility(View.VISIBLE);
                holder.text_remove.setOnClickListener(view -> callBack.onRemoveClicked(position));
            }else {
                holder.text_remove.setVisibility(View.GONE);
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

        }
    }

    public interface CompanyPostJobExperienceAdapterCallBack {
        void onRemoveClicked(int position);
    }

}