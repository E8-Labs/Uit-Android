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
import com.antizon.uit_android.models.applicant.home.GenericUserDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class AdminAllCompanyMembersAdapter extends RecyclerView.Adapter<AdminAllCompanyMembersAdapter.ViewHolder> {

    Context context;
    List<GenericApplicantDataModel> applicantsList;
    AdminAllCompanyMembersAdapterCallback callBack;

    public AdminAllCompanyMembersAdapter(Context context, List<GenericApplicantDataModel> applicantsList, AdminAllCompanyMembersAdapterCallback callBack) {
        this.context = context;
        this.applicantsList = applicantsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_admin_company_member, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericApplicantDataModel applicantDataModel = applicantsList.get(position);
        if (applicantDataModel !=null){

            Utilities.loadImage(context, applicantDataModel.getProfile_image(), holder.user_ProfileImage);
            holder.text_profileName.setText(applicantDataModel.getName());
            holder.text_jobTitle.setText(applicantDataModel.getJob_title());

            GenericUserDataModel companyDataModel = applicantDataModel.getParentDataModel();
            if ( companyDataModel!=null){
                Utilities.loadImage(context, companyDataModel.getProfile_image(), holder.company_profileImage);
                holder.text_companyName.setText(companyDataModel.getName());
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
        RoundedImageView user_ProfileImage, company_profileImage;
        TextView text_profileName, text_jobTitle, text_companyName;

        RelativeLayout btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_ProfileImage = itemView.findViewById(R.id.user_ProfileImage);
            company_profileImage = itemView.findViewById(R.id.company_profileImage);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_jobTitle = itemView.findViewById(R.id.text_jobTitle);
            text_companyName = itemView.findViewById(R.id.text_companyName);
            btnMessage = itemView.findViewById(R.id.btnMessage);

        }
    }


    public interface AdminAllCompanyMembersAdapterCallback{
        void onItemClick(int position);
        void onMessageClick(int position);
    }
}
