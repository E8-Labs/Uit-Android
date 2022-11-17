package com.antizon.uit_android.activities.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.community.HorizontalChannelsAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.AllChannelsResponseModel;
import com.antizon.uit_android.models.community.ChannelDataModel;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.FileUtils;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityCreatePostActivity extends AppCompatActivity implements HorizontalChannelsAdapter.HorizontalChannelsAdapterCallBack {
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;

    private final int PERMISSION_CODE = 1001;
    private final int VIDEO_REQUEST = 103;
    private final int SELECT_PICTURE = 500;


    RelativeLayout layout_backArrow, layout_postTitle, layout_description, layout_URL, layout_post, layout_imageCancel, btn_selectImage;
    EditText edit_URL, edit_description, edit_postTitle;
    RoundedImageView image_uploadProfile;

    BottomSheetDialog bottomSheetDialog;

    RecyclerView recyclerview_selectChannel;
    List<ChannelDataModel> channelsList;
    HorizontalChannelsAdapter horizontalChannelsAdapter;
    AllChannelsResponseModel allChannelsResponse;

    String selectedMediaType, mediaPath = "", type;
    File videoImageFile;

    SessionManagement sessionManagement;

    CommunityPostDataModel communityPostDataModel;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_create_post);
        Utilities.setWhiteBars(CommunityCreatePostActivity.this);
        context = CommunityCreatePostActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(CommunityCreatePostActivity.this);

        sessionManagement = new SessionManagement(context);

        type = getIntent().getStringExtra("type");
        allChannelsResponse = getIntent().getParcelableExtra("allChannelsResponse");
        if (type.equals("edit")) {
            communityPostDataModel = getIntent().getParcelableExtra("communityPostDataModel");
        }


        layout_backArrow = findViewById(R.id.layout_backArrow);
        layout_postTitle = findViewById(R.id.layout_postTitle);
        layout_description = findViewById(R.id.layout_description);
        layout_URL = findViewById(R.id.layout_URL);
        layout_post = findViewById(R.id.layout_post);
        edit_postTitle = findViewById(R.id.edit_postTitle);
        edit_description = findViewById(R.id.edit_description);
        edit_URL = findViewById(R.id.edit_URL);
        recyclerview_selectChannel = findViewById(R.id.recyclerview_selectChannel);
        image_uploadProfile = findViewById(R.id.image_uploadProfile);
        layout_imageCancel = findViewById(R.id.layout_imageCancel);
        btn_selectImage = findViewById(R.id.btn_selectImage);

        layout_backArrow.setOnClickListener(v -> onBackPressed());

        layout_imageCancel.setOnClickListener(v -> {
            mediaPath = "";
            image_uploadProfile.setVisibility(View.GONE);
            layout_imageCancel.setVisibility(View.GONE);
            btn_selectImage.setVisibility(View.VISIBLE);
        });

        channelsList = new ArrayList<>();
        channelsList = allChannelsResponse.getChannelsList();


        for (int i = 0; i < channelsList.size(); i++) {
            channelsList.get(i).setSelected(false);
        }
        setChannelsRecyclerview(recyclerview_selectChannel, channelsList);


        if (type.equals("edit")) {
            if (communityPostDataModel.getVideo_url() != null) {
                selectedMediaType = "video";
            } else {
                selectedMediaType = "image";
            }

            image_uploadProfile.setVisibility(View.VISIBLE);
            layout_imageCancel.setVisibility(View.VISIBLE);
            btn_selectImage.setVisibility(View.GONE);
            Utilities.loadImage(context, communityPostDataModel.getImage_path(), image_uploadProfile);

            for (int i = 0; i < channelsList.size(); i++) {
                for (int j = 0; j < communityPostDataModel.getChannelsList().size(); j++) {
                    if (channelsList.get(i).getId() == communityPostDataModel.getChannelsList().get(j).getId()) {
                        channelsList.get(i).setSelected(true);
                    }
                }
            }

            horizontalChannelsAdapter.notifyDataSetChanged();

            edit_postTitle.setText(communityPostDataModel.getPost_title());
            edit_description.setText(communityPostDataModel.getPost_description());
            edit_URL.setText(communityPostDataModel.getPost_url());

        }

        btn_selectImage.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_two_options, findViewById(R.id.bottom_sheet_option));

            LinearLayout btn_cancel = sheetView.findViewById(R.id.btn_cancel);
            LinearLayout btn_selectImage = sheetView.findViewById(R.id.btn_selectImage);
            LinearLayout btn_selectVideo = sheetView.findViewById(R.id.btn_selectVideo);

            btn_cancel.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            btn_selectImage.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                selectedMediaType = "image";
                checkGalleryPermissions(selectedMediaType);
            });

            btn_selectVideo.setOnClickListener(v12 -> {
                bottomSheetDialog.dismiss();
                selectedMediaType = "video";
                checkGalleryPermissions(selectedMediaType);
            });

            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        });

        layout_post.setOnClickListener(v -> {
            String postTitle = edit_postTitle.getText().toString();
            String description = edit_description.getText().toString();
            String postUrl = edit_URL.getText().toString();

            boolean isChannelSelected = false;

            for (int i = 0; i < channelsList.size(); i++) {
                if (channelsList.get(i).isSelected()) {
                    isChannelSelected = true;
                    break;
                }
            }

            if (type.equals("edit")) {
                if (mediaPath.isEmpty()) {
                    if (postTitle.isEmpty()) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter post title");
                    } else if (description.isEmpty()) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter description");
                    } else if (!isChannelSelected) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please select minimum one channel");
                    } else if (!postUrl.isEmpty() && !Utilities.isValidUrl(postUrl)) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter proper company url");
                    } else {
                        Utilities.hideKeyboard(edit_description, context);
                        progressDialog.setMessage("updating post...");
                        progressDialog.show();
                        requestForUpdatePostInfo(communityPostDataModel.getId() + "", postTitle, description, postUrl, channelsList);
                    }
                } else {
                    if (postTitle.isEmpty()) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter post title");
                    } else if (description.isEmpty()) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter description");
                    } else if (!isChannelSelected) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please select minimum one channel");
                    } else if (!postUrl.isEmpty() && !Utilities.isValidUrl(postUrl)) {
                        CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter proper company url");
                    } else {
                        Utilities.hideKeyboard(edit_description, context);
                        progressDialog.setMessage("updating post...");
                        progressDialog.show();
                        if (selectedMediaType.equals("image")) {
                            requestForUpdatePostImage(communityPostDataModel.getId() + "", postTitle, description, postUrl, channelsList, mediaPath);
                        } else {
                            requestForUpdatePostVideo(communityPostDataModel.getId() + "", postTitle, description, postUrl, channelsList, mediaPath, videoImageFile);
                        }

                    }
                }
            } else {
                if (mediaPath.isEmpty()) {
                    CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please select image or video");
                } else if (postTitle.isEmpty()) {
                    CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter post title");
                } else if (description.isEmpty()) {
                    CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter description");
                } else if (!isChannelSelected) {
                    CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please select minimum one channel");
                } else if (!postUrl.isEmpty() && !Utilities.isValidUrl(postUrl)) {
                    CustomCookieToast.showRequiredToast(CommunityCreatePostActivity.this, "Please enter proper company url");
                } else {
                    Utilities.hideKeyboard(edit_description, context);
                    progressDialog.setMessage("uploading post...");
                    progressDialog.show();
                    if (selectedMediaType.equals("image")) {
                        requestForUploadPostImage(postTitle, description, postUrl, channelsList, mediaPath);
                    } else {
                        requestForUploadPostVideo(postTitle, description, postUrl, channelsList, mediaPath, videoImageFile);
                    }

                }
            }
        });

    }


    private void requestForUploadPostImage(String post_title, String post_description, String post_url, List<ChannelDataModel> channelsList, String mediaPath) {
        List<String> selectedIds = new ArrayList<>();

        for (int i = 0; i < channelsList.size(); i++) {
            if (channelsList.get(i).isSelected()) {
                selectedIds.add(channelsList.get(i).getId() + "");
            }
        }

        RequestBody[] channelsRequestBody = new RequestBody[selectedIds.size()];
        for (int i = 0; i < selectedIds.size(); i++) {
            channelsRequestBody[i] = RequestBody.create(selectedIds.get(i), MediaType.parse("text/plain"));
        }

        RequestBody postTitleBody = RequestBody.create(post_title, MediaType.parse("text/plain"));
        RequestBody postDescriptionBody = RequestBody.create(post_description, MediaType.parse("text/plain"));
        RequestBody postUrlBody = RequestBody.create(post_url, MediaType.parse("text/plain"));


        File image = new File(mediaPath);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part postMediaPart = MultipartBody.Part.createFormData("post_image", image.getName(), requestBodyPostMedia);


        Call<MainResponseModel> call = service.uploadImagePostForCommunity("Bearer " + sessionManagement.getToken(), postTitleBody, postDescriptionBody, postUrlBody, channelsRequestBody, postMediaPart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestForUpdatePostImage(String postId, String post_title, String post_description, String post_url, List<ChannelDataModel> channelsList, String mediaPath) {
        List<String> selectedIds = new ArrayList<>();

        for (int i = 0; i < channelsList.size(); i++) {
            if (channelsList.get(i).isSelected()) {
                selectedIds.add(channelsList.get(i).getId() + "");
            }
        }

        RequestBody[] channelsRequestBody = new RequestBody[selectedIds.size()];
        for (int i = 0; i < selectedIds.size(); i++) {
            channelsRequestBody[i] = RequestBody.create(selectedIds.get(i), MediaType.parse("text/plain"));
        }

        RequestBody postIdBody = RequestBody.create(postId, MediaType.parse("text/plain"));
        RequestBody postTitleBody = RequestBody.create(post_title, MediaType.parse("text/plain"));
        RequestBody postDescriptionBody = RequestBody.create(post_description, MediaType.parse("text/plain"));
        RequestBody postUrlBody = RequestBody.create(post_url, MediaType.parse("text/plain"));


        File image = new File(mediaPath);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part postMediaPart = MultipartBody.Part.createFormData("post_image", image.getName(), requestBodyPostMedia);


        Call<MainResponseModel> call = service.updatePostImageForCommunity("Bearer " + sessionManagement.getToken(), postIdBody, postTitleBody, postDescriptionBody, postUrlBody, channelsRequestBody, postMediaPart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestForUploadPostVideo(String post_title, String post_description, String post_url, List<ChannelDataModel> channelsList, String videoPath, File videoImagePath) {
        List<String> selectedIds = new ArrayList<>();

        for (int i = 0; i < channelsList.size(); i++) {
            if (channelsList.get(i).isSelected()) {
                selectedIds.add(channelsList.get(i).getId() + "");
            }
        }

        RequestBody[] channelsRequestBody = new RequestBody[selectedIds.size()];
        for (int i = 0; i < selectedIds.size(); i++) {
            channelsRequestBody[i] = RequestBody.create(selectedIds.get(i), MediaType.parse("text/plain"));
        }

        RequestBody postTitleBody = RequestBody.create(post_title, MediaType.parse("text/plain"));
        RequestBody postDescriptionBody = RequestBody.create(post_description, MediaType.parse("text/plain"));
        RequestBody postUrlBody = RequestBody.create(post_url, MediaType.parse("text/plain"));


        File videoFile = new File(videoPath);
        RequestBody requestBodyVideo = RequestBody.create(videoFile, MediaType.parse("*/*"));
        MultipartBody.Part videoMediaPart = MultipartBody.Part.createFormData("post_video", videoFile.getName(), requestBodyVideo);

        RequestBody requestBodyPostImage = RequestBody.create(videoImagePath, MediaType.parse("*/*"));
        MultipartBody.Part imageMediaPart = MultipartBody.Part.createFormData("post_image", videoImagePath.getName(), requestBodyPostImage);


        Call<MainResponseModel> call = service.uploadVideoPostForCommunity("Bearer " + sessionManagement.getToken(), postTitleBody, postDescriptionBody, postUrlBody, channelsRequestBody, videoMediaPart, imageMediaPart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestForUpdatePostVideo(String postId, String post_title, String post_description, String post_url, List<ChannelDataModel> channelsList, String videoPath, File videoImagePath) {
        List<String> selectedIds = new ArrayList<>();

        for (int i = 0; i < channelsList.size(); i++) {
            if (channelsList.get(i).isSelected()) {
                selectedIds.add(channelsList.get(i).getId() + "");
            }
        }

        RequestBody[] channelsRequestBody = new RequestBody[selectedIds.size()];
        for (int i = 0; i < selectedIds.size(); i++) {
            channelsRequestBody[i] = RequestBody.create(selectedIds.get(i), MediaType.parse("text/plain"));
        }

        RequestBody postIdBody = RequestBody.create(postId, MediaType.parse("text/plain"));
        RequestBody postTitleBody = RequestBody.create(post_title, MediaType.parse("text/plain"));
        RequestBody postDescriptionBody = RequestBody.create(post_description, MediaType.parse("text/plain"));
        RequestBody postUrlBody = RequestBody.create(post_url, MediaType.parse("text/plain"));


        File videoFile = new File(videoPath);
        RequestBody requestBodyVideo = RequestBody.create(videoFile, MediaType.parse("*/*"));
        MultipartBody.Part videoMediaPart = MultipartBody.Part.createFormData("post_video", videoFile.getName(), requestBodyVideo);

        RequestBody requestBodyPostImage = RequestBody.create(videoImagePath, MediaType.parse("*/*"));
        MultipartBody.Part imageMediaPart = MultipartBody.Part.createFormData("post_image", videoImagePath.getName(), requestBodyPostImage);


        Call<MainResponseModel> call = service.updateVideoPostForCommunity("Bearer " + sessionManagement.getToken(), postIdBody, postTitleBody, postDescriptionBody, postUrlBody, channelsRequestBody, videoMediaPart, imageMediaPart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestForUpdatePostInfo(String postId, String post_title, String post_description, String post_url, List<ChannelDataModel> channelsList) {
        List<String> selectedIds = new ArrayList<>();

        for (int i = 0; i < channelsList.size(); i++) {
            if (channelsList.get(i).isSelected()) {
                selectedIds.add(channelsList.get(i).getId() + "");
            }
        }

        Call<MainResponseModel> call = service.updatePostInfo("Bearer " + sessionManagement.getToken(), postId, post_title, post_description, post_url, selectedIds);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setChannelsRecyclerview(RecyclerView recyclerView, List<ChannelDataModel> channelsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        horizontalChannelsAdapter = new HorizontalChannelsAdapter(context, channelsList, this);
        recyclerView.setAdapter(horizontalChannelsAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position) {
        ChannelDataModel channelModel = channelsList.get(position);
        channelModel.setSelected(!channelModel.isSelected());
        horizontalChannelsAdapter.notifyDataSetChanged();

    }

    private void checkGalleryPermissions(String type) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            if (TextUtils.equals(type, "image")) {
                selectImageFromGallery();
            } else if (TextUtils.equals(type, "video")) {
                openVideos();
            }
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO};
            requestPermissions(permissions, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && permissions.length == 4) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                if (TextUtils.equals(selectedMediaType, "image")) {
                    selectImageFromGallery();
                } else if (TextUtils.equals(selectedMediaType, "video")) {
                    openVideos();
                }
            } else {
                Toast.makeText(context, "Permissions are denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityIfNeeded(photoPickerIntent, SELECT_PICTURE);
    }

    private void openVideos() {
        Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
        videoPickerIntent.setType("video/*");
        startActivityIfNeeded(videoPickerIntent, VIDEO_REQUEST);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            mediaPath = FileUtils.getPath(context, selectedMediaUri);

            image_uploadProfile.setVisibility(View.VISIBLE);
            layout_imageCancel.setVisibility(View.VISIBLE);
            btn_selectImage.setVisibility(View.GONE);
            if (mediaPath != null) {
                if (requestCode == SELECT_PICTURE) {
                    image_uploadProfile.setImageURI(selectedMediaUri);
                } else if (requestCode == VIDEO_REQUEST) {
                    Bitmap videoBitMap = getVideoThumbnailFromUri(selectedMediaUri);
                    try {
                        videoImageFile = FileUtils.createFileFromBitmap(context, videoBitMap);
                        image_uploadProfile.setImageBitmap(videoBitMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private Bitmap getVideoThumbnailFromUri(Uri uri) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                try {
                    mediaMetadataRetriever.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}