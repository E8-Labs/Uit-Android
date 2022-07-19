package com.antizon.uit_android.generic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.welcome.AddEducation;
import com.antizon.uit_android.generic.model.ModelApplicantDegree;

import java.util.List;

public class ApplicantDegreeAdapter extends RecyclerView.Adapter<ApplicantDegreeAdapter.MyViewHolder> {

    private static final String TAG = ApplicantDegreeAdapter.class.getSimpleName();
    List<ModelApplicantDegree> list;
    Context context;
    SelectionListener selectionListener;


    public ApplicantDegreeAdapter(List<ModelApplicantDegree> list, Context context, SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;

    }

    @NonNull
    @Override
    public ApplicantDegreeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_degree, parent, false);
        return new ApplicantDegreeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicantDegreeAdapter.MyViewHolder holder, final int position) {
        final ModelApplicantDegree dataModel = list.get(getItemViewType(position));

        holder.applicantDegree.setText(dataModel.getName());
//        holder.applicantSubtitle.setText(dataModel.getName());

        if (dataModel.getSelected()) {
            holder.applicantDegree.setBackground(context.getResources().getDrawable(R.drawable.app_color_curved_background));
            holder.applicantDegree.setTextColor(context.getResources().getColor(R.color.white));
            holder.applicantDegree.setPadding(20, 40, 20, 40);
        } else {
            holder.applicantDegree.setBackground(context.getResources().getDrawable(R.drawable.industry_borders));
            holder.applicantDegree.setTextColor(context.getResources().getColor(R.color.black));
            holder.applicantDegree.setPadding(20, 40, 20, 40);
        }

        holder.applicantDegreeLayout.setOnClickListener(view -> {

            Intent intent = new Intent(context, AddEducation.class);
            context.startActivity(intent);
//
//                Toast.makeText(context, "" + dataModel.getName(), Toast.LENGTH_SHORT).show();

            holder.applicantDegree.setBackground(context.getResources().getDrawable(R.drawable.app_color_curved_background));
            holder.applicantDegree.setTextColor(context.getResources().getColor(R.color.white));
            holder.applicantDegree.setPadding(20, 40, 20, 40);
            selectionListener.selectedapplicantDegree(position);


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

        TextView applicantDegree, applicantSubtitle;
        ConstraintLayout applicantDegreeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            applicantDegree = itemView.findViewById(R.id.applicant_degree);
            applicantDegreeLayout = itemView.findViewById(R.id.applicant_degree_layout);
        }
    }

    public interface SelectionListener {
        void selectedapplicantDegree(int modelapplicantDegree);
    }
}