package com.antizon.uit_android.uit_members.welcome;

import androidx.annotation.Nullable;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

import java.io.IOException;

public class ProfilePictureActivity extends BaseActivity {

    private static final String TAG = ProfilePictureActivity.class.getSimpleName();
    TextView changePicture, next;
    ImageView companyLogoImage;
    String encodedImageData = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        Utilities.setCustomStatusAndNavColor(ProfilePictureActivity.this, R.color.white, R.color.white);

        setIds();
        initialize();
        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        next = findViewById(R.id.next);
        changePicture = findViewById(R.id.changePicture);
        companyLogoImage = findViewById(R.id.companyLogoImage);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(v -> {

            if (encodedImageData.isEmpty()) {
                CustomCookieToast.showRequiredToast(ProfilePictureActivity.this, "Please select your profile picture");
            }else {
                String profilePic = "" + uri;
                Log.d(TAG, "onClick: profilePic: " + profilePic);
                Intent intent = new Intent(ProfilePictureActivity.this, UitMemberFullNameActivity.class);
                intent.putExtra("profilePic", uri.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        companyLogoImage.setOnClickListener(view -> pickImage(ProfilePictureActivity.this));

        changePicture.setOnClickListener(view -> pickImage(ProfilePictureActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);
                    loadProfile(ProfilePictureActivity.this, uri.toString(), companyLogoImage);

                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}