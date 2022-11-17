package com.antizon.uit_android.company.welcome;

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
import java.util.Objects;


public class UploadCompanyLogoActivity extends BaseActivity {

    private static final String TAG = UploadCompanyLogoActivity.class.getSimpleName();
    TextView changePicture, next;
    ImageView companyLogoImage;
    String encodedImageData = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_company_logo);
        Utilities.setWhiteBars(UploadCompanyLogoActivity.this);

        initViews();
    }

    void initViews() {
        changePicture = findViewById(R.id.changePicture);
        companyLogoImage = findViewById(R.id.companyLogoImage);
        next = findViewById(R.id.next);

        companyLogoImage.setOnClickListener(view -> pickImage(UploadCompanyLogoActivity.this));

        changePicture.setOnClickListener(view -> pickImage(UploadCompanyLogoActivity.this));


        next.setOnClickListener(v -> {
            if (encodedImageData.isEmpty()) {
                CustomCookieToast.showRequiredToast(UploadCompanyLogoActivity.this, "Please select your company logo");
            } else {
                String profilePic = "" + uri;
                Log.d(TAG, "onClick: profilePic: " + profilePic);
                Intent intent = new Intent(UploadCompanyLogoActivity.this, CompanyNameActivity.class);
                intent.putExtra("profilePic", uri.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = Objects.requireNonNull(data).getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    loadProfile(UploadCompanyLogoActivity.this, uri.toString(), companyLogoImage);
                    changePicture.setText(getResources().getString(R.string.changePicture));
                } catch (IOException ignored) {

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