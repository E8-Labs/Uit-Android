package com.antizon.uit_android.adapters.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.models.community.ChannelDataModel;

import java.util.List;

public class HorizontalChannelsAdapter extends RecyclerView.Adapter<HorizontalChannelsAdapter.ViewHolder> {

    Context context;
    List<ChannelDataModel> channelsList;
    HorizontalChannelsAdapterCallBack callBack;


    public HorizontalChannelsAdapter(Context context, List<ChannelDataModel> channelsList, HorizontalChannelsAdapterCallBack callBack) {
        this.context = context;
        this.channelsList = channelsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_select_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChannelDataModel channelDataModel = channelsList.get(position);
        if (channelDataModel !=null){

            if (channelDataModel.getName() !=null){
                holder.text_channelName.setText(channelDataModel.getName());
            }

            boolean selected = channelDataModel.isSelected();
            if (selected){
                holder.layout_channelName.setBackgroundResource(R.drawable.app_color_4dp_rounded);
                holder.text_channelName.setTextColor(ContextCompat.getColor(context, R.color.white));
            }else{
                holder.layout_channelName.setBackgroundResource(R.drawable.app_color_4dp_rounded_outline);
                holder.text_channelName.setTextColor(ContextCompat.getColor(context, R.color.codGrey));
            }

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return channelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout_channelName;
        TextView text_channelName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_channelName = itemView.findViewById(R.id.layout_channelName);
            text_channelName = itemView.findViewById(R.id.text_channelName);
        }
    }

    public interface HorizontalChannelsAdapterCallBack{
        void onItemClick(int position);
    }
}
