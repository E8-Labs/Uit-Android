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
import com.antizon.uit_android.generic.model.ModelApplicantRace;

import java.util.List;

public class ApplicantRaceAdapter extends RecyclerView.Adapter<ApplicantRaceAdapter.MyViewHolder> {

    List<ModelApplicantRace> list;
    Context context;
    SelectionListener selectionListener;

    public ApplicantRaceAdapter(List<ModelApplicantRace> list, Context context, SelectionListener selectionListener) {
        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public ApplicantRaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_race, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicantRaceAdapter.MyViewHolder holder, int position) {
        final ModelApplicantRace dataModel = list.get(getItemViewType(position));

        holder.companySize.setText(dataModel.getName());

        holder.asian_checkbox.setChecked(dataModel.isSelected());

        holder.asian_checkbox.setOnClickListener(view -> {
            holder.companySize.setTextColor(context.getResources().getColor(R.color.black));
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

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
        void selectedCompanySize(boolean isChecked, int position);
    }
}
