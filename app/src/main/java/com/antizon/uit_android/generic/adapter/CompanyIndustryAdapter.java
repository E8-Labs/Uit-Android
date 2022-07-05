package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompanyIndustryAdapter extends RecyclerView.Adapter<CompanyIndustryAdapter.MyViewHolder> {

    private static final String TAG = CompanyIndustryAdapter.class.getSimpleName();
    List<ModelCompanySize> list;
    Context context;
    SelectionListener selectionListener;
    ArrayList<ModelCompanySize> filterArrayList = new ArrayList<>();

    public CompanyIndustryAdapter(List<ModelCompanySize> list, Context context, SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;

        if (list != null) {
            filterArrayList.addAll(list);
        }
    }

    @NonNull
    @Override
    public CompanyIndustryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_industry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyIndustryAdapter.MyViewHolder holder, final int position) {
        final ModelCompanySize dataModel = list.get(getItemViewType(position));

        holder.companyIndustry.setText(dataModel.getName());

        holder.companyIndustryLayout.setOnClickListener(view -> {

            if (!dataModel.isSelected()) {
                holder.companyIndustryLayout.setBackgroundColor(context.getColor(R.color.app_color));
                holder.companyIndustry.setTextColor(context.getColor(R.color.white));
                dataModel.setSelected(true);
            } else {
                holder.companyIndustryLayout.setBackgroundColor(context.getColor(R.color.grayCircle));
                holder.companyIndustry.setTextColor(context.getColor(R.color.black));
                dataModel.setSelected(false);
            }
//                selectionListener.selectedCompanyIndustry(position);
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

        TextView companyIndustry;
        ConstraintLayout companyIndustryLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            companyIndustry = itemView.findViewById(R.id.company_industry);
            companyIndustryLayout = itemView.findViewById(R.id.company_industry_layout);
        }
    }

    public interface SelectionListener {
        void selectedCompanyIndustry(int modelcompanyIndustry);
    }


    public void setFilterArrayListValue(List<ModelCompanySize> list) {

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
            for (ModelCompanySize dataModel : filterArrayList) {

                if (dataModel.getName() != null) {

                    if (dataModel.getName().toLowerCase(Locale.getDefault()).contains(title)) {

                        Log.d(TAG, "searchFilter: title " + title + " and adding to product list");
                        list.add(dataModel);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

}