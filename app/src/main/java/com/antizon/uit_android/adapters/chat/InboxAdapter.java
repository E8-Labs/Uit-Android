package com.antizon.uit_android.adapters.chat;


import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.chat.InboxModel;
import com.antizon.uit_android.models.chat.InboxUser;
import com.antizon.uit_android.utilities.ArfiDeveloperTime;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;


public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {
    List<InboxModel> chatList;
    Context context;
    String userID;

    InboxAdapterCallBack callBack;

    // Constructor
    public InboxAdapter(Context context, List<InboxModel> arrayList, String userID, InboxAdapterCallBack callBack) {
        this.chatList = arrayList;
        this.context = context;
        this.userID = userID;
        this.callBack = callBack;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_inbox, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull final MyViewHolder holder, final int position) {
        InboxModel chatModel = chatList.get(position);

        if (chatModel != null) {
            String time = new ArfiDeveloperTime().getPrettyTime(chatModel.getLastMessageDate(), "YYY-MM-dd HH:mm:ss");
            holder.tvTime.setText(time);

            //setting the message
            holder.tvMessage.setText(chatModel.getLastMessage());

            if (chatModel.getUsers() == null || chatModel.getUsers().size() < 2) return;

            InboxUser user = chatModel.getUsers().get(1);
            if (user == null) return;

            //setting the other user name
            holder.tvUserName.setText(user.getName());
            //Setting the Other user Image
            Utilities.loadImage(context, user.getProfileImage(), holder.userImage);

            holder.itemView.setOnClickListener(v -> callBack.onItemClick(chatModel));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<InboxModel> filteredList) {
        this.chatList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView tvUserName, tvMessage, tvTime;

        public MyViewHolder(View view) {
            super(view);
            userImage = view.findViewById(R.id.iv_user_profile);
            tvUserName = view.findViewById(R.id.tv_full_name);
            tvMessage = view.findViewById(R.id.tv_message);
            tvTime = view.findViewById(R.id.tv_time);
        }
    }

    public interface InboxAdapterCallBack{
        void onItemClick(InboxModel inboxModel);
    }

}
