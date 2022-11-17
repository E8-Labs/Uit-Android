package com.antizon.uit_android.adapters.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.CompanyInviteAdapter;
import com.antizon.uit_android.models.company.invite.InviteMemberModel;
import com.antizon.uit_android.models.invites.CompanyInvitedTeamMemberModel;

import java.util.List;

public class CompanyInvitedTeamMembersAdapter extends RecyclerView.Adapter<CompanyInvitedTeamMembersAdapter.ViewHolder> {

    Context context;
    List<CompanyInvitedTeamMemberModel> list;

    public CompanyInvitedTeamMembersAdapter(Context context, List<CompanyInvitedTeamMemberModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_company_invited_team_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyInvitedTeamMemberModel inviteMemberModel = list.get(position);
        if (inviteMemberModel != null) {
            if (inviteMemberModel.getName().length()>1){
                holder.text_firstLetter.setText(inviteMemberModel.getName().substring(0,1));
            }else {
                holder.text_firstLetter.setText(inviteMemberModel.getName());
            }
            holder.text_memberName.setText(inviteMemberModel.getName());
            holder.text_memberEmail.setText(inviteMemberModel.getEmail());

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
        TextView text_firstLetter, text_memberName, text_memberEmail, btnEdit;
        public ViewHolder(View itemView) {
            super(itemView);

            text_firstLetter = itemView.findViewById(R.id.text_firstLetter);
            text_memberName = itemView.findViewById(R.id.text_memberName);
            text_memberEmail = itemView.findViewById(R.id.text_memberEmail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }


}