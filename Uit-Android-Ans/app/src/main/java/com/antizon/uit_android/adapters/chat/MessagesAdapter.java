package com.antizon.uit_android.adapters.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.chat.MessageModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import net.time4j.Moment;
import net.time4j.PrettyTime;
import net.time4j.format.expert.Iso8601Format;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    public static final int LOADING = 2;
    public static final int TYPE_SENDER = 0;
    public static final int TYPE_RECEIVER = 1;
    Context context;
    String userID;

    List<MessageModel> list;
    private boolean isLoadingAdded;

    String otherUserProfile;

    public MessagesAdapter(Context context, List<MessageModel> list, String otherUserProfile, String  userID) {
        this.context = context;
        this.list = list;
        this.otherUserProfile = otherUserProfile;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SENDER) {
            view = LayoutInflater.from(context).inflate(R.layout.sender_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.receiver_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (holder.getItemViewType() == LOADING) return;//return if viewType is LOADING

            MessageModel current = list.get(position);

            Moment moment;
            try {
                moment = Iso8601Format.EXTENDED_DATE_TIME_OFFSET.parse(current.getCreatedAt());
                String ago = PrettyTime.of(Locale.getDefault()).printRelativeInStdTimezone(moment);
                ago = ago.replace("moments", "sec");
                ago = ago.replace("minutes", "min");
                ago = ago.replace("seconds", "sec");
                ago = ago.replace("hours", "hrs");
                ago = ago.replace("days", "d");
                holder.tvMessageTime.setText(ago);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //If Image is not present in the message
            if (current.getImageUrl() == null && TextUtils.isEmpty(current.getImageUrl())) {
                holder.rlMessageImage.setVisibility(View.GONE);
                holder.tvMessage.setVisibility(View.VISIBLE);
                holder.tvMessage.setText(current.getMessage());
            } else {
                holder.tvMessage.setVisibility(View.GONE);
                holder.rlMessageImage.setVisibility(View.VISIBLE);

                //Setting the Message Image
                Utilities.loadImage(context, current.getImageUrl(), holder.ivMessageImage);
            }

            //Setting Receiver Profile Picture
            if (holder.getItemViewType() == TYPE_RECEIVER) {
                Utilities.loadImage(context, otherUserProfile, holder.ivReceiverProfile);
            }

            //open image on click of view photo


        } catch (Exception ignored) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingAdded && position == list.size() - 1) {
            return LOADING;
        } else {
            int id = Integer.parseInt(userID);
            if (list.get(position).getUser().getUserId() == id) {
                return TYPE_SENDER;
            } else {
                return TYPE_RECEIVER;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setList(List<MessageModel> list) {
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<MessageModel> list, boolean notify) {
        this.list = list;
        if (notify)
            notifyDataSetChanged();
    }

    public void addAll(List<MessageModel> moveResults) {
        for (int i = 0; i < moveResults.size(); i++) {
            list.add(moveResults.get(i));
            notifyItemInserted(list.size() - 1);
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        list.add(new MessageModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = list.size() - 1;
        if (position >= 0) {
            MessageModel result = list.get(position);
            if (result != null) {
                //remove the LOADING item
                list.remove(position);
                //notify the removed item
                notifyItemRemoved(position);
            }
        }
    }

    public void setPost(MessageModel post, int position) {
        list.set(position, post);
        notifyItemChanged(position);
    }

    public void clear() {
        list.clear();
    }

    public void addNewItem(MessageModel message) {
        list.add(message);
        notifyItemInserted(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage, tvMessageTime, tvViewPhoto;
        RoundedImageView ivMessageImage, ivReceiverProfile;

        RelativeLayout rlMessageImage, rlViewPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvMessageTime = itemView.findViewById(R.id.tv_message_time);
            ivMessageImage = itemView.findViewById(R.id.iv_message_image);
            rlMessageImage = itemView.findViewById(R.id.rl_message_image);
            ivReceiverProfile = itemView.findViewById(R.id.iv_user_profile);
            tvViewPhoto = itemView.findViewById(R.id.tv_view_photo);
            rlViewPhoto = itemView.findViewById(R.id.rl_view_photo);
        }
    }
}