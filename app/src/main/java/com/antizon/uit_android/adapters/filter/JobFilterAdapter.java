package com.antizon.uit_android.adapters.filter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class JobFilterAdapter extends RecyclerView.Adapter<JobFilterAdapter.ViewHolder> {

    Context context;
    List<String> filtersList;

    public JobFilterAdapter(Context context, List<String> filtersList) {
        this.context = context;
        this.filtersList = filtersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_job_filter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {
        String filterName = filtersList.get(position);

        if (filterName != null) {
            holder.tv_title.setText(filterName);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filtersList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;


        public ViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
        }
    }

}
