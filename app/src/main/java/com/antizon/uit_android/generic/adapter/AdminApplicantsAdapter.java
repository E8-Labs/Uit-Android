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
import com.antizon.uit_android.generic.activities.GenericProfile;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.uit_admin.welcome.AdminMessage;
import com.antizon.uit_android.uit_admin.welcome.CompanyProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminApplicantsAdapter extends RecyclerView.Adapter<AdminApplicantsAdapter.MyViewHolder> {

    private static final String TAG = "PendingCompanies: AdminApplicantsAdapter";

    List<ModelAdminApplicants> list;
    Context context;
    ArrayList<ModelAdminApplicants> filterArrayList = new ArrayList<>();


    public AdminApplicantsAdapter(List<ModelAdminApplicants> list, Context context) {
        this.list = list;
        this.context = context;
        if (list != null) {
            filterArrayList.addAll(list);
        }

    }
    @NonNull
    @Override
    public AdminApplicantsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_applicants, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new AdminApplicantsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminApplicantsAdapter.MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final ModelAdminApplicants dataModel = list.get(getItemViewType(position));

        Log.d(TAG, "onBindViewHolder: list: " + list.size());
//        Log.d(TAG, "onBindViewHolder: active_members: " + dataModel.getActive_members());
        holder.noahNega.setText(dataModel.getName());
        holder.softwareDeveloper.setText(dataModel.getJob_title());
        holder.softwareDeveloper.setText("Android Developer");
        holder.employed.setText("Employed");
        holder.messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminMessage.class);

                context.startActivity(intent);

            }
        });

        Glide.with(context)
                .load(dataModel.getProfile_image())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_baseline_image_24))
                .into(holder.menYellow);
        holder.admin_applicant_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GenericProfile.class);
                intent.putExtra("data", dataModel);
                context.startActivity(intent);
                Log.d(TAG, "onClick: accept: ");

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

        TextView noahNega, softwareDeveloper, employed;
        ImageView messageIcon,menYellow;
        ConstraintLayout admin_applicant_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            noahNega = itemView.findViewById(R.id.noahNega);
            softwareDeveloper = itemView.findViewById(R.id.softwareDeveloper);
            employed = itemView.findViewById(R.id.employed);
            messageIcon = itemView.findViewById(R.id.messageIcon);
            menYellow = itemView.findViewById(R.id.menYellow);

            admin_applicant_layout = itemView.findViewById(R.id.admin_applicant_layout);
        }
    }

    public interface CompanyStatusInterface {
        public void accept(ModelAdminApplicants dataModel);

        public void decline(ModelAdminApplicants dataModel);
    }



    public void setFilterArrayListValue(List<ModelAdminApplicants> list) {

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
            for (ModelAdminApplicants dataModel : filterArrayList) {

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