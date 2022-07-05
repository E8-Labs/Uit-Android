package com.antizon.uit_android.adapters.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.community.ChannelDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;

public class CommunityChannelsAdapter extends RecyclerView.Adapter<CommunityChannelsAdapter.ViewHolder> {
    public static final int VIEW_TYPE_ADD = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    Context context;
    List<ChannelDataModel> allChannelModelList;
    CommunityChannelsAdapterCallBack callBack;
    String role;
    public CommunityChannelsAdapter(Context context, List<ChannelDataModel> allChannelModelList, String role, CommunityChannelsAdapterCallBack callBack) {
        this.context = context;
        this.allChannelModelList = allChannelModelList;
        this.role = role;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ADD){
            view = LayoutInflater.from(context).inflate(R.layout.single_item_add_channel_feed, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.single_item_channel_feed, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == allChannelModelList.size()){
            holder.itemView.setOnClickListener(v -> callBack.onAddChannelClick());
        }else {
            ChannelDataModel channelDataModel = allChannelModelList.get(position);
            if (channelDataModel !=null){
                if (channelDataModel.getImage_path() !=null){
                    Utilities.loadImage(context, channelDataModel.getImage_path(), holder.channel_image);
                }

                if (channelDataModel.getName() !=null){
                    holder.text_name.setText(channelDataModel.getName());
                }

                holder.itemView.setOnClickListener(v -> callBack.onItemClick(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (role.equals("1")){
            return allChannelModelList.size() + 1;
        }else {
            return allChannelModelList.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == allChannelModelList.size()) {
            return VIEW_TYPE_ADD;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView channel_image;
        TextView text_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            channel_image = itemView.findViewById(R.id.channel_image);
            text_name = itemView.findViewById(R.id.text_name);
        }
    }

    public interface CommunityChannelsAdapterCallBack{
        void onItemClick(int position);
        void onAddChannelClick();
    }
}
