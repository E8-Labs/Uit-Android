package com.antizon.uit_android.adapters.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.filter.IndustryModel;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final Context context;
    private List<IndustryModel> list;

    public TagAdapter(Context context, List<IndustryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_selected_job, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final IndustryModel current = list.get(position);
        if (current == null) return;

        holder.tvTitle.setText(current.getName());

        holder.ivClose.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void addTag(IndustryModel model) {
        if (list == null)
            list = new ArrayList<>();
        list.add(0, model);
        notifyItemInserted(0);
    }

    public void clear() {
        if (list == null) list = new ArrayList<>();
        else list.clear();
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View ivClose, llBackground;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.ll_background);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivClose = itemView.findViewById(R.id.iv_close);
        }
    }
}
