package com.antizon.uit_android.adapters.community;

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
import com.antizon.uit_android.models.UitUserModel;
import com.antizon.uit_android.models.comments.CommentDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import net.time4j.Moment;
import net.time4j.PrettyTime;
import net.time4j.format.expert.Iso8601Format;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class CommentRepliesAdapter extends RecyclerView.Adapter<CommentRepliesAdapter.ViewHolder>  {

    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_LOADING = 2;
    boolean isLoadingAdded = false;

    Context context;
    List<CommentDataModel> commentsList;
    CommentRepliesAdapterCallBack callBack;
    String parentCommentId;

    public CommentRepliesAdapter(Context context, List<CommentDataModel> commentsList, String parentCommentId, CommentRepliesAdapterCallBack callBack) {
        this.context = context;
        this.commentsList = commentsList;
        this.parentCommentId = parentCommentId;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_TYPE_LOADING){
            v = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.single_item_comment_reply, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType() != VIEW_TYPE_LOADING) {
            CommentDataModel comment = commentsList.get(position);
            if (comment !=null){
                UitUserModel uitUserModel = comment.getUserModel();

                String comments =comment.getComments() + " ";

                String likes = comment.getLikes() +  " ";

                if (uitUserModel !=null){
                    if (uitUserModel.getProfile_image() != null){
                        Utilities.loadImage(context, uitUserModel.getProfile_image() , holder.image_profile);
                    }

                    if (uitUserModel.getName() !=null){
                        holder.text_profileName.setText(uitUserModel.getName());
                    }
                }

                if (comment.getComment() !=null){
                    holder.text_description.setText(comment.getComment());
                }

                if (comment.isLiked()) {
                    holder.btnLike.setImageResource(R.drawable.liked_ic);
                }
                else {
                    holder.btnLike.setImageResource(R.drawable.unlike_ic);
                }

                holder.text_likesCounting.setText(likes);
                holder.text_repliesCount.setText(comments);

                holder.btnLike.setOnClickListener(v -> {
                    comment.setLiked(!comment.isLiked());
                    int updatedCount = updateLikeUi(holder.btnLike, holder.text_likesCounting, comment.isLiked(), comment.getLikes());
                    comment.setLikes(updatedCount);
                    callBack.onLikeClicked(String.valueOf(comment.getId()));
                });

                if (comment.getCreated_at() !=null){
                    Moment moment;
                    try {
                        moment = Iso8601Format.EXTENDED_DATE_TIME_OFFSET.parse(comment.getCreated_at());
                        String ago = PrettyTime.of(Locale.getDefault()).printRelativeInStdTimezone(moment);
                        ago = ago.replace("moments", "sec");
                        ago = ago.replace("minutes", "min");
                        ago = ago.replace("seconds", "sec");
                        ago = ago.replace("hours", "hrs");
                        ago = ago.replace("days", "d");
                        holder.text_time.setText(ago);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    @SuppressLint("SetTextI18n")
    private int updateLikeUi(ImageView btnLike, TextView likesCount, boolean isLiked, int likeCount) {
        int updatedLikesCount;

        if (isLiked) {
            btnLike.setImageResource(R.drawable.liked_ic);
            updatedLikesCount = likeCount + 1;
        } else {
            updatedLikesCount =likeCount - 1;
            btnLike.setImageResource(R.drawable.unlike_ic);
        }
        String updatedCounter = updatedLikesCount + "";
        likesCount.setText(updatedCounter);
        return updatedLikesCount;
    }



    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == commentsList.size()-1 && isLoadingAdded) {
            return VIEW_TYPE_LOADING;
        }else {
            return VIEW_TYPE_NORMAL;
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView image_profile;
        TextView text_profileName, text_description, text_likesCounting, text_repliesCount, text_replyComment, text_time;
        ImageView btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            text_profileName = itemView.findViewById(R.id.text_profileName);
            text_description = itemView.findViewById(R.id.text_description);
            text_likesCounting = itemView.findViewById(R.id.text_likesCounting);
            text_repliesCount = itemView.findViewById(R.id.text_repliesCount);
            text_replyComment = itemView.findViewById(R.id.text_replyComment);
            text_time = itemView.findViewById(R.id.text_time);
            btnLike = itemView.findViewById(R.id.btnLike);
        }
    }

    public interface CommentRepliesAdapterCallBack{
        void onLikeClicked(String commentId);

    }
}
