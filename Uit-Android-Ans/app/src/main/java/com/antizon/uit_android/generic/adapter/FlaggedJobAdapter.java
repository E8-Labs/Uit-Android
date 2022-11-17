package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelFlaggedJob;
import com.antizon.uit_android.utilities.MySpannable;
import com.antizon.uit_android.utilities.MySpanny;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class FlaggedJobAdapter extends RecyclerView.Adapter<FlaggedJobAdapter.MyViewHolder> {
    List<ModelFlaggedJob> list;
    Context context;

    public FlaggedJobAdapter(List<ModelFlaggedJob> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public FlaggedJobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flagged_user, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final FlaggedJobAdapter.MyViewHolder holder, final int position) {
        final ModelFlaggedJob dataModel = list.get(position);

        MySpanny spanny = new MySpanny("" , new MySpannable(false) {
            @Override
            public void onClick(View widget) {
            }
        });
        spanny.append(dataModel.getName()).append(" flagged the job \"")
                .append( dataModel.getJob_title(), new MySpannable(false) {
            @Override public void onClick(View widget) {
            }});
        spanny.append(" \"");

        holder.name.setText(spanny);
        Glide.with(context)
                .load(dataModel.getProfile_image()).circleCrop()
                .into(holder.profileImage);


    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<ModelFlaggedJob> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView profileImage;
        ConstraintLayout uit_admin_approved_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            profileImage = itemView.findViewById(R.id.profile_image);
            uit_admin_approved_layout = itemView.findViewById(R.id.uit_admin_approved_layout);
        }
    }

}