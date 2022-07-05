package com.antizon.uit_android.generic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.WorkExperienceModel;

import java.util.List;

public class WorkExperienceAdapter extends RecyclerView.Adapter<WorkExperienceAdapter.MyViewHolder> {

    private static final String TAG = WorkExperienceAdapter.class.getSimpleName();
    List<WorkExperienceModel> list;
    Context context;
    WorkExperienceAdapter.SelectionListener selectionListener;

    public WorkExperienceAdapter(List<WorkExperienceModel> list, Context context, WorkExperienceAdapter.SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public WorkExperienceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_education, parent, false);
        return new WorkExperienceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkExperienceAdapter.MyViewHolder holder, final int position) {
        final WorkExperienceModel dataModel = list.get(getItemViewType(position));

        holder.fieldOfStudy.setText(dataModel.getJob());
        holder.universityName.setText(dataModel.getCompany());
        holder.profession.setText(dataModel.getStartYear());
        holder.deadline.setText(dataModel.getRoleText());

//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.delete(position);
//            }
//        });
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

        TextView fieldOfStudy, universityName, profession, deadline,edit;
        ConstraintLayout educationLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            fieldOfStudy = itemView.findViewById(R.id.fieldOfStudy);
            universityName = itemView.findViewById(R.id.universityName);
            profession = itemView.findViewById(R.id.profession);
            deadline = itemView.findViewById(R.id.deadline);
            edit = itemView.findViewById(R.id.edit);
            educationLayout = itemView.findViewById(R.id.applicant_education_layout);
        }
    }

    public interface SelectionListener {
        void delete(int position);
    }

}