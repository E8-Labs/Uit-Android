package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.company.invite.InviteMemberModel;

import java.util.List;

public class CompanyInviteAdapter extends RecyclerView.Adapter<CompanyInviteAdapter.ViewHolder> {

    Context context;
    List<InviteMemberModel> list;
    CompanyInviteAdapterCallBack callBack;

    public CompanyInviteAdapter(Context context, List<InviteMemberModel> list, CompanyInviteAdapterCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_company_invite_team_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InviteMemberModel inviteMemberModel = list.get(position);
        if (inviteMemberModel != null) {
            if (inviteMemberModel.getMemberName().length()>1){
                holder.text_firstLetter.setText(inviteMemberModel.getMemberName().substring(0,1));
            }else {
                holder.text_firstLetter.setText(inviteMemberModel.getMemberName());
            }
            holder.text_memberName.setText(inviteMemberModel.getMemberName());
            holder.text_memberEmail.setText(inviteMemberModel.getMemberEmail());

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
        TextView text_firstLetter, text_memberName, text_memberEmail, btnEdit;
        public ViewHolder(View itemView) {
            super(itemView);

            text_firstLetter = itemView.findViewById(R.id.text_firstLetter);
            text_memberName = itemView.findViewById(R.id.text_memberName);
            text_memberEmail = itemView.findViewById(R.id.text_memberEmail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public interface CompanyInviteAdapterCallBack {
        void onEditBtnClicked(int position);
    }

}