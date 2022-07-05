package com.antizon.uit_android.activities.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

public class CommunityNewChannelActivity extends AppCompatActivity {
    Context context;
    TextView text_newChannel, text_changeImage;
    RoundedImageView image_office;
    RelativeLayout layout_cancel, layout_save;
    EditText edit_channelTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_new_channel);
        Utilities.setWhiteBars(CommunityNewChannelActivity.this);
        context = CommunityNewChannelActivity.this;
        
        text_newChannel = findViewById(R.id.text_newChannel);
        text_changeImage = findViewById(R.id.text_changeImage);
        image_office = findViewById(R.id.image_office);
        layout_cancel = findViewById(R.id.layout_cancel);
        layout_save = findViewById(R.id.layout_save);
        edit_channelTitle = findViewById(R.id.edit_channelTitle);


        layout_save.setOnClickListener(v -> {
            String channelTitle = edit_channelTitle.getText().toString();
            if (channelTitle.isEmpty()){
                CustomCookieToast.showRequiredToast(CommunityNewChannelActivity.this, "Please enter channel title");
            }else {
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });

        layout_cancel.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}