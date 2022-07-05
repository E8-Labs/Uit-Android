package com.antizon.uit_android.recruiter.welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.welcome.ApplicantPersonalInfoActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;

import java.io.IOException;

public class ProfilePic extends BaseActivity {

    private static final String TAG = ApplicantPersonalInfoActivity.class.getSimpleName();

    ImageView companyLogoImage;
    TextView accountTypeTitle, next, changePicture;
    String encodedImageData = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic);

        setIds();
        initialize();
        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        next = findViewById(R.id.next);
        accountTypeTitle = findViewById(R.id.accountTypeTitle);
        companyLogoImage = findViewById(R.id.companyLogoImage);
        changePicture = findViewById(R.id.changePicture);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!encodedImageData.isEmpty()) {

                    String profilePic = "" + uri;
                    Log.d(TAG, "onClick: profilePic: " + profilePic);
                    Intent intent = new Intent(ProfilePic.this, FullName.class);
                    intent.putExtra("profilePic", uri.toString());
                    startActivity(intent);
                }
                else if (encodedImageData.isEmpty()){
                    Toast.makeText(ProfilePic.this, "Please upload your profile picture", Toast.LENGTH_SHORT).show();

                }
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(ProfilePic.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == AppConstants.REQUEST_CODE_For_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    Log.d(TAG, "onActivityResult: encodedImageData: " + encodedImageData);

                    loadProfile(ProfilePic.this, uri.toString(), companyLogoImage);
                    changePicture.setText(getResources().getString(R.string.changePicture));

                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: IOException: " + e.getMessage());
                }
            }
        }
    }


}