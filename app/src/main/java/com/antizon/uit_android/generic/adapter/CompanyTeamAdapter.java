package com.antizon.uit_android.generic.adapter;


import android.content.Context;
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
import com.antizon.uit_android.generic.model.ModelCompanyTeam;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;

public class CompanyTeamAdapter extends RecyclerView.Adapter<CompanyTeamAdapter.MyViewHolder> {

    private static final String TAG = "PendingCompanies: CompanyTeamAdapter";

    List<ModelCompanyTeam> list;
    Context context;
    CompanyStatusInterface companyStatusInterface;

    public CompanyTeamAdapter(List<ModelCompanyTeam> list, Context context) {
        this.list = list;
        this.context = context;
        this.companyStatusInterface = companyStatusInterface;
    }

    @NonNull
    @Override
    public CompanyTeamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_accepted, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new CompanyTeamAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyTeamAdapter.MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final ModelCompanyTeam dataModel = list.get(getItemViewType(position));

        Log.d(TAG, "onBindViewHolder: list: " + list.size());
        Log.d(TAG, "onBindViewHolder: active_members: " + dataModel.getActive_members());
        holder.name.setText(dataModel.getName());
        holder.state.setText(dataModel.getCity() + ", " + dataModel.getState());
        Glide.with(context)
                .load(dataModel.getProfile_image())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                .into(holder.profileImage);

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: accept: ");
                companyStatusInterface.accept(dataModel);
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: decline: ");
                companyStatusInterface.decline(dataModel);
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

        TextView name, state, accept, decline;
        ImageView profileImage;
        ConstraintLayout company_accepted_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            state = itemView.findViewById(R.id.state);
            name = itemView.findViewById(R.id.name);
            profileImage = itemView.findViewById(R.id.profile_image);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);

            company_accepted_layout = itemView.findViewById(R.id.company_accepted_layout);
        }
    }

    public interface CompanyStatusInterface {
        public void accept(ModelCompanyTeam dataModel);

        public void decline(ModelCompanyTeam dataModel);
    }


}