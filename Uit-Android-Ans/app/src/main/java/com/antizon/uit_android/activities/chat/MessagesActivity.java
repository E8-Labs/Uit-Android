package com.antizon.uit_android.activities.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.FlagJobUserActivity;
import com.antizon.uit_android.adapters.chat.MessagesAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.chat.CreateChatResponse;
import com.antizon.uit_android.models.chat.GetConversationResponse;
import com.antizon.uit_android.models.chat.MessageModel;
import com.antizon.uit_android.models.chat.SendMessageResponse;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.FileUtils;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.wang.avi.AVLoadingIndicatorView;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {
    private static final int GALLERY_PERMISSION_CODE = 101;
    private static final int PICK_IMAGE = 103;

    Context context;
    GetDataService service;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView tvUsername, tvStatus;
    ImageView btnBack, ivUserProfile, image_add, btnMenu;
    EditText etMessage;
    RelativeLayout rlSend;
    AVLoadingIndicatorView loading;

    List<MessageModel> messagesList;
    MessagesAdapter adapter;
    String secondUserProfile, secondUsername;
    String userID, secondUserId;
    boolean isPusherAlreadySet = false;

    String picturePath;
    String chatId;
    SessionManagement sessionManagement;

    Pusher pusher;

    AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Utilities.setWhiteBars(MessagesActivity.this);
        context = MessagesActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_chat);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_color);

        btnBack = findViewById(R.id.btnBack);
        loading = findViewById(R.id.loading);
        tvUsername = findViewById(R.id.tv_username);
        tvStatus = findViewById(R.id.tv_status);
        ivUserProfile = findViewById(R.id.iv_user_profile);
        rlSend = findViewById(R.id.btn_send);
        etMessage = findViewById(R.id.edit_message);
        image_add = findViewById(R.id.image_add);
        recyclerView = findViewById(R.id.recyclerview_conversation);
        btnMenu = findViewById(R.id.btnMenu);

        getIntentData();

        loading.setVisibility(View.VISIBLE);
        getUserMessagesList("Bearer " + sessionManagement.getToken(), chatId);

        rlSend.setOnClickListener(v -> proceedToSendMessage());
        image_add.setOnClickListener(v -> checkGalleryPermission());

        btnBack.setOnClickListener(v -> back());
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        registerForContextMenu(btnMenu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_options_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnReportChat:
                Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
                flagJobIntent.putExtra("type", "flag_user");
                flagJobIntent.putExtra("flagImage", secondUserProfile);
                flagJobIntent.putExtra("flagTitle", secondUsername);
                flagJobIntent.putExtra("flagId", secondUserId);
                startActivity(flagJobIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                return true;
            case R.id.btnDeleteChat:
                showDeleteChatPopup(chatId);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void showDeleteChatPopup(String chatId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MessagesActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(MessagesActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        headerText = "Delete Chat";
        areYouSure = "Are you sure you want to delete this chat?";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            requestForDeleteChat("Bearer " + sessionManagement.getToken(), chatId);
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void requestForDeleteChat(String authToken, String postId) {
        Call<MainResponseModel> call = service.deleteChat(authToken, postId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void getIntentData() {
        userID = sessionManagement.getKeyId();

        chatId = getIntent().getStringExtra("CHAT_ID");
        secondUserId = getIntent().getStringExtra("second_user_id");
        secondUserProfile = getIntent().getStringExtra("second_user_picture");
        secondUsername = getIntent().getStringExtra("second_user_name");

        if (!TextUtils.isEmpty(secondUsername)) {
            tvUsername.setText(secondUsername);
        }
        Utilities.loadImage(context, secondUserProfile, ivUserProfile);
    }


    private void getUserMessagesList(String authToken, String chatId) {
        Call<GetConversationResponse> call = service.getUserConversation(authToken, chatId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<GetConversationResponse> call, @NotNull Response<GetConversationResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        messagesList = new ArrayList<>();

                        messagesList = response.body().getData();
                        setMessagesToRecyclerView(recyclerView, messagesList);
                        if (!isPusherAlreadySet) {
                            setMessagePusher();
                            isPusherAlreadySet = true;
                        }

                        if (messagesList.size() > 0){
                            etMessage.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> recyclerView.scrollToPosition(messagesList.size()-1));
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetConversationResponse> call, @NotNull Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMessagesToRecyclerView(RecyclerView recyclerView, List<MessageModel> messagesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessagesAdapter(context, messagesList, secondUserProfile, userID);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(messagesList.size()-1);
    }

    public void onRefresh() {
        if (adapter != null) {
            adapter.clear();
        }
        if (messagesList != null) {
            messagesList.clear();
        }
        getUserMessagesList("Bearer " + sessionManagement.getToken(), chatId);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void proceedToSendMessage() {
        String message = etMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            etMessage.setText("");
            proceedToSendMessage(message);
        }
    }

    private void proceedToSendMessage(String message) {
        if (messagesList == null || messagesList.isEmpty()) {
            createChat(message);
        } else {
            sendMessage(message);
        }

    }

    private void sendMessage(String message) {
        Call<SendMessageResponse> call = service.sendMessage("Bearer " + sessionManagement.getToken(), chatId, message);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SendMessageResponse> call, @NotNull Response<SendMessageResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().isStatus()) {
                            Toast.makeText(context, "Unable to send the message right now!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(context, "unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SendMessageResponse> call, @NotNull Throwable t) {
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createChat(String message) {
        Call<CreateChatResponse> call = service.createChat("Bearer " + sessionManagement.getToken(), secondUserId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CreateChatResponse> call, @NonNull Response<CreateChatResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        chatId = response.body().getData().getId();
                    }
                    sendMessage(message);
                } else {
                    Toast.makeText(context, "unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkGalleryPermission() {
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //Permission not granted request it
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            //show popup for run time permission
            requestPermissions(permissions, GALLERY_PERMISSION_CODE);
        } else {
            //permission already granted
            pickImageFromGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission was granted
                pickImageFromGallery();
            } else {
                //permission was denied
                Toast.makeText(context, "Permission were denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityIfNeeded(intent, PICK_IMAGE);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri imageUri = data.getData();
                Uri finalUri = imageUri;

                String uri = imageUri.toString();
                if (uri.contains("content://com.google.android.apps.photos")) {
                    Bitmap bitmap = FileUtils.getBitmapFromUri(context, imageUri);
                    @SuppressLint("SimpleDateFormat")
                    String name = "JPEG" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'").format(new Date());
                    finalUri = FileUtils.insertImage(context, getContentResolver(), bitmap, name, "");
                }
                picturePath = FileUtils.getPath(context, finalUri);
                if (!TextUtils.isEmpty(picturePath)) {
                    goNext(picturePath);
                } else {
                    Toast.makeText(context, "Picture Path is Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void goNext(String picturePath) {
        sendMessageWithImage(picturePath);
    }


    private void sendMessageWithImage(String imagePath) {

        File image = new File(imagePath);
        RequestBody requestBodyImage = RequestBody.create(image, MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("chat_image", image.getName(), requestBodyImage);

        RequestBody messageBody = RequestBody.create("send an image", MediaType.parse("text/plain"));
        RequestBody from = RequestBody.create(chatId, MediaType.parse("text/plain"));

        Call<SendMessageResponse> call = service.sendMessageWithImage("Bearer " + sessionManagement.getToken(), from, messageBody, imagePart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SendMessageResponse> call, @NotNull Response<SendMessageResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        final MessageModel data = response.body().getData();
                        addNewMessage(data);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SendMessageResponse> call, @NotNull Throwable t) {
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setMessagePusher() {
        // pusher
        PusherOptions options = new PusherOptions();
        options.setCluster("us3").setUseTLS(false);
         pusher = new Pusher("785126dad3606ac09387", options);
        Channel channel = pusher.subscribe("Chat");

        channel.bind("NewMessage"+chatId, event -> {
            Gson gson = new Gson();
            MessageModel message = gson.fromJson(event.getData(), MessageModel.class);
            runOnUiThread(() -> {
                if (message != null) {

                    messagesList.add(message);
                    setMessagesToRecyclerView(recyclerView, messagesList);
                } else {
                    Toast.makeText(context, "message is empty", Toast.LENGTH_SHORT).show();
                }
            });
        });

        pusher.connect();
    }

    private void back() {
        pusher.disconnect();
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void addNewMessage(MessageModel message) {
        adapter.addNewItem(message);
        recyclerView.scrollToPosition(0);
    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}