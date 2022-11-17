package com.antizon.uit_android.notifications.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.activities.community.CommunityPostDetailActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.community.CommunityPostDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.adapters.NotificationAdapter;
import com.antizon.uit_android.notifications.models.GetNotificationsResponse;
import com.antizon.uit_android.notifications.models.NotificationModel;
import com.antizon.uit_android.utilities.SimpleScrollListener;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity implements NotificationAdapter.NotificationAdapterCallBack {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notificationList, newNotificationList;

    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Utilities.setCustomStatusAndNavColor(NotificationsActivity.this, R.color.white_dash, R.color.white_dash);
        context = NotificationsActivity.this;

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog  = new ProgressDialog(context);

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.rv_notifications);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForNotificationsList("Bearer " + sessionManagement.getToken());

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void requestForNotificationsList(String authToken) {
        Call<GetNotificationsResponse> call = service.getNotifications(authToken, "0");

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetNotificationsResponse> call, @NonNull Response<GetNotificationsResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        notificationList = new ArrayList<>();
                        newNotificationList = new ArrayList<>();

                        notificationList = response.body().getData();
                        showNotificationsRecyclerview(recyclerView, notificationList);

                        if (notificationList != null){
                            if (notificationList.size() >= 15) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                                isLoading = false;
                            }
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetNotificationsResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotificationsRecyclerview(RecyclerView recyclerView, List<NotificationModel> notificationList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationAdapter = new NotificationAdapter(context, notificationList, this);
        recyclerView.setAdapter(notificationAdapter);

        recyclerView.addOnScrollListener(new SimpleScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    loadMoreItemsForPagination(notificationAdapter);
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

        });

    }


    private void loadMoreItemsForPagination(NotificationAdapter notificationAdapter) {
        Call<GetNotificationsResponse> call = service.getNotifications("Bearer " + sessionManagement.getToken(), String.valueOf(notificationList.size()));
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetNotificationsResponse> call, @NotNull Response<GetNotificationsResponse> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newNotificationList = new ArrayList<>();

                        newNotificationList = response.body().getData();
                        notificationList.addAll(newNotificationList);

                        if (newNotificationList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        notificationAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetNotificationsResponse> call, @NotNull Throwable t) {
                isLoading = false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onNotificationClicked(int position, NotificationModel notificationModel) {
        notificationList.get(position).setSeen(true);
        notificationAdapter.notifyDataSetChanged();
        requestForSeenNotification("Bearer " + sessionManagement.getToken(), notificationModel.getId() +"");

        if (notificationModel.getNotificationType() == 1){
            GenericApplicantDataModel fromUser = notificationModel.getFromUser();
            if (fromUser != null && notificationModel.getChatMessage() != null){
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra("CHAT_ID", notificationModel.getChatMessage().getChatId());
                intent.putExtra("second_user_id", String.valueOf(fromUser.getUser_id()));
                intent.putExtra("second_user_picture", fromUser.getProfile_image());
                intent.putExtra("second_user_name", fromUser.getName());
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        } else if (notificationModel.getNotificationType() == 14) {
             CommunityPostDataModel post = notificationModel.getPost();

            if (post != null ){
                Intent intent = new Intent(context, CommunityPostDetailActivity.class);
                intent.putExtra("postData", post);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }

        }
    }


    private void requestForSeenNotification(String authToken, String notificationId) {
        Call<MainResponseModel> call = service.seenNotification(authToken, notificationId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}