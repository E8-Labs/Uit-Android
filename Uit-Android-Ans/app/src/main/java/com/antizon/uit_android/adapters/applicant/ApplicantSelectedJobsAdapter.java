package com.antizon.uit_android.adapters.applicant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class ApplicantSelectedJobsAdapter extends RecyclerView.Adapter<ApplicantSelectedJobsAdapter.ViewHolder> {

    Context context;
    List<ApplicantJobDataModel> jobsList;
    ApplicantSelectedJobsAdapterCallBack callBack;

    public ApplicantSelectedJobsAdapter(Context context, List<ApplicantJobDataModel> jobsList, ApplicantSelectedJobsAdapterCallBack callBack) {
        this.context = context;
        this.jobsList = jobsList;
        this.callBack = callBack;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_selected_job, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {
        ApplicantJobDataModel degreeDataModel = jobsList.get(position);

        if (degreeDataModel != null) {
            if (degreeDataModel.getName()!=null){
                holder.tv_title.setText(degreeDataModel.getName());
            }
            holder.iv_close.setOnClickListener(v -> callBack.onCrossBtnClicked(position));
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
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_close;
        public ViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            iv_close = view.findViewById(R.id.iv_close);
        }
    }

    public interface ApplicantSelectedJobsAdapterCallBack{
        void onCrossBtnClicked(int position);
    }

}
