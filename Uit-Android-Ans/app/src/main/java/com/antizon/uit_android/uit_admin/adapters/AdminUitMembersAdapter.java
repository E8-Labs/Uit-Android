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

public class AdminUitMembersAdapter extends RecyclerView.Adapter<AdminUitMembersAdapter.ViewHolder> {
    public static int TYPE_VIEW_PENDING = 1, TYPE_VIEW_APPROVED = 2;

    Context context;
    List<GenericApplicantDataModel> uitMembersList;
    int accountStatus;
    AdminUitMembersAdapterCallBack callBack;

    public AdminUitMembersAdapter(Context context, List<GenericApplicantDataModel> uitMembersList, int accountStatus, AdminUitMembersAdapterCallBack callBack) {
        this.context = context;
        this.uitMembersList = uitMembersList;
        this.accountStatus = accountStatus;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_VIEW_PENDING){
            view = LayoutInflater.from(context).inflate(R.layout.single_item_pending_company, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.single_item_approved_company, parent, false);
        }
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdminUitMembersAdapter.ViewHolder holder, int position) {
        GenericApplicantDataModel uitMemberModel = uitMembersList.get(position);
        if (uitMemberModel !=null){

            Utilities.loadImage(context, uitMemberModel.getProfile_image(), holder.image_profile);
            holder.text_profileName.setText(uitMemberModel.getName());
            holder.text_location.setText(uitMemberModel.getEmail());

            if (holder.getItemViewType() == TYPE_VIEW_PENDING){
                holder.btnAccept.setOnClickListener(v -> callBack.onAcceptClicked(position));
                holder.btnDecline.setOnClickListener(v -> callBack.onDeclineClicked(position));
            }else {
                holder.text_activeMember.setVisibility(View.GONE);
                holder.btnMessage.setOnClickListener(v -> callBack.onMessageClicked(position));
            }

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return uitMembersList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<GenericApplicantDataModel> filteredList) {
        uitMembersList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (accountStatus == 4){
            return TYPE_VIEW_PENDING;
        }else {
            return TYPE_VIEW_APPROVED;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image_profile;
        TextView text_profileName, text_location, text_activeMember, btnAccept, btnDecline;

        RelativeLayout btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_location = itemView.findViewById(R.id.text_location);

            text_activeMember = itemView.findViewById(R.id.text_activeMember);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnMessage = itemView.findViewById(R.id.btnMessage);

        }
    }


    public interface AdminUitMembersAdapterCallBack{
        void onItemClick(int position);
        void onAcceptClicked(int position);
        void onDeclineClicked(int position);
        void onMessageClicked(int position);
    }
}
