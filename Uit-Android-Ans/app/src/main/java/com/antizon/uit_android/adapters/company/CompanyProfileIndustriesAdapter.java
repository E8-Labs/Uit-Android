package com.antizon.uit_android.adapters.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.profile.NameIdModel;

import java.util.List;

public class CompanyProfileIndustriesAdapter extends RecyclerView.Adapter<CompanyProfileIndustriesAdapter.ViewHolder> {

    Context context;
    List<NameIdModel> industriesList;


    public CompanyProfileIndustriesAdapter(Context context, List<NameIdModel> industriesList) {
        this.context = context;
        this.industriesList = industriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_profile_industry_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NameIdModel industry = industriesList.get(position);
        if (industry !=null){
            if (position == 0){
                holder.circle.setVisibility(View.GONE);
            }else {
                holder.circle.setVisibility(View.VISIBLE);
            }
            holder.text_industryName.setText(industry.getName());

        }
    }

    @Override
    public int getItemCount() {
        return industriesList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_industryName;
        View circle;
        public ViewHolder(View itemView) {
            super(itemView);
            text_industryName = itemView.findViewById(R.id.text_industryName);
            circle = itemView.findViewById(R.id.circle);

        }
    }

}