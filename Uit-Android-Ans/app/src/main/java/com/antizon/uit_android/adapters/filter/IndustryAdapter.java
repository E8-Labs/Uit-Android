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

import java.util.List;

public class IndustryAdapter extends RecyclerView.Adapter<IndustryAdapter.ViewHolder> {

    Context context;
    List<IndustryModel> list;
    OnSelectionListener onSelectionListener;

    public IndustryAdapter(Context context, List<IndustryModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_industry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final IndustryModel current = list.get(position);
        if (current == null) return;

        holder.tvTitle.setText(current.getName());

        holder.tvTitle.setOnClickListener(v -> {
            if (onSelectionListener != null)
                onSelectionListener.onSelection(position, current);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnSelectionListener {
        void onSelection(int position, IndustryModel model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
