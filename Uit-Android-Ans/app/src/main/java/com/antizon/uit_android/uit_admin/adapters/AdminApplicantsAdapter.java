package com.antizon.uit_android.uit_admin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;

public class AdminApplicantsAdapter extends RecyclerView.Adapter<AdminApplicantsAdapter.ViewHolder> {
  
    Context context;
    List<GenericApplicantDataModel> applicantsList;
    AdminApplicantsAdapterCallback callBack;

    public AdminApplicantsAdapter(Context context, List<GenericApplicantDataModel> applicantsList, AdminApplicantsAdapterCallback callBack) {
        this.context = context;
        this.applicantsList = applicantsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_admin_applicant, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericApplicantDataModel applicantDataModel = applicantsList.get(position);
        if (applicantDataModel !=null){
            Utilities.loadImage(context, applicantDataModel.getProfile_image(), holder.image_profile);
            holder.text_profileName.setText(applicantDataModel.getName());
            holder.text_location.setText(applicantDataModel.getEmail());

            if (applicantDataModel.getProfessional_info() !=null){
                if (applicantDataModel.getProfessional_info().getEmployment_status() == 0){
                    holder.text_employmentStatus.setText(context.getString(R.string.text_notEmployed));
                }else if (applicantDataModel.getProfessional_info().getEmployment_status() == 1){
                    holder.text_employmentStatus.setText(context.getString(R.string.text_employedButLooking));
                }else{
                    holder.text_employmentStatus.setText(context.getString(R.string.text_employedButNotLooking));
                }
            }else {
                holder.text_employmentStatus.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
            holder.btnMessage.setOnClickListener(v -> callBack.onMessageClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return applicantsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<GenericApplicantDataModel> filteredList) {
        applicantsList = filteredList;
        notifyDataSetChanged();
    }
    

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image_profile;
        TextView text_profileName, text_location, text_employmentStatus;

        RelativeLayout btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_location = itemView.findViewById(R.id.text_location);
            text_employmentStatus = itemView.findViewById(R.id.text_employmentStatus);
            btnMessage = itemView.findViewById(R.id.btnMessage);

        }
    }


    public interface AdminApplicantsAdapterCallback{
        void onItemClick(int position);
        void onMessageClick(int position);
    }
}
