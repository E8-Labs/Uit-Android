package com.antizon.uit_android.uit_admin.community;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.ChannelListAdapter;
import com.antizon.uit_android.generic.model.ModelChannelList;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllChannel extends BaseActivity {
    private static final String TAG = AllChannel.class.getSimpleName();

    ImageView plus,cut,reminder;
    TextView newChannel,senior,save;
    EditText title;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    RecyclerView allChannelRecyclerView;

    ChannelListAdapter channelListAdapter;
    private List<ModelChannelList> list;
    private ModelChannelList dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_all_channel);
        setIds();
        initialize();
        setListener();
        setUpAllChannelRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: url: " + AppConstants.GET_CHANNEL_LIST);
        Log.d(TAG, "onResume: token: " + sessionManagement.getToken());
        sendServerRequestGET(AppConstants.GET_CHANNEL_LIST, sessionManagement.getToken());

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        plus = findViewById(R.id.plus);
        allChannelRecyclerView = findViewById(R.id.allChannelRecyclerView);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(AllChannel.this);
        sessionManagement = new SessionManagement(AllChannel.this);
        list = new ArrayList<>();
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newChannelBottomSheet();

            }
        });

    }

    void setUpAllChannelRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllChannel.this);
        allChannelRecyclerView.setLayoutManager(linearLayoutManager);

        channelListAdapter = new ChannelListAdapter(list, AllChannel.this);
        allChannelRecyclerView.setAdapter(channelListAdapter);
    }
    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    list.clear();

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject dataObject = dataArray.getJSONObject(i);
                        int id = dataObject.getInt("id");
                        Log.d(TAG, "onResponse: id " + id);
                        String name = dataObject.getString("name");
                        Log.d(TAG, "onResponse: name " + name);
                        String image_path = dataObject.getString("image_path");
                        Log.d(TAG, "onResponse: image_path " + image_path);

                        dataModel = new ModelChannelList();
                        dataModel.setId(id);
                        dataModel.setName(name);
                        dataModel.setImage_path(image_path);

                        list.add(dataModel);

                    }
                }
            }
            channelListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }

    void newChannelBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.new_channel_bottomsheet);
        cut = bottomSheetDialog.findViewById(R.id.cut);
        reminder = bottomSheetDialog.findViewById(R.id.reminder);
        newChannel = bottomSheetDialog.findViewById(R.id.newChannel);
        senior = bottomSheetDialog.findViewById(R.id.senior);
        save = bottomSheetDialog.findViewById(R.id.save);
        title = bottomSheetDialog.findViewById(R.id.title);




        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}