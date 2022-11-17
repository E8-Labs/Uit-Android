package com.antizon.uit_android.recruiter.welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import java.io.IOException;
import java.util.Objects;

public class RecruiterProfilePicActivity extends BaseActivity {

    ImageView companyLogoImage;
    TextView accountTypeTitle, next, changePicture;
    String encodedImageData = "", companyId = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_profile_pic);
        Utilities.setWhiteBars(RecruiterProfilePicActivity.this);
        initViews();
    }

    private void initViews() {
        companyId = getIntent().getStringExtra("companyId");
        next = findViewById(R.id.next);
        accountTypeTitle = findViewById(R.id.accountTypeTitle);
        companyLogoImage = findViewById(R.id.companyLogoImage);
        changePicture = findViewById(R.id.changePicture);

        next.setOnClickListener(view -> {
            if (encodedImageData.isEmpty()) {
                CustomCookieToast.showRequiredToast(RecruiterProfilePicActivity.this, "Please select your profile picture");
            }else {
                Intent intent = new Intent(RecruiterProfilePicActivity.this, RecruiterFullNameActivity.class);
                intent.putExtra("profilePic", uri.toString());
                intent.putExtra("companyId", companyId);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            }

        });

        changePicture.setOnClickListener(view -> pickImage(RecruiterProfilePicActivity.this));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = Objects.requireNonNull(data).getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    loadProfile(RecruiterProfilePicActivity.this, uri.toString(), companyLogoImage);
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