package com.antizon.uit_android.recruiter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.recruiter.models.RecruiterJobTypeSelectionModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;

public class CompanyTeamMemberJobSelectionAdapter extends RecyclerView.Adapter<CompanyTeamMemberJobSelectionAdapter.ViewHolder> {

    Context context;
    List<RecruiterJobTypeSelectionModel> typesList;
    CompanyTeamMemberJobSelectionAdapterCallBack callBack;

    public CompanyTeamMemberJobSelectionAdapter(Context context, List<RecruiterJobTypeSelectionModel> typesList, CompanyTeamMemberJobSelectionAdapterCallBack callBack) {
        this.context = context;
        this.typesList = typesList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_channel_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecruiterJobTypeSelectionModel typeSelectionModel = typesList.get(position);
        if (typeSelectionModel !=null){
            Utilities.loadImage(context, typeSelectionModel.getImage(), holder.channel_image);
            holder.text_name.setText(typeSelectionModel.getName());

            if (typeSelectionModel.isSelected()){
                holder.text_name.setTextColor(context.getColor(R.color.app_color));
                holder.layout_profile.setBackgroundResource(R.drawable.app_color_outline_rounded_50dp);
            }else {
                holder.text_name.setTextColor(context.getColor(R.color.black));
                holder.layout_profile.setBackgroundResource(R.drawable.app_color_transparent_7dp_rounded);
            }

            holder.itemView.setOnClickListener(v -> {
                if (!typeSelectionModel.isSelected()){
                    callBack.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return typesList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout_profile;
        RoundedImageView channel_image;
        TextView text_name;
        ImageView btn_editChannel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_profile = itemView.findViewById(R.id.layout_profile);
            channel_image = itemView.findViewById(R.id.channel_image);
            text_name = itemView.findViewById(R.id.text_name);
            btn_editChannel = itemView.findViewById(R.id.btn_editChannel);
            btn_editChannel.setVisibility(View.GONE);
        }
    }

    public interface CompanyTeamMemberJobSelectionAdapterCallBack{
        void onItemClick(int position);
    }
}