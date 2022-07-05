package com.antizon.uit_android.company.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.adapters.chat.InboxAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.chat.GetInboxResponse;
import com.antizon.uit_android.models.chat.InboxModel;
import com.antizon.uit_android.models.chat.InboxUser;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, InboxAdapter.InboxAdapterCallBack {
    GetDataService service;
    Context context;

    SessionManagement sessionManagement;

    List<InboxModel> inboxList;
    RecyclerView recyclerview_inbox;
    InboxAdapter adapter;

    SwipeRefreshLayout swipe;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        recyclerview_inbox = rootView.findViewById(R.id.recyclerview_inbox);
        swipe = rootView.findViewById(R.id.swipe);

        sessionManagement = new SessionManagement(context);

        getUserConversation("Bearer " + sessionManagement.getToken());

        swipe.setOnRefreshListener(this);
        return rootView;
    }

    private void getUserConversation(String authToken) {
        Call<GetInboxResponse> call = service.getUserInboxList(authToken);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<GetInboxResponse> call, @NotNull Response<GetInboxResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        inboxList = new ArrayList<>();
                        inboxList = response.body().getData();
                        showConversations(recyclerview_inbox, inboxList);
                    } else {
                        CustomCookieToast.showFailureToast(requireActivity(), response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetInboxResponse> call, @NotNull Throwable t) {
                CustomCookieToast.showFailureToast(requireActivity(), t.getMessage());
            }
        });
    }

    private void showConversations(RecyclerView recyclerView, List<InboxModel> inboxList) {
        if (!inboxList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new InboxAdapter(context, inboxList, String.valueOf(sessionManagement.getKeyId()), this);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
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
        getUserConversation("Bearer " + sessionManagement.getToken());
    }

    @Override
    public void onItemClick(InboxModel inboxModel) {
        InboxUser user = inboxModel.getUsers().get(1);

        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", String.valueOf(inboxModel.getId()));
        intent.putExtra("second_user_id", String.valueOf(user.getUserId()));
        intent.putExtra("second_user_picture", user.getProfileImage());
        intent.putExtra("second_user_name", user.getName());
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}