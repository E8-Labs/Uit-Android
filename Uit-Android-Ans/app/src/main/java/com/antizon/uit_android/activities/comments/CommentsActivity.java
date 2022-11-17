package com.antizon.uit_android.activities.comments;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.community.CommentsAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.comments.CommentDataModel;
import com.antizon.uit_android.models.comments.CommentsResponseModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity implements CommentsAdapter.CommentsAdapterCallBack, SwipeRefreshLayout.OnRefreshListener {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog  progressDialog;

    RelativeLayout btnBack, btn_send, rl_reply_to;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerview_comments;
    List<CommentDataModel> commentsList;
    CommentsAdapter commentsAdapter;
    EditText edit_comment;
    TextView tv_replying_to;
    ImageView iv_close_reply;
    ImageView uitImage;
    RoundedImageView user_ProfileImage;

    Pusher pusher;
    String postId, commentId;

    boolean isReply = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Utilities.setWhiteBars(CommentsActivity.this);
        context = CommentsActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog  = new ProgressDialog(context);

        postId = getIntent().getStringExtra("postId");

        btnBack = findViewById(R.id.btnBack);
        recyclerview_comments = findViewById(R.id.recyclerview_comments);
        swipe = findViewById(R.id.swipe);
        edit_comment = findViewById(R.id.edit_comment);
        btn_send = findViewById(R.id.btn_send);
        uitImage = findViewById(R.id.uitImage);
        user_ProfileImage = findViewById(R.id.user_ProfileImage);
        rl_reply_to = findViewById(R.id.rl_reply_to);
        tv_replying_to = findViewById(R.id.tv_replying_to);
        iv_close_reply = findViewById(R.id.iv_close_reply);


        if (sessionManagement.getRole().equals("1")){
            uitImage.setVisibility(View.VISIBLE);
            user_ProfileImage.setVisibility(View.GONE);
        }else {
            uitImage.setVisibility(View.GONE);
            user_ProfileImage.setVisibility(View.VISIBLE);
            Utilities.loadImage(context, sessionManagement.getProfileImage(), user_ProfileImage);
        }

        btnBack.setOnClickListener(v -> onBackPressed());
        swipe.setOnRefreshListener(this);

        btn_send.setOnClickListener(v -> {
            String comment = edit_comment.getText().toString();
            if (!TextUtils.isEmpty(comment)){
                if (isReply){
                    requestForAddCommentReply("Bearer " + sessionManagement.getToken(), commentId, comment);
                    showReplyTo(false, "");
                }else {
                    requestForAddComment("Bearer " + sessionManagement.getToken(), postId, comment);
                }
            }
            edit_comment.setText("");
        });

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForPostComments("Bearer " + sessionManagement.getToken(), postId);

        setCommentsPusher();

        iv_close_reply.setOnClickListener(v -> {
            showReplyTo(false, "");
            isReply = false;
        });
    }

    private void showReplyTo(boolean showReplyTo, String commenterName) {
        if (showReplyTo) {
            rl_reply_to.setVisibility(VISIBLE);
            String etReplyText ="Replying to @" + commenterName;
            tv_replying_to.setText(etReplyText);
        } else {
            rl_reply_to.setVisibility(View.GONE);
            edit_comment.setText("");
        }
    }
    private void requestForPostComments(String authToken, String postId){
        Call<CommentsResponseModel> call = service.getPostComments(authToken, postId);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<CommentsResponseModel> call, @NotNull Response<CommentsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        commentsList = new ArrayList<>();

                        commentsList = response.body().getCommentsList();
                        showCommentsRecyclerview(recyclerview_comments, commentsList);

                    }else {
                        CustomCookieToast.showRequiredToast(CommentsActivity.this, response.body().getMessage());
                    }
                }else {
                    CustomCookieToast.showRequiredToast(CommentsActivity.this, response.message());
                }


            }

            @Override
            public void onFailure(@NotNull Call<CommentsResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showRequiredToast(CommentsActivity.this, t.getMessage());
            }
        });

    }

    private void showCommentsRecyclerview(RecyclerView recyclerView, List<CommentDataModel> commentsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(context, commentsList ,this);
        recyclerView.setAdapter(commentsAdapter);
    }

    @Override
    public void onLikeClicked(String commentId) {
        requestForLikeComment("Bearer " + sessionManagement.getToken(), commentId);
    }

    @Override
    public void onCommentRepliesClicked(int position) {
        requestForCommentReplies("Bearer " + sessionManagement.getToken(),String.valueOf(commentsList.get(position).getId()), position);
    }

    @Override
    public void onReplyToCommentClicked(int position) {
        if (commentsList.get(position).getUserModel() != null){
            commentId = commentsList.get(position).getId() + "";
            isReply = true;
            showReplyTo(true, commentsList.get(position).getUserModel().getName());
        }
    }


    private void requestForCommentReplies(String authToken, String commentId, int position) {
        Call<CommentsResponseModel> call = service.getCommentReplies(authToken, commentId);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<CommentsResponseModel> call, @NotNull Response<CommentsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        List<CommentDataModel> repliesList;

                        repliesList = response.body().getCommentsList();
                        if (!repliesList.isEmpty()){
                            commentsList.get(position).setRepliesList(repliesList);
                            commentsAdapter.notifyDataSetChanged();
                        }


                    }else {
                        CustomCookieToast.showRequiredToast(CommentsActivity.this, response.body().getMessage());
                    }
                }else {
                    CustomCookieToast.showRequiredToast(CommentsActivity.this, response.message());
                }


            }

            @Override
            public void onFailure(@NotNull Call<CommentsResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showRequiredToast(CommentsActivity.this, t.getMessage());
            }
        });

    }

    private void requestForLikeComment(String authToken, String postId) {
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<MainResponseModel> call = service.likeComment(authToken, postId);
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
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> swipe.setRefreshing(false), 400);
        requestForPostComments("Bearer " + sessionManagement.getToken(), postId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void requestForAddCommentReply(String authToken, String commentId, String message) {
        Call<MainResponseModel> call = service.addReply(authToken, commentId, message);
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

    private void requestForAddComment(String authToken, String postId, String message) {
        Call<MainResponseModel> call = service.addComment(authToken, postId, message);
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

    @SuppressLint("NotifyDataSetChanged")
    public void setCommentsPusher() {
        // pusher
        PusherOptions options = new PusherOptions();
        options.setCluster("us3").setUseTLS(false);
        pusher = new Pusher("785126dad3606ac09387", options);
        Channel channel = pusher.subscribe("Community");

        channel.bind("NewComment"+postId, event -> {
            Gson gson = new Gson();
            CommentDataModel comment = gson.fromJson(event.getData(), CommentDataModel.class);
            runOnUiThread(() -> {
                if (comment != null) {
                    if (commentsList.size()==0){
                        commentsList.add(comment);
                    }else {
                        commentsList.add(commentsList.size(), comment);
                    }
                    recyclerview_comments.smoothScrollToPosition(commentsList.size()-1);
                    commentsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "message is null", Toast.LENGTH_SHORT).show();
                }
            });
        });

        pusher.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }
}