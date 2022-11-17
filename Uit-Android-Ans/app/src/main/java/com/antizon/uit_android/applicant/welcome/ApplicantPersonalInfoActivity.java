package com.antizon.uit_android.applicant.welcome;

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

public class ApplicantPersonalInfoActivity extends BaseActivity {

    private static final String TAG = ApplicantPersonalInfoActivity.class.getSimpleName();
    TextView changePicture, next;
    ImageView companyLogoImage;
    String encodedImageData = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_personal_info);
        Utilities.setWhiteBars(ApplicantPersonalInfoActivity.this);
        setIds();
        initialize();
        setListener();
    }

    private void setIds() {

        next = findViewById(R.id.next);
        changePicture = findViewById(R.id.changePicture);
        companyLogoImage = findViewById(R.id.companyLogoImage);
    }

    private void initialize() {
        Log.d(TAG, "initialize: ");

    }

    private void setListener() {
        Log.d(TAG, "setListener: ");

        companyLogoImage.setOnClickListener(view -> pickImage(ApplicantPersonalInfoActivity.this));

        changePicture.setOnClickListener(view -> pickImage(ApplicantPersonalInfoActivity.this));

        next.setOnClickListener(v -> {

            if (encodedImageData.isEmpty()) {
                CustomCookieToast.showRequiredToast(ApplicantPersonalInfoActivity.this, "Please select your profile picture");
            }else {
                String profilePic = "" + uri;
                Log.d(TAG, "onClick: profilePic: " + profilePic);
                Intent intent = new Intent(ApplicantPersonalInfoActivity.this, ApplicantFullNameActivity.class);
                intent.putExtra("profilePic", uri.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    //code for image picker from gallery and camera

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    uri = data.getParcelableExtra("path");
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                        Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);

                        loadProfile(ApplicantPersonalInfoActivity.this, uri.toString(), companyLogoImage);
                        changePicture.setText(getResources().getString(R.string.changePicture));

                    } catch (IOException e) {
                        Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
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
}