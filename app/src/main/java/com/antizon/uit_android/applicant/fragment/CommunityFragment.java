package com.antizon.uit_android.applicant.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.comments.CommentsActivity;
import com.antizon.uit_android.activities.community.CommunityCreatePostActivity;
import com.antizon.uit_android.activities.community.CommunityNewChannelActivity;
import com.antizon.uit_android.activities.community.CommunityPostDetailActivity;
import com.antizon.uit_android.adapters.community.CommunityAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.AllChannelsResponseModel;
import com.antizon.uit_android.models.community.ChannelDataModel;
import com.antizon.uit_android.models.community.CommunityFeedsResponseModel;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.models.community.CommunityPusherPostDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFragment extends Fragment implements CommunityAdapter.CommunityAdapterCallBack, SwipeRefreshLayout.OnRefreshListener {
    GetDataService service;
    Context context;

    SessionManagement sessionManagement;

    SwipeRefreshLayout swipe;
    RecyclerView recyclerview_activePost;
    List<CommunityPostDataModel> communityList;
    CommunityAdapter communityAdapter;
    List<ChannelDataModel> channelList;
    AllChannelsResponseModel allChannelsResponse;

    PusherOptions options;
    Pusher pusher;

    View rootView;

    int userId;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_community, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        recyclerview_activePost = rootView.findViewById(R.id.recyclerview_activePost);
        swipe = rootView.findViewById(R.id.swipe);

        sessionManagement = new SessionManagement(context);

        userId = Integer.parseInt(sessionManagement.getKeyId());
        requestForAllChannels("Bearer " + sessionManagement.getToken());

        swipe.setOnRefreshListener(this);
        return rootView;
    }

    private void requestForAllChannels(String authToken){
        Call<AllChannelsResponseModel> call = service.getAllChannels(authToken);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<AllChannelsResponseModel> call, @NotNull Response<AllChannelsResponseModel> response) {
                if (response.isSuccessful()) {
                    allChannelsResponse = response.body();
                    if (allChannelsResponse !=null){
                        boolean status = allChannelsResponse.isStatus();
                        if (status) {
                            channelList = new ArrayList<>();
                            channelList = allChannelsResponse.getChannelsList();

                            requestForCommunityFeeds(authToken);

                        }else {
                            CustomCookieToast.showRequiredToast(requireActivity(), allChannelsResponse.getMessage());
                        }
                    }

                }else {
                    CustomCookieToast.showRequiredToast(requireActivity(), response.message());
                }


            }

            @Override
            public void onFailure(@NotNull Call<AllChannelsResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showRequiredToast(requireActivity(), t.getMessage());
            }
        });

    }

    private void requestForCommunityFeeds(String authToken){
        Call<CommunityFeedsResponseModel> call = service.getCommunityPosts(authToken);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<CommunityFeedsResponseModel> call, @NotNull Response<CommunityFeedsResponseModel> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        communityList = new ArrayList<>();

                        communityList = response.body().getFeedsList();
                        showFeedsRecyclerview(recyclerview_activePost, channelList, communityList);
                        setPostsPusher();
                    }else {
                        CustomCookieToast.showRequiredToast(requireActivity(), response.body().getMessage());
                    }
                }else {
                    CustomCookieToast.showRequiredToast(requireActivity(), response.message());
                }


            }

            @Override
            public void onFailure(@NotNull Call<CommunityFeedsResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showRequiredToast(requireActivity(), t.getMessage());
            }
        });

    }

    private void showFeedsRecyclerview(RecyclerView recyclerView, List<ChannelDataModel> channelList, List<CommunityPostDataModel> communityList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        communityAdapter = new CommunityAdapter(context, channelList, communityList ,sessionManagement.getRole(), this);
        recyclerView.setAdapter(communityAdapter);
    }

    @Override
    public void onLikeClicked(String postId) {
        requestForLikePost("Bearer " + sessionManagement.getToken(), postId);
    }

    @Override
    public void onCommentClicked(String postId) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onAddChannelClick() {
        Intent intent = new Intent(context, CommunityNewChannelActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onNewPostClicked() {
        Intent intent = new Intent(context, CommunityCreatePostActivity.class);
        intent.putExtra("allChannelsResponse", allChannelsResponse);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onOptionMenuClicked(int postUserId) {
        if (userId == postUserId) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_post_admin, rootView.findViewById(R.id.bottom_sheet_post_admin));

            RelativeLayout btnCancel = sheetView.findViewById(R.id.btn_cancel);

            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
            bottomSheetDialog.setDismissWithAnimation(true);
            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        } else {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_post_other_user, rootView.findViewById(R.id.bottom_sheet_post_other_user));

            RelativeLayout btnCancel = sheetView.findViewById(R.id.btn_cancel);

            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
            bottomSheetDialog.setDismissWithAnimation(true);
            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(context, CommunityPostDetailActivity.class);
        intent.putExtra("postData", communityList.get(position));
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onPostShareClicked(int position) {
        String postUrl;
        if (communityList.get(position).getVideo_url() != null){
            postUrl = communityList.get(position).getVideo_url();
        }else {
            postUrl = communityList.get(position).getImage_path();
        }
        Utilities.shareAppLink(context, postUrl);
    }

    private void requestForLikePost(String authToken, String postId) {
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<MainResponseModel> call = service.likePost(authToken, postId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onRefresh() {
        Vibrator v = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> swipe.setRefreshing(false), 400);
        requestForAllChannels("Bearer " + sessionManagement.getToken());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPostsPusher() {
        // pusher
        options = new PusherOptions();
        options.setCluster("us3").setUseTLS(false);

        pusher = new Pusher("785126dad3606ac09387", options);
        Channel channel = pusher.subscribe("Community");

        channel.bind("NewPost", event -> {
            Gson gson = new Gson();
            CommunityPusherPostDataModel pusherPostDataModel = gson.fromJson(event.getData(), CommunityPusherPostDataModel.class);
            communityList.add(0, pusherPostDataModel.getCommunityPostDataModel());
            requireActivity().runOnUiThread(() -> communityAdapter.notifyDataSetChanged());
        });
        pusher.connect();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }
}