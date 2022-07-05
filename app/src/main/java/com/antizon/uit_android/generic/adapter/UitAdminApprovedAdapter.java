package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_admin.welcome.AdminMessage;
import com.antizon.uit_android.uit_admin.welcome.CompanyProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;

public class UitAdminApprovedAdapter extends RecyclerView.Adapter<UitAdminApprovedAdapter.MyViewHolder> {

    private static final String TAG = UitAdminApprovedAdapter.class.getSimpleName();
    List<ModelUitAdminApproved> list;
    Context context;
    SessionManagement sessionManagement;
    public UitAdminApprovedAdapter(List<ModelUitAdminApproved> list, Context context) {
        this.list = list;
        this.context = context;
        sessionManagement = new SessionManagement(context);
    }

    @NonNull
    @Override
    public UitAdminApprovedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approved, parent, false);
        return new UitAdminApprovedAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final UitAdminApprovedAdapter.MyViewHolder holder, final int position) {
        final ModelUitAdminApproved dataModel = list.get(getItemViewType(position));

        Log.d(TAG, "onBindViewHolder: active_members: " + dataModel.getActive_members());
        holder.name.setText(dataModel.getName());
        holder.state.setText(dataModel.getCity() + ", " + dataModel.getState());
        Glide.with(context)
                .load(dataModel.getProfile_image())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                .into(holder.profileImage);
        holder.active_members.setText(dataModel.getActive_members() + " active members");

        holder.messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminMessage.class);

                context.startActivity(intent);

            }
        });

        holder.uit_admin_approved_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: accept: ");

                    Intent intent = new Intent(context, CompanyProfile.class);
                intent.putExtra("dataModel",dataModel);
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, state, active_members;
        ImageView profileImage,messageIcon;
        ConstraintLayout uit_admin_approved_layout;

        public MyViewHolder(View itemView) {
            super(itemView);


            messageIcon = itemView.findViewById(R.id.messageIcon);
            state = itemView.findViewById(R.id.city);
            name = itemView.findViewById(R.id.name);
            profileImage = itemView.findViewById(R.id.profile_image);
            active_members = itemView.findViewById(R.id.active_members);
            uit_admin_approved_layout = itemView.findViewById(R.id.uit_admin_approved_layout);
        }
    }

}