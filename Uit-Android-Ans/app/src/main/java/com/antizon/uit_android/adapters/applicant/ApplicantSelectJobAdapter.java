package com.antizon.uit_android.adapters.applicant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ApplicantSelectJobAdapter extends RecyclerView.Adapter<ApplicantSelectJobAdapter.ViewHolder> {

    Context context;
    List<ApplicantJobDataModel> jobsList;
    ApplicantSelectJobAdapterCallBack callBack;

    public ApplicantSelectJobAdapter(Context context, List<ApplicantJobDataModel> jobsList, ApplicantSelectJobAdapterCallBack callBack) {
        this.context = context;
        this.jobsList = jobsList;
        this.callBack = callBack;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_select_degree, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {
        ApplicantJobDataModel degreeDataModel = jobsList.get(position);

        if (degreeDataModel != null) {
            if (degreeDataModel.getName()!=null){
                holder.text_degreeName.setText(degreeDataModel.getName());
            }
            holder.itemView.setOnClickListener(v -> callBack.onItemClick(degreeDataModel));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<ApplicantJobDataModel> filteredList) {
        this.jobsList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_degreeName;

        public ViewHolder(View view) {
            super(view);
            text_degreeName = view.findViewById(R.id.text_degreeName);
        }
    }

    public interface ApplicantSelectJobAdapterCallBack{
        void onItemClick(ApplicantJobDataModel degreeDataModel);
    }

}
