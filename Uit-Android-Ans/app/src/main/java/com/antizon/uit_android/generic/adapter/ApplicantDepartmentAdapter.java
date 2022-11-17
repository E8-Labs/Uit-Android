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
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import java.util.List;

public class ApplicantDepartmentAdapter extends RecyclerView.Adapter<ApplicantDepartmentAdapter.ViewHolder> {

    List<ApplicantDepartmentDataModel> list;
    Context context;
    ApplicantDepartmentAdapterCallBack callBack;

    public ApplicantDepartmentAdapter(Context context, List<ApplicantDepartmentDataModel> list, ApplicantDepartmentAdapterCallBack callBack) {

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
        ApplicantDepartmentDataModel departmentDataModel = list.get(position);

        holder.applicantDepartment.setText(departmentDataModel.getName());

        if (departmentDataModel.isSelected()){
            holder.checked_ic.setImageResource(R.drawable.checked_ic);
        }else {
            holder.checked_ic.setImageResource(R.drawable.not_checked_ic);
        }

        holder.itemView.setOnClickListener(view -> callBack.selectedApplicantDepartment(departmentDataModel.isSelected(), position));
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

    public interface ApplicantDepartmentAdapterCallBack {
        void selectedApplicantDepartment(boolean isChecked, int position);
    }
}