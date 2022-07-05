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

public class ApplicantProfessionalInterestAdapter extends RecyclerView.Adapter<ApplicantProfessionalInterestAdapter.MyViewHolder> {

    List<ModelCompanySize> list;
    Context context;
    ApplicantProfessionalInterestAdapter.SelectionListener selectionListener;

    public ApplicantProfessionalInterestAdapter(List<ModelCompanySize> list, Context context, SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public ApplicantProfessionalInterestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_department, parent, false);
        return new ApplicantProfessionalInterestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicantProfessionalInterestAdapter.MyViewHolder holder, final int position) {
        final ModelCompanySize companyModel = list.get(getItemViewType(position));

        holder.applicantDepartment.setText(companyModel.getName());

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

        TextView applicantDepartment;
        ImageView checked_ic;

        public MyViewHolder(View itemView) {
            super(itemView);

            applicantDepartment = itemView.findViewById(R.id.applicant_department);
            checked_ic = itemView.findViewById(R.id.checked_ic);
        }
    }

    public interface SelectionListener {
        void selectedCompanySize(boolean isChecked, int position);
    }
}