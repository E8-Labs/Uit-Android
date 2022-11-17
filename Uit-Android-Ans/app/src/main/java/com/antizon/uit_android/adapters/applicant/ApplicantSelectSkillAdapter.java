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
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ApplicantSelectSkillAdapter extends RecyclerView.Adapter<ApplicantSelectDegreeAdapter.MyViewHolder> {

    Context context;
    List<SkillDataModel> skillsList;
    ApplicantSelectSkillAdapterCallBack callBack;

    public ApplicantSelectSkillAdapter(Context context, List<SkillDataModel> skillsList, ApplicantSelectSkillAdapterCallBack callBack) {
        this.context = context;
        this.skillsList = skillsList;
        this.callBack = callBack;

    }

    @NonNull
    @Override
    public ApplicantSelectDegreeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_select_degree, parent, false);
        return new ApplicantSelectDegreeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull final ApplicantSelectDegreeAdapter.MyViewHolder holder, final int position) {
        SkillDataModel degreeDataModel = skillsList.get(position);

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
        return skillsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<SkillDataModel> filteredList) {
        this.skillsList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_degreeName;


        public MyViewHolder(View view) {
            super(view);
            text_degreeName = view.findViewById(R.id.text_degreeName);
        }
    }

    public interface ApplicantSelectSkillAdapterCallBack{
        void onItemClick(SkillDataModel skillDataModel);
    }

}