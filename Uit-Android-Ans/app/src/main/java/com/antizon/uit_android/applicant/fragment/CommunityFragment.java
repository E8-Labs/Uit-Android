package com.antizon.uit_android.applicant.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.comments.CommentsActivity;
import com.antizon.uit_android.activities.community.CommunityCreatePostActivity;
import com.antizon.uit_android.activities.community.CommunityNewChannelActivity;
import com.antizon.uit_android.activities.community.CommunityPostDetailActivity;
import com.antizon.uit_android.activities.jobs.FlagJobUserActivity;
import com.antizon.uit_android.adapters.community.CommunityAdapter;
import com.antizon.uit_android.adapters.community.CommunityChannelsAdapter;
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
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    SwipeRefreshLayout swipe;
    RecyclerView recyclerview_activePost;
    List<CommunityPostDataModel> communityList;
    CommunityAdapter communityAdapter;
    List<ChannelDataModel> channelList;
    AllChannelsResponseModel allChannelsResponse;

    View rootView;
    AlertDialog deleteDialog;
    BottomSheetDialog bottomSheetDialog;

    PusherOptions options;
    Pusher pusher;
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
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading.....");

        recyclerview_activePost = rootView.findViewById(R.id.recyclerview_activePost);
        swipe = rootView.findViewById(R.id.swipe);

        userId = Integer.parseInt(sessionManagement.getKeyId());
        requestForAllChannels("Bearer " + sessionManagement.getToken());
        setPostsPusher();

        swipe.setOnRefreshListener(this);

        return rootView;
    }

    private void requestForAllChannels(String authToken) {
        Call<AllChannelsResponseModel> call = service.getAllChannels(authToken);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<AllChannelsResponseModel> call, @NotNull Response<AllChannelsResponseModel> response) {
                if (response.isSuccessful()) {
                    allChannelsResponse = response.body();
                    if (allChannelsResponse != null) {
                        boolean status = allChannelsResponse.isStatus();
                        if (status) {
                            channelList = new ArrayList<>();
                            channelList = allChannelsResponse.getChannelsList();

                            requestForCommunityFeeds(authToken, "***********", "all");

                        } else {
                            CustomCookieToast.showRequiredToast(requireActivity(), allChannelsResponse.getMessage());
                        }
                    }

                } else {
                    CustomCookieToast.showRequiredToast(requireActivity(), response.message());
                }


            }

            @Override
            public void onFailure(@NotNull Call<AllChannelsResponseModel> call, @NotNull Throwable t) {
                CustomCookieToast.showRequiredToast(requireActivity(), t.getMessage());
            }
        });

    }

    private void requestForCommunityFeeds(String authToken, String channelId, String type) {
        Call<CommunityFeedsResponseModel> call;
        if (type.equals("all")) {
            call = service.getCommunityPosts(authToken);
        } else {
            call = service.getCommunityPostsByChannel(authToken, channelId);
        }

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<CommunityFeedsResponseModel> call, @NotNull Response<CommunityFeedsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        communityList = new ArrayList<>();

                        communityList = response.body().getFeedsList();
                        showFeedsRecyclerview(recyclerview_activePost, channelList, communityList);
                    } else {
                        CustomCookieToast.showRequiredToast(requireActivity(), response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showRequiredToast(requireActivity(), response.message());
                }


            }

            @Override
            public void onFailure(@NotNull Call<CommunityFeedsResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showRequiredToast(requireActivity(), t.getMessage());
            }
        });

    }

    private void showFeedsRecyclerview(RecyclerView recyclerView, List<ChannelDataModel> channelList, List<CommunityPostDataModel> communityList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        communityAdapter = new CommunityAdapter(context, channelList, communityList, sessionManagement.getRole(), this);
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
        intent.putExtra("type", "add");
        onChannelAddedLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onNewPostClicked() {
        Intent intent = new Intent(context, CommunityCreatePostActivity.class);
        intent.putExtra("type", "add");
        intent.putExtra("allChannelsResponse", allChannelsResponse);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onOptionMenuClicked(int position) {
        if (userId == communityList.get(position).getUser_id() || sessionManagement.getRole().equals("1")) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_post_admin, rootView.findViewById(R.id.bottom_sheet_post_admin));

            RelativeLayout btnEdit = sheetView.findViewById(R.id.btnEdit);
            RelativeLayout btnShare = sheetView.findViewById(R.id.btnShare);
            RelativeLayout btnDelete = sheetView.findViewById(R.id.btnDelete);
            RelativeLayout btnCancel = sheetView.findViewById(R.id.btn_cancel);

            btnDelete.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                showDeletePostPopup(position,communityList.get(position).getId()+"");
            });


            btnEdit.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(context, CommunityCreatePostActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("communityPostDataModel", communityList.get(position));
                intent.putExtra("allChannelsResponse", allChannelsResponse);
                onChannelAddedLauncher.launch(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });


            btnShare.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                shareCommunityFeed(position);
            });

            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
            bottomSheetDialog.setDismissWithAnimation(true);


        } else {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_post_other_user, rootView.findViewById(R.id.bottom_sheet_post_other_user));

            LinearLayout btnShare = sheetView.findViewById(R.id.btnShare);
            LinearLayout btnReport = sheetView.findViewById(R.id.btnReport);
            RelativeLayout btnCancel = sheetView.findViewById(R.id.btn_cancel);

            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

            btnShare.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                shareCommunityFeed(position);
            });

            btnReport.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
                flagJobIntent.putExtra("type", "flag_post");
                flagJobIntent.putExtra("flagImage", communityList.get(position).getImage_path());
                flagJobIntent.putExtra("flagTitle",  communityList.get(position).getPost_title());
                flagJobIntent.putExtra("flagId", String.valueOf(communityList.get(position).getId()));
                startActivity(flagJobIntent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });


            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();

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
        shareCommunityFeed(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onChannelSelectedClicked(CommunityChannelsAdapter communityChannelsAdapter, int position) {
        for (int i = 0; i < channelList.size(); i++) {
            channelList.get(i).setSelected(i == position);
        }
        communityChannelsAdapter.notifyDataSetChanged();
        progressDialog.show();
        requestForCommunityFeeds("Bearer " + sessionManagement.getToken(), String.valueOf(channelList.get(position).getId()), "channel");
    }

    @Override
    public void onEditChannelClicked(CommunityChannelsAdapter communityChannelsAdapter, int position) {
        showChannelEditDeleteBottomSheet(communityChannelsAdapter, position, channelList.get(position).getId() + "");
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

        channel.bind("NewPost", event ->
                requireActivity().runOnUiThread(() -> {
                    Gson gson = new Gson();
                    CommunityPusherPostDataModel pusherPostDataModel = gson.fromJson(event.getData(), CommunityPusherPostDataModel.class);
                    if (pusherPostDataModel != null) {
                        if (pusherPostDataModel.getCommunityPostDataModel() != null) {
                            communityList.add(0, pusherPostDataModel.getCommunityPostDataModel());
                            communityAdapter.notifyDataSetChanged();
                        }

                    }
                }));
        pusher.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showChannelEditDeleteBottomSheet(CommunityChannelsAdapter communityChannelsAdapter, int position, String channelId){
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_two_options, rootView.findViewById(R.id.bottom_sheet_option));

        LinearLayout btn_cancel = sheetView.findViewById(R.id.btn_cancel);

        LinearLayout btn_selectImage = sheetView.findViewById(R.id.btn_selectImage);
        TextView text_selectImage = sheetView.findViewById(R.id.text_selectImage);
        LinearLayout btn_selectVideo = sheetView.findViewById(R.id.btn_selectVideo);
        TextView text_selectVideo = sheetView.findViewById(R.id.text_selectVideo);

        btn_cancel.setOnClickListener(view1 -> bottomSheetDialog.dismiss());

        text_selectImage.setText(R.string.edit);
        text_selectVideo.setText(R.string.delete);

        btn_selectImage.setOnClickListener(v1 -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(context, CommunityNewChannelActivity.class);
            intent.putExtra("type", "edit");
            intent.putExtra("channelDataModel", channelList.get(position));
            onChannelAddedLauncher.launch(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        btn_selectVideo.setOnClickListener(v12 -> {
            bottomSheetDialog.dismiss();
            showDeleteChannelPopup(channelId, position, communityChannelsAdapter);
        });

        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteChannelPopup(String channelId, int position, CommunityChannelsAdapter communityChannelsAdapter) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(requireActivity()).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        headerText = "Delete Channel";
        areYouSure = "Are you sure you want to delete this channel";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            requestForDeleteChannel("Bearer " + sessionManagement.getToken(), channelId);
            channelList.remove(position);
            communityChannelsAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }
    private void requestForDeleteChannel(String authToken, String channelId) {
        Call<MainResponseModel> call = service.deleteChannel(authToken, channelId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (!status){
                            CustomCookieToast.showFailureToast(requireActivity(), response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(requireActivity(), response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), t.getLocalizedMessage());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDeletePostPopup(int position, String postId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(requireActivity()).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        headerText = "Delete Post";
        areYouSure = "Are you sure you want to delete this post";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            requestForDeletePost("Bearer " + sessionManagement.getToken(), postId);
            communityList.remove(position);
            communityAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }


    private void requestForDeletePost(String authToken, String postId) {
        Call<MainResponseModel> call = service.deletePost(authToken, postId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (!status){
                            CustomCookieToast.showFailureToast(requireActivity(), response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(requireActivity(), response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), t.getLocalizedMessage());
            }
        });
    }

    private void shareCommunityFeed(int position){
        String postUrl;
        if (communityList.get(position).getVideo_url() != null) {
            postUrl = communityList.get(position).getVideo_url();
        } else {
            postUrl = communityList.get(position).getImage_path();
        }
        Utilities.shareAppLink(context, postUrl);
    }

    ActivityResultLauncher<Intent> onChannelAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() !=null){
                requestForAllChannels("Bearer " + sessionManagement.getToken());
            }
        }
    });
}