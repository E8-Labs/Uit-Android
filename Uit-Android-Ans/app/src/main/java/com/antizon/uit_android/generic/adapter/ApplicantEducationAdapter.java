package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;

import java.util.ArrayList;

public class ApplicantEducationAdapter extends RecyclerView.Adapter<ApplicantEducationAdapter.MyViewHolder> {

    Context context;
    ArrayList<ApplicantEducationDataModel> list;
    EducationAdapterCallBack callBack;

    public ApplicantEducationAdapter(Context context, ArrayList<ApplicantEducationDataModel> list, EducationAdapterCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ApplicantEducationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_education, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicantEducationAdapter.MyViewHolder holder, final int position) {
        ApplicantEducationDataModel applicantEducationDataModel = list.get(getItemViewType(position));
        if (applicantEducationDataModel !=null){

            String date;
            if (applicantEducationDataModel.getEndDate() !=null){
                if (!applicantEducationDataModel.getEndDate().isEmpty()){
                    date = applicantEducationDataModel.getStartDate() + " - " + applicantEducationDataModel.getEndDate();
                }else {
                    date =  applicantEducationDataModel.getStartDate();
                }
            }else {
                date =  applicantEducationDataModel.getStartDate();
            }

            holder.fieldOfStudy.setText(applicantEducationDataModel.getFieldOfStudy());
            holder.universityName.setText(applicantEducationDataModel.getSchoolName());
            holder.profession.setText(applicantEducationDataModel.getApplicantDegreeData().getName());
            holder.deadline.setText(date);

            holder.btnEdit.setOnClickListener(v -> callBack.onEditClicked(position));
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

    public interface EducationAdapterCallBack {
        void onEditClicked(int position);
    }

}