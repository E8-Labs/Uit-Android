package com.antizon.uit_android.adapters.community;

import static android.view.View.GONE;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.UitUserModel;
import com.antizon.uit_android.models.community.ChannelDataModel;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.utilities.DoubleClickListener;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> implements CommunityChannelsAdapter.CommunityChannelsAdapterCallBack {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_LOADING = 2;
    boolean isLoadingAdded = false;

    Context context;
    CommunityChannelsAdapter communityChannelsAdapter;
    List<ChannelDataModel> channelList;
    List<CommunityPostDataModel> communityList;
    CommunityAdapterCallBack callBack;
    String role;

    public CommunityAdapter(Context context, List<ChannelDataModel> channelList, List<CommunityPostDataModel> communityList, String role, CommunityAdapterCallBack callBack) {
        this.context = context;
        this.channelList = channelList;
        this.communityList = communityList;
        this.role = role;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_TYPE_LOADING) {
            v = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
        } else if (viewType == VIEW_TYPE_HEADER) {
            v = LayoutInflater.from(context).inflate(R.layout.community_zero_index_list_item, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.single_item_community_feed, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder.getItemViewType() != VIEW_TYPE_LOADING) {
            if (position == 0) {
                if (role.equals("1") || role.equals("3")) {
                    holder.btn_newPost.setVisibility(View.VISIBLE);
                    holder.btn_notifications.setVisibility(View.GONE);
                } else {
                    holder.btn_newPost.setVisibility(View.GONE);
                    holder.btn_notifications.setVisibility(View.VISIBLE);

                    SessionManagement sessionManagement = new SessionManagement(context);
                    if (sessionManagement.getKeyUnreadNotifications().equals("0")){
                        holder.dashboardNotification.setImageResource(R.drawable.notification_all_read_ic);
                    }else {
                        holder.dashboardNotification.setImageResource(R.drawable.notifications_ic);
                    }
                }

                holder.btn_newPost.setOnClickListener(v -> callBack.onNewPostClicked());
                String activePosts = communityList.size() + " Active Posts";
                showChannelsRecyclerView(holder.recyclerview_channels, channelList, role);
                holder.text_activePosts.setText(activePosts);

            } else {
                CommunityPostDataModel post = communityList.get(position - 1);
                if (post != null) {
                    String comments = post.getComments() + "";
                    String likes = post.getLikes() + " ";

                    if (post.getVideo_url() != null) {
                        holder.video_ic.setVisibility(View.VISIBLE);
                        holder.loading_image.setVisibility(View.GONE);
                    } else {
                        holder.video_ic.setVisibility(View.GONE);
                    }
                    Utilities.loadImage(context, post.getImage_path(), holder.post_image, holder.loading_image);

                    if (post.isIs_liked()) {
                        holder.like_ic.setImageResource(R.drawable.liked_ic);
                    } else {
                        holder.like_ic.setImageResource(R.drawable.unlike_ic);
                    }

                    holder.text_likesCounting.setText(likes);
                    holder.text_commentsCounting.setText(comments);

                    if (post.getPost_title() != null) {
                        holder.text_title.setText(post.getPost_title());
                    }

                    if (post.getPost_description() != null) {
                        holder.text_description.setText(post.getPost_description());
                    }

                    if (post.getEngagement_text() != null) {
                        holder.text_engagedCount.setText(post.getEngagement_text());
                    }

                    showLikedUserImages(holder, post);

                    holder.btn_like.setOnClickListener(v -> {
                        post.setIs_liked(!post.isIs_liked());
                        int updatedCount = updateLikeUi(holder, post);
                        post.setLikes(updatedCount);
                        callBack.onLikeClicked(String.valueOf(post.getId()));
                    });

                    UitUserModel uitUserModel = post.getUserModel();
                    if (uitUserModel != null) {
                        holder.posted_by.setText(uitUserModel.getName());
                        switch (uitUserModel.getRole()) {
                            case 1:
                                holder.text_role.setText(context.getString(R.string.text_admin));
                                break;
                            case 2:
                                holder.text_role.setText(context.getString(R.string.text_company));
                                break;
                            case 3:
                                holder.text_role.setText(context.getString(R.string.text_uitMember));
                                break;
                            case 4:
                                holder.text_role.setText(context.getString(R.string.text_companyMember));
                                break;
                            case 5:
                                holder.text_role.setText(context.getString(R.string.text_applicant));
                                break;
                        }

                        holder.btn_menu.setOnClickListener(v -> callBack.onOptionMenuClicked(position-1));
                    }

                    holder.btn_comment.setOnClickListener(v -> callBack.onCommentClicked(String.valueOf(post.getId())));

                    holder.itemView.setOnClickListener(new DoubleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            callBack.onItemClicked(position - 1);
                        }
                    });

                    holder.btnShare.setOnClickListener(v -> callBack.onPostShareClicked(position - 1));
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private int updateLikeUi(ViewHolder postViewHolder, CommunityPostDataModel post) {
        int updatedLikesCount;

        if (post.isIs_liked()) {
            postViewHolder.like_ic.setImageResource(R.drawable.liked_ic);
            updatedLikesCount = post.getLikes() + 1;
        } else {
            updatedLikesCount = post.getLikes() - 1;
            postViewHolder.like_ic.setImageResource(R.drawable.unlike_ic);
        }
        postViewHolder.text_likesCounting.setText(updatedLikesCount + "");
        return updatedLikesCount;
    }

    @Override
    public int getItemCount() {
        return communityList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == communityList.size() && isLoadingAdded) {
            return VIEW_TYPE_LOADING;
        } else if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    private void showChannelsRecyclerView(RecyclerView recyclerView, List<ChannelDataModel> channelList, String role) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        communityChannelsAdapter = new CommunityChannelsAdapter(context, channelList, role, this);
        recyclerView.setAdapter(communityChannelsAdapter);
    }

    @Override
    public void onItemClick(int position) {
        callBack.onChannelSelectedClicked(communityChannelsAdapter, position);
    }

    @Override
    public void onEditChannelClicked(int position) {
        callBack.onEditChannelClicked(communityChannelsAdapter, position);
    }

    @Override
    public void onAddChannelClick() {
        callBack.onAddChannelClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_title, text_description, posted_by, text_likesCounting, text_shareCounting, text_commentsCounting, text_engagedCount, text_role;
        ImageView post_image, like_ic, video_ic;
        RelativeLayout btn_menu, layout_admin, btn_comment, btn_like, btnShare;
        RoundedImageView civOne, civTwo, civThree, civFour;
        AVLoadingIndicatorView loading_image;

        // Channels
        RecyclerView recyclerview_channels;
        TextView text_activePosts;
        RelativeLayout btn_newPost, btn_notifications;
        ImageView dashboardNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_title);
            text_description = itemView.findViewById(R.id.text_description);
            posted_by = itemView.findViewById(R.id.posted_by);
            text_likesCounting = itemView.findViewById(R.id.text_likesCounting);
            text_shareCounting = itemView.findViewById(R.id.text_shareCounting);
            text_commentsCounting = itemView.findViewById(R.id.text_commentsCounting);
            text_engagedCount = itemView.findViewById(R.id.text_engagedCount);
            btn_menu = itemView.findViewById(R.id.btn_menu);
            post_image = itemView.findViewById(R.id.post_image);
            btn_like = itemView.findViewById(R.id.btn_like);
            layout_admin = itemView.findViewById(R.id.layout_admin);
            btn_comment = itemView.findViewById(R.id.btn_comment);
            btnShare = itemView.findViewById(R.id.btnShare);
            civOne = itemView.findViewById(R.id.civOne);
            civTwo = itemView.findViewById(R.id.civTwo);
            civThree = itemView.findViewById(R.id.civThree);
            civFour = itemView.findViewById(R.id.civFour);
            like_ic = itemView.findViewById(R.id.like_ic);
            text_role = itemView.findViewById(R.id.text_role);
            video_ic = itemView.findViewById(R.id.video_ic);
            loading_image = itemView.findViewById(R.id.loading_image);

            // Channels
            recyclerview_channels = itemView.findViewById(R.id.recyclerview_channels);
            text_activePosts = itemView.findViewById(R.id.text_activePosts);
            btn_newPost = itemView.findViewById(R.id.btn_newPost);
            btn_notifications = itemView.findViewById(R.id.btn_notifications);
            dashboardNotification = itemView.findViewById(R.id.dashboardNotification);

        }
    }

    public interface CommunityAdapterCallBack {
        void onLikeClicked(String postId);

        void onCommentClicked(String postId);

        void onAddChannelClick();

        void onNewPostClicked();

        void onOptionMenuClicked(int position);

        void onItemClicked(int position);

        void onPostShareClicked(int position);

        void onChannelSelectedClicked(CommunityChannelsAdapter communityChannelsAdapter, int position);
        void onEditChannelClicked(CommunityChannelsAdapter communityChannelsAdapter, int position);
    }

    private void showLikedUserImages(ViewHolder holder, CommunityPostDataModel post) {

        if (post.getEngagedPeople().size() == 0) {
            holder.civOne.setVisibility(View.GONE);
            holder.civTwo.setVisibility(View.GONE);
            holder.civThree.setVisibility(View.GONE);
            holder.civFour.setVisibility(View.GONE);
            holder.text_engagedCount.setVisibility(GONE);
        }else if (post.getEngagedPeople().size() == 1) {
            if (post.getEngagedPeople().get(0) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(0).getProfile_image(), holder.civOne);
            }
            holder.civTwo.setVisibility(View.GONE);
            holder.civThree.setVisibility(View.GONE);
            holder.civFour.setVisibility(View.GONE);
        }
        else if (post.getEngagedPeople().size() == 2) {
            if (post.getEngagedPeople().get(0) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(0).getProfile_image(), holder.civOne);
            }
            if (post.getEngagedPeople().get(1) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(1).getProfile_image(), holder.civTwo);
            }
            holder.civThree.setVisibility(View.GONE);
            holder.civFour.setVisibility(View.GONE);
        }
        else if (post.getEngagedPeople().size() == 3) {
            if (post.getEngagedPeople().get(0) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(0).getProfile_image(), holder.civOne);
            }

            if (post.getEngagedPeople().get(1) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(1).getProfile_image(), holder.civTwo);
            }
            if (post.getEngagedPeople().get(2) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(2).getProfile_image(), holder.civThree);
            }

            holder.civFour.setVisibility(View.GONE);
        }

        else if (post.getEngagedPeople().size() >= 4) {
            if (post.getEngagedPeople().get(0) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(0).getProfile_image(), holder.civOne);
            }

            if (post.getEngagedPeople().get(1) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(1).getProfile_image(), holder.civTwo);
            }
            if (post.getEngagedPeople().get(2) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(2).getProfile_image(), holder.civThree);
            }

            if (post.getEngagedPeople().get(3) != null) {
                Utilities.loadImage(context, post.getEngagedPeople().get(3).getProfile_image(), holder.civThree);
            }
        }
    }
}
