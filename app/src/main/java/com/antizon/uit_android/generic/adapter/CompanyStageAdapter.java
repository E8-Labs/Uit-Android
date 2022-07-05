package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelCompanySize;

import java.util.List;

public class CompanyStageAdapter extends RecyclerView.Adapter<CompanyStageAdapter.ViewHolder> {

    List<ModelCompanySize> list;
    Context context;
    CompanyStageAdapterCallBack callBack;

    public CompanyStageAdapter(Context context, List<ModelCompanySize> list, CompanyStageAdapterCallBack callBack) {

        this.list = list;
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCompanySize companySize = list.get(getItemViewType(position));

        holder.applicantDepartment.setText(companySize.getName());

        if (companySize.isSelected()){
            holder.checked_ic.setImageResource(R.drawable.checked_ic);
        }else {
            holder.checked_ic.setImageResource(R.drawable.not_checked_ic);
        }

        holder.itemView.setOnClickListener(view -> callBack.onOnSelectCompanySizeListener(companySize.isSelected(), position));

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView applicantDepartment;
        ImageView checked_ic;
        public ViewHolder(View itemView) {
            super(itemView);
            applicantDepartment = itemView.findViewById(R.id.applicant_department);
            checked_ic = itemView.findViewById(R.id.checked_ic);
        }
    }

    public interface CompanyStageAdapterCallBack {
        void onOnSelectCompanySizeListener(boolean isChecked, int position);
    }

}
