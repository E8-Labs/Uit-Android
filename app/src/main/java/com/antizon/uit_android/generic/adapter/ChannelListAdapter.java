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
import com.antizon.uit_android.generic.model.ModelChannelList;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_admin.welcome.CompanyProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.MyViewHolder> {

    private static final String TAG = ChannelListAdapter.class.getSimpleName();
    List<ModelChannelList> list;
    Context context;
    public ChannelListAdapter(List<ModelChannelList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChannelListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flagged_user, parent, false);
        return new ChannelListAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ChannelListAdapter.MyViewHolder holder, final int position) {
        final ModelChannelList dataModel = list.get(getItemViewType(position));

        Log.d(TAG, "onBindViewHolder: name: " + dataModel.getName());
        holder.name.setText(dataModel.getName());



        Glide.with(context)
                .load(dataModel.getImage_path())
                .circleCrop()
                .into(holder.profileImage);

//        holder.active_members.setText(dataModel.getActive_members() + " active members");

        holder.uit_admin_approved_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        TextView name, city;
        ImageView profileImage;
        ConstraintLayout uit_admin_approved_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.city);
            name = itemView.findViewById(R.id.name);
            profileImage = itemView.findViewById(R.id.profile_image);
            uit_admin_approved_layout = itemView.findViewById(R.id.uit_admin_approved_layout);
        }
    }

}