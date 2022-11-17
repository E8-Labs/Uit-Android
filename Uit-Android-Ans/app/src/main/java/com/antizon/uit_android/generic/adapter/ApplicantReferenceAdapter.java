package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.work.ApplicantReferenceModel;

import java.util.List;

public class ApplicantReferenceAdapter extends RecyclerView.Adapter<ApplicantReferenceAdapter.ViewHolder> {

    List<ApplicantReferenceModel> referencesList;
    Context context;
    ApplicantReferenceAdapterCallBack callBack;

    public ApplicantReferenceAdapter(Context context, List<ApplicantReferenceModel> referencesList, ApplicantReferenceAdapterCallBack callBack) {
        this.referencesList = referencesList;
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reference, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicantReferenceModel applicantReferenceModel = referencesList.get(position);
        if (applicantReferenceModel !=null){
            String phoneNumber = applicantReferenceModel.getCountryCode() + applicantReferenceModel.getPhoneNumber();

            holder.name.setText(applicantReferenceModel.getName());
            holder.designation.setText(applicantReferenceModel.getApplicantJobDataModel().getName());
            holder.number.setText(phoneNumber);

            holder.remove.setOnClickListener(view -> callBack.onRemoveClicked(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return referencesList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, designation, number, remove;
        ConstraintLayout referenceLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            designation = itemView.findViewById(R.id.designation);
            number = itemView.findViewById(R.id.number);
            remove = itemView.findViewById(R.id.remove);
            referenceLayout = itemView.findViewById(R.id.referenceLayout);

        }
    }

    public interface ApplicantReferenceAdapterCallBack {
        void onRemoveClicked(int position);
    }

}