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
import com.antizon.uit_android.generic.model.ModelExperience;

import java.util.List;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.MyViewHolder> {

    private static final String TAG = ExperienceAdapter.class.getSimpleName();
    List<ModelExperience> list;
    Context context;
    ExperienceAdapter.SelectionListener selectionListener;

    public ExperienceAdapter(List<ModelExperience> list, Context context, ExperienceAdapter.SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public ExperienceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_experience_for_job_post, parent, false);
        return new ExperienceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExperienceAdapter.MyViewHolder holder, final int position) {
        final ModelExperience dataModel = list.get(getItemViewType(position));

        holder.years.setText(dataModel.getYears()+" years");
        holder.title.setText(dataModel.getExperience());
        holder.priority.setText(dataModel.getPriority());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionListener.delete(position);
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

        TextView years, title, priority, remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            years = itemView.findViewById(R.id.years);
            title = itemView.findViewById(R.id.title);
            priority = itemView.findViewById(R.id.requireOrPreferred);
            remove = itemView.findViewById(R.id.removeExperience);
        }
    }

    public interface SelectionListener {
        void delete(int position);
    }

}