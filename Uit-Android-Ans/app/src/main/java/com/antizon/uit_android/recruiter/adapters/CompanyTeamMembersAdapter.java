package com.antizon.uit_android.recruiter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class CompanyTeamMembersAdapter extends RecyclerView.Adapter<CompanyTeamMembersAdapter.ViewHolder> {
    public static int TYPE_VIEW_ACCEPTED = 1, TYPE_VIEW_PENDING = 2;

    Context context;
    List<GenericApplicantDataModel> companyTeamMembersList;
    String type;
    CompanyTeamMembersAdapterCallBack callBack;

    public CompanyTeamMembersAdapter(Context context, List<GenericApplicantDataModel> companyTeamMembersList, String type, CompanyTeamMembersAdapterCallBack callBack) {
        this.context = context;
        this.type = type;
        this.companyTeamMembersList = companyTeamMembersList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_VIEW_PENDING){
            view = LayoutInflater.from(context).inflate(R.layout.single_item_pending_company, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.single_item_company_approved_team_member, parent, false);
        }
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericApplicantDataModel uitMemberModel = companyTeamMembersList.get(position);
        if (uitMemberModel !=null){
            Utilities.loadImage(context, uitMemberModel.getProfile_image(), holder.image_profile);
            holder.text_profileName.setText(uitMemberModel.getName());
            holder.text_location.setText(uitMemberModel.getJob_title());

            if (holder.getItemViewType() == TYPE_VIEW_PENDING){
                holder.btnAccept.setOnClickListener(v -> callBack.onAcceptClicked(position));
                holder.btnDecline.setOnClickListener(v -> callBack.onDeclineClicked(position));
            }else {
                holder.btnMessage.setOnClickListener(v -> callBack.onMessageClicked(position));
            }

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return companyTeamMembersList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<GenericApplicantDataModel> filteredList) {
        companyTeamMembersList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (type.equals("pending")){
            return TYPE_VIEW_PENDING;
        }else {
            return TYPE_VIEW_ACCEPTED;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image_profile;
        TextView text_profileName, text_location, btnMessage, btnAccept, btnDecline;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_location = itemView.findViewById(R.id.text_location);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);

        }
    }


    public interface CompanyTeamMembersAdapterCallBack{
        void onItemClick(int position);
        void onMessageClicked(int position);
        void onAcceptClicked(int position);
        void onDeclineClicked(int position);
    }
}
