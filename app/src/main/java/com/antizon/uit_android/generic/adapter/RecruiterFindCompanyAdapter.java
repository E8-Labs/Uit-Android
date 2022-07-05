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
import com.antizon.uit_android.generic.model.ModelFlaggedUser;
import com.antizon.uit_android.generic.model.ModelRecruiterFindCompany;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecruiterFindCompanyAdapter extends RecyclerView.Adapter<RecruiterFindCompanyAdapter.MyViewHolder> {

    private static final String TAG = RecruiterFindCompanyAdapter.class.getSimpleName();
    List<ModelRecruiterFindCompany> list;
    Context context;

    ArrayList<ModelRecruiterFindCompany> filterArrayList = new ArrayList<>();
    public RecruiterFindCompanyAdapter(List<ModelRecruiterFindCompany> list, Context context) {

        this.list = list;
        this.context = context;
        if (list != null) {
            filterArrayList.addAll(list);
        }
    }

    @NonNull
    @Override
    public RecruiterFindCompanyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_accepted, parent, false);
        return new RecruiterFindCompanyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecruiterFindCompanyAdapter.MyViewHolder holder, final int position) {
        final ModelRecruiterFindCompany dataModel = list.get(getItemViewType(position));

        holder.name.setText(dataModel.getName());
        holder.state.setText(dataModel.getEmail());
        holder.profile_image.setImageResource(R.drawable.kevin_valdez);
        holder.company_accepted_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + dataModel.getName(), Toast.LENGTH_SHORT).show();
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

        TextView name, state , accept;
        ImageView profile_image;
        ConstraintLayout company_accepted_layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            state = itemView.findViewById(R.id.state);
            accept = itemView.findViewById(R.id.accept);
            profile_image = itemView.findViewById(R.id.profile_image);
            company_accepted_layout = itemView.findViewById(R.id.company_accepted_layout);
        }
    }

    public void setFilterArrayListValue(List<ModelRecruiterFindCompany> list) {

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
            for (ModelRecruiterFindCompany dataModel : filterArrayList) {

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