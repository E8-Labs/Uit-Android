package com.antizon.uit_android.notifications.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.comments.CommentDataModel;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.notifications.NotificationSpannable;
import com.antizon.uit_android.notifications.models.NotificationModel;
import com.antizon.uit_android.utilities.ArfiDeveloperTime;
import com.antizon.uit_android.utilities.MySpanny;
import com.antizon.uit_android.utilities.Utilities;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<NotificationModel> notificationsList;
    NotificationAdapterCallBack callBack;

    public NotificationAdapter(Context context, List<NotificationModel> notificationsList, NotificationAdapterCallBack callBack) {
        this.context = context;
        this.notificationsList = notificationsList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notificationModel = notificationsList.get(position);
        if (notificationModel == null) return;

        holder.tvTime.setText(new ArfiDeveloperTime().getPrettyTimeFromCreatedAt(notificationModel.getCreatedAt()));

        setSeen(holder, !notificationModel.isSeen());

        GenericApplicantDataModel fromUser = notificationModel.getFromUser();

        if (fromUser != null){
            Utilities.loadImage(context, fromUser.getProfile_image(), holder.ivProfile);

            CommentDataModel comment = notificationModel.getComment();
            final CommunityPostDataModel post = notificationModel.getPost();


            MySpanny spanny = new MySpanny();
            final NotificationSpannable black = new NotificationSpannable(context, false, false, R.color.black);
            final NotificationSpannable appColor = new NotificationSpannable(context, false, false, R.color.app_color);
            final NotificationSpannable blackBold = new NotificationSpannable(context, false, true, R.color.black);

            if (notificationModel.getNotificationType() == 1){
                holder.btnNotificationType.setVisibility(View.VISIBLE);
            }else {
                holder.btnNotificationType.setVisibility(View.GONE);
            }

            if (!notificationModel.isSeen()){
                holder.itemView.setOnClickListener(v -> callBack.onNotificationClicked(position, notificationModel));
            }

            switch (notificationModel.getNotificationType()) {
                case 1:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" sent you a message", black);
                    break;
                case 2:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" is joining UI as applicant", black);
                    break;
                case 3:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" approval is pending", black);
                    break;
                case 4:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" commented on a post", black);
                    if (comment != null){
                        spanny.append(" \"" + comment.getComment() + "\"", appColor);
                    }
                    break;
                case 5:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" flagged the user", black);
                    break;
                case 6:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" flagged the job", black);
                    break;
                case 7:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" is joining UIT as new Hire", black);
                    break;
                case 8:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" is joining UIT as new Applicant", black);
                    break;
                case 9:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" is joining UIT as new Company", black);
                    break;
                case 10:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" is joining UIT as new Recruiter", black);
                    break;
                case 11:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" is joining UIT as new Member", black);
                    break;
                case 12:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" has approved the job", black);
                    break;
                case 13:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" has rejected the job", black);
                    break;
                case 14:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" has liked the post\"", black);
                    if (post!=null){
                        spanny.append(post.getPost_title(), appColor);
                    }else{
                        spanny.append("post not found", appColor);
                    }
                    spanny.append(" \"", black);
                    break;
                case 15:
                    spanny.append(fromUser.getName(), blackBold);
                    spanny.append(" has unliked the post", black);
                    if (post!=null){
                        spanny.append(post.getPost_title(), appColor);
                    }else{
                        spanny.append("post not found", appColor);
                    }
                    spanny.append(" \"", black);
                    break;
            }

            holder.tvNotification.setText(spanny);
        }

    }

    private void setSeen(ViewHolder holder, boolean isSeen) {
        if (isSeen) {
            holder.rlRoot.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_green_light));
        } else {
            holder.rlRoot.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_white_7sdp_rounded));
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList == null ? 0 : notificationsList.size();
    }


    public interface NotificationAdapterCallBack {
        void onNotificationClicked(int position, NotificationModel notificationModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View rlRoot;
        ImageView ivProfile, image_notificationType;
        TextView tvNotification, tvTime;
        RelativeLayout btnNotificationType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlRoot = itemView.findViewById(R.id.rl_root);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            btnNotificationType = itemView.findViewById(R.id.btnNotificationType);
            image_notificationType = itemView.findViewById(R.id.image_notificationType);
            tvNotification = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
