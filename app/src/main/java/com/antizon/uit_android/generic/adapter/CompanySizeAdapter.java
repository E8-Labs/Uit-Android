package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelCompanySize;

import java.util.List;

public class CompanySizeAdapter extends RecyclerView.Adapter<CompanySizeAdapter.MyViewHolder> {

    private static final String TAG = CompanySizeAdapter.class.getSimpleName();
    List<ModelCompanySize> list;
    Context context;

   SelectionListener selectionListener;

    public CompanySizeAdapter(List<ModelCompanySize> list, Context context, SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public CompanySizeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_size, parent, false);
        return new CompanySizeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanySizeAdapter.MyViewHolder holder, int position) {
        final ModelCompanySize dataModel = list.get(getItemViewType(position));

        holder.companySize.setText(dataModel.getName());

//
//        if (dataModel.getSelected()) {
//            holder.companySize.setBackground(context.getResources().getDrawable(R.drawable.app_color_curved_background));
//            holder.companySize.setTextColor(context.getResources().getColor(R.color.white));
//            holder.companySize.setPadding(20, 40, 20, 40);
//        } else {
//            holder.companySize.setBackground(context.getResources().getDrawable(R.drawable.industry_borders));
//            holder.companySize.setTextColor(context.getResources().getColor(R.color.black));
//            holder.companySize.setPadding(20, 40, 20, 40);
//        }
        holder.asian_checkbox.setOnClickListener(view -> {
//                holder.companySize.setBackground(context.getResources().getDrawable(R.drawable.app_color_curved_background));
            holder.companySize.setTextColor(context.getResources().getColor(R.color.black));
//                holder.companySize.setPadding(20, 40, 20, 40);
            selectionListener.selectedCompanySize(holder.asian_checkbox.isChecked(), position);
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

        TextView companySize;
        ConstraintLayout companySizeLayout;
        CheckBox asian_checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);

            companySize = itemView.findViewById(R.id.company_size);
            companySizeLayout = itemView.findViewById(R.id.company_size_layout);
            asian_checkbox = itemView.findViewById(R.id.asian_checkbox);

        }
    }
    public interface SelectionListener {
        void selectedCompanySize(Boolean isChecked, int position);
    }
}
