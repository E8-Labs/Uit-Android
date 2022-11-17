package com.antizon.uit_android.adapters.applicant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.profile.ApplicantProfileEducationDataModel;
import java.util.List;

public class ApplicantProfessionalEducationAdapter extends RecyclerView.Adapter<ApplicantProfessionalEducationAdapter.MyViewHolder> {

    Context context;
    List<ApplicantProfileEducationDataModel> list;

    public ApplicantProfessionalEducationAdapter(Context context, List<ApplicantProfileEducationDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_profile_education, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ApplicantProfileEducationDataModel applicantEducationDataModel = list.get(getItemViewType(position));
        if (applicantEducationDataModel !=null){

            String date;

            if (applicantEducationDataModel.getEnd_date() !=null){
                if (!applicantEducationDataModel.getEnd_date().isEmpty()){
                    date = applicantEducationDataModel.getStart_date() + " - " + applicantEducationDataModel.getEnd_date();
                }else {
                    date =  applicantEducationDataModel.getStart_date();
                }
            }else {
                date =  applicantEducationDataModel.getStart_date();
            }

            holder.deadline.setText(date);
            holder.fieldOfStudy.setText(applicantEducationDataModel.getField());
            holder.universityName.setText(applicantEducationDataModel.getSchool_name());
            holder.profession.setText(applicantEducationDataModel.getDegree().getName());
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


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fieldOfStudy, universityName, profession, deadline,btnEdit;
        public MyViewHolder(View itemView) {
            super(itemView);

            fieldOfStudy = itemView.findViewById(R.id.fieldOfStudy);
            universityName = itemView.findViewById(R.id.universityName);
            profession = itemView.findViewById(R.id.profession);
            deadline = itemView.findViewById(R.id.deadline);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

}