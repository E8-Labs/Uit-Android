package com.antizon.uit_android.adapters.jobs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.AppliedJobApplicantDataModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;

public class CompanyAppliedJobApplicantAdapter extends RecyclerView.Adapter<CompanyAppliedJobApplicantAdapter.ViewHolder> {

    Context context;
    List<AppliedJobApplicantDataModel> appliedJobApplicantsList;
    CompanyAppliedJobApplicantAdapterCallBack callBack;

    public CompanyAppliedJobApplicantAdapter(Context context, List<AppliedJobApplicantDataModel> appliedJobApplicantsList, CompanyAppliedJobApplicantAdapterCallBack callBack) {
        this.context = context;
        this.appliedJobApplicantsList = appliedJobApplicantsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.single_item_listed_job_applicant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppliedJobApplicantDataModel hiredApplicant = appliedJobApplicantsList.get(position);
        if (hiredApplicant !=null){
            holder.text_applicationStatus.setText(Utilities.getAppliedJobStatus(hiredApplicant.getApplication_status()));
            GenericApplicantDataModel applicantDataModel = hiredApplicant.getGenericApplicantDataModel();
            if (applicantDataModel != null){
                Utilities.loadImage(context, applicantDataModel.getProfile_image(), holder.image_profile);
                holder.text_profileName.setText(applicantDataModel.getName());
                holder.text_jobTitle.setText(applicantDataModel.getJob_title());
                holder.btnMessage.setOnClickListener(v -> callBack.onMessageClicked(position));
                holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
            }

        }
    }

    @Override
    public int getItemCount() {
        return appliedJobApplicantsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<AppliedJobApplicantDataModel> filteredList) {
        appliedJobApplicantsList = filteredList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image_profile;
        TextView text_profileName, text_jobTitle, text_applicationStatus, btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_jobTitle = itemView.findViewById(R.id.text_jobTitle);
            text_applicationStatus = itemView.findViewById(R.id.text_applicationStatus);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }


    public interface CompanyAppliedJobApplicantAdapterCallBack{
        void onItemClick(int position);
        void onMessageClicked(int position);
    }
}
