package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.welcome.AddEducation;
import com.antizon.uit_android.generic.model.ModelApplicantEducation;


import java.util.List;

public class ApplicantEducationAdapter extends RecyclerView.Adapter<ApplicantEducationAdapter.MyViewHolder> {

    private static final String TAG = ApplicantEducationAdapter.class.getSimpleName();
    List<ModelApplicantEducation> list;
    Context context;

    public ApplicantEducationAdapter(List<ModelApplicantEducation> list, Context context) {

        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicantEducationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_education, parent, false);
        return new ApplicantEducationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicantEducationAdapter.MyViewHolder holder, final int position) {
        final ModelApplicantEducation dataModel = list.get(getItemViewType(position));

        holder.applicantEducation.setText(dataModel.getName());
        holder.subtitle.setText(dataModel.getSubTitle());
        holder.applicantEducationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(context, AddEducation.class);
//                context.startActivity(intent);

                Toast.makeText(context, "" + dataModel.getName(), Toast.LENGTH_SHORT).show();


            }
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

        TextView applicantEducation , subtitle;
        ConstraintLayout applicantEducationLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

//            subtitle = itemView.findViewById(R.id.subtitle);
//            applicantEducation = itemView.findViewById(R.id.applicant_education);
            applicantEducationLayout = itemView.findViewById(R.id.applicant_education_layout);
        }
    }

}