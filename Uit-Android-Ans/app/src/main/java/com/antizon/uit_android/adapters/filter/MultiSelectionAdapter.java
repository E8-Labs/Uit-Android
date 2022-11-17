package com.antizon.uit_android.adapters.filter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.filter.MultiSelectionModel;

import java.util.List;

public class MultiSelectionAdapter extends RecyclerView.Adapter<MultiSelectionAdapter.ViewHolder> {

    final Context context;
    List<MultiSelectionModel> list;
    OnSelectionListener onSelectionListener;

    public MultiSelectionAdapter(Context context, List<MultiSelectionModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_horizontal_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MultiSelectionModel current = list.get(position);
        if (current == null) return;

        holder.tvTitle.setText(current.getTitle());

        setSelected(holder, current);

        holder.tvTitle.setOnClickListener(v -> {
            current.setSelected(!current.isSelected());
            setSelected(holder, current);
            if (onSelectionListener != null)
                onSelectionListener.onSelection(current.isSelected(), position, current);
        });
    }

    private void setSelected(@NonNull ViewHolder holder, MultiSelectionModel current) {
        if (current.isSelected()) {
            holder.tvTitle.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_app_color_rounded_5dp));
            holder.tvTitle.setTextColor(Color.WHITE);
        } else {
            holder.tvTitle.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_white_rounded_5dp_outline));
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.app_color));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void unselectAll() {
        if (list != null) {
            for (MultiSelectionModel model : list) {
                model.setSelected(false);
            }
            notifyDataSetChanged();
        }
    }

    public interface OnSelectionListener {
        void onSelection(boolean isSelected, int position, MultiSelectionModel model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
