package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.work.ApplicantWorkExperienceModel;
import java.util.List;

public class ApplicantWorkExperienceAdapter extends RecyclerView.Adapter<ApplicantWorkExperienceAdapter.ViewHolder> {

    Context context;
    List<ApplicantWorkExperienceModel> list;
    ApplicantWorkExperienceAdapterCallBack callBack;

    public ApplicantWorkExperienceAdapter( Context context, List<ApplicantWorkExperienceModel> list, ApplicantWorkExperienceAdapterCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_work_experience, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        ApplicantWorkExperienceModel applicantWorkExperienceModel = list.get(position);

        if (applicantWorkExperienceModel !=null){
            String workingDate;
            if (applicantWorkExperienceModel.getEndDate() !=null){
                if (!applicantWorkExperienceModel.getEndDate().isEmpty()){
                    workingDate= applicantWorkExperienceModel.getStartDate() + " - " + applicantWorkExperienceModel.getEndDate();
                }else {
                    workingDate = applicantWorkExperienceModel.getStartDate()  + " - present";
                }
            }
            else {
                workingDate = applicantWorkExperienceModel.getStartDate()  + " - present";
            }

            holder.fieldOfStudy.setText(applicantWorkExperienceModel.getJobTitle());
            holder.universityName.setText(applicantWorkExperienceModel.getCompanyName());
            holder.profession.setText(applicantWorkExperienceModel.getResponsibilities());
            holder.deadline.setText(workingDate);

            holder.btnEdit.setOnClickListener(v -> callBack.onEditBtnClicked(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fieldOfStudy, universityName, profession, deadline, btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            fieldOfStudy = itemView.findViewById(R.id.fieldOfStudy);
            universityName = itemView.findViewById(R.id.universityName);
            profession = itemView.findViewById(R.id.profession);
            deadline = itemView.findViewById(R.id.deadline);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public interface ApplicantWorkExperienceAdapterCallBack {
        void onEditBtnClicked(int position);
    }

}