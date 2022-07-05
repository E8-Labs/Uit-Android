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
import com.antizon.uit_android.generic.model.ModelExperience;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyViewHolder> {

    private static final String TAG = SkillAdapter.class.getSimpleName();
    List<ModelExperience> list;
    Context context;
    SkillAdapter.SelectionListener selectionListener;

    public SkillAdapter(List<ModelExperience> list, Context context, SkillAdapter.SelectionListener selectionListener) {

        this.list = list;
        this.context = context;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public SkillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);
        return new SkillAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SkillAdapter.MyViewHolder holder, final int position) {
        final ModelExperience dataModel = list.get(getItemViewType(position));

        holder.skill.setText(dataModel.getSkill());

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

        TextView skill;
        ImageView remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            skill = itemView.findViewById(R.id.skillValue);
            remove = itemView.findViewById(R.id.removeSkill);
        }
    }

    public interface SelectionListener {
        void delete(int position);
    }

}