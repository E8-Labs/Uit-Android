package com.antizon.uit_android.activities.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.ChannelDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.FileUtils;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityNewChannelActivity extends AppCompatActivity {
    private final int PERMISSION_CODE = 1001;
    private final int SELECT_PICTURE = 1001;
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    TextView text_newChannel, text_selectImage;
    RoundedImageView channelImage;
    RelativeLayout layout_cancel, layout_save;
    EditText edit_channelTitle;

    String mediaPath = "", type;

    ChannelDataModel channelDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_new_channel);
        Utilities.setWhiteBars(CommunityNewChannelActivity.this);
        context = CommunityNewChannelActivity.this;
        type = getIntent().getStringExtra("type");

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        text_newChannel = findViewById(R.id.text_newChannel);
        text_selectImage = findViewById(R.id.text_selectImage);
        channelImage = findViewById(R.id.channelImage);
        layout_cancel = findViewById(R.id.layout_cancel);
        layout_save = findViewById(R.id.layout_save);
        edit_channelTitle = findViewById(R.id.edit_channelTitle);

        if (type.equals("edit")){
            channelDataModel = getIntent().getParcelableExtra("channelDataModel");
            edit_channelTitle.setText(channelDataModel.getName());
            Utilities.loadImage(context, channelDataModel.getImage_path(), channelImage);
        }

        layout_save.setOnClickListener(v -> {
            String channelTitle = edit_channelTitle.getText().toString();

            if (type.equals("edit")){
                if (mediaPath.isEmpty()){
                    Utilities.hideKeyboard(edit_channelTitle, context);
                    progressDialog.setMessage("updating channel...");
                    progressDialog.show();
                    requestForUpdateCommunityChannelName(channelDataModel.getId() + "", channelTitle);
                }else {
                    Utilities.hideKeyboard(edit_channelTitle, context);
                    progressDialog.setMessage("updating channel...");
                    progressDialog.show();
                    requestForUpdateCommunityChannel(channelDataModel.getId() + "", channelTitle, mediaPath);
                }
            }else {
                if (mediaPath.isEmpty()){
                    CustomCookieToast.showRequiredToast(CommunityNewChannelActivity.this, "Please select image or video");
                }else if (channelTitle.isEmpty()){
                    CustomCookieToast.showRequiredToast(CommunityNewChannelActivity.this, "Please enter channel title");
                }else {
                    Utilities.hideKeyboard(edit_channelTitle, context);
                    progressDialog.setMessage("adding channel...");
                    progressDialog.show();
                    requestForAddCommunityChannel(channelTitle, mediaPath);
                }
            }

        });

        layout_cancel.setOnClickListener(v -> onBackPressed());

        text_selectImage.setOnClickListener(v -> checkGalleryPermissions());
    }

    private void checkGalleryPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            selectImageFromGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,};
            requestPermissions(permissions, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && permissions.length == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED ) {
                selectImageFromGallery();
            } else {
                Toast.makeText(context, "Permissions are denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImageFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityIfNeeded(photoPickerIntent, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedMediaUri = data.getData();
            mediaPath = FileUtils.getPath(context, selectedMediaUri);
            if (mediaPath != null) {
                channelImage.setImageURI(selectedMediaUri);
            }
        }
    }

    private void requestForAddCommunityChannel(String channelName, String mediaPath) {
        RequestBody postTitleBody = RequestBody.create(channelName, MediaType.parse("text/plain"));
        File image = new File(mediaPath);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part postMediaPart = MultipartBody.Part.createFormData("channel_image", image.getName(), requestBodyPostMedia);

        Call<MainResponseModel> call = service.addCommunityChannel("Bearer " + sessionManagement.getToken(), postTitleBody, postMediaPart);

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

    private void requestForUpdateCommunityChannelName(String channelId, String channelName) {
        Call<MainResponseModel> call = service.updateCommunityChannelName("Bearer " + sessionManagement.getToken(), channelId, channelName);
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

    private void requestForUpdateCommunityChannel(String channelId, String channelName, String mediaPath) {
        RequestBody postChannelIdBody = RequestBody.create(channelId, MediaType.parse("text/plain"));
        RequestBody postTitleBody = RequestBody.create(channelName, MediaType.parse("text/plain"));

        File image = new File(mediaPath);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part postMediaPart = MultipartBody.Part.createFormData("channel_image", image.getName(), requestBodyPostMedia);

        Call<MainResponseModel> call = service.updateCommunityChannelMedia("Bearer " + sessionManagement.getToken(), postChannelIdBody, postTitleBody, postMediaPart);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


}