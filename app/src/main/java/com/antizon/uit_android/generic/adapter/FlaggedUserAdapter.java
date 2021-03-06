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
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelFlaggedUser;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_admin.welcome.CompanyProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FlaggedUserAdapter extends RecyclerView.Adapter<FlaggedUserAdapter.MyViewHolder> {

    private static final String TAG = FlaggedUserAdapter.class.getSimpleName();
    List<ModelFlaggedUser> list;
    Context context;
    ArrayList<ModelFlaggedUser> filterArrayList = new ArrayList<>();

    public FlaggedUserAdapter(List<ModelFlaggedUser> list, Context context) {
        this.list = list;
        this.context = context;
        if (list != null) {
            filterArrayList.addAll(list);
        }
    }

    @NonNull
    @Override
    public FlaggedUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flagged_user, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final FlaggedUserAdapter.MyViewHolder holder, final int position) {
        final ModelFlaggedUser dataModel = list.get(getItemViewType(position));

        Log.d(TAG, "onBindViewHolder: name: " + dataModel.getName());
        holder.name.setText(dataModel.getName());
        holder.city.setText(dataModel.getJob_title());
        holder.city.setText("Android Developer");

        Glide.with(context)
                .load(dataModel.getProfile_image())
                .circleCrop()
                .into(holder.profileImage);
//        holder.active_members.setText(dataModel.getActive_members() + " active members");

        holder.uit_admin_approved_layout.setOnClickListener(view -> {

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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

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

    public void setFilterArrayListValue(List<ModelFlaggedUser> list) {

        if (list != null)
            filterArrayList.clear();
        filterArrayList.addAll(list);
    }
    public void search(String title) {

        Log.d(TAG, "searchFilter: searched Value is " + title);
        title = title.toLowerCase(Locale.getDefault());
        list.clear();
        if (title.length() == 0) {

            Log.d(TAG, "searchFilter: searched value is 0");
            list.addAll(filterArrayList);
        } else {


            Log.d(TAG, "searchFilter: Else Part");
            for (ModelFlaggedUser dataModel : filterArrayList) {

                if (dataModel.getName() != null) {

                    if (dataModel.getName().toLowerCase(Locale.getDefault()).contains(title) ||
                            dataModel.getName().toLowerCase(Locale.getDefault()).contains(title)) {

                        Log.d(TAG, "searchFilter: title " + title + " and adding to product list");
                        list.add(dataModel);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

}