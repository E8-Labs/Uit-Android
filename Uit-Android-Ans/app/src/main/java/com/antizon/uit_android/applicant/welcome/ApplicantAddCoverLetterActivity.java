package com.antizon.uit_android.applicant.welcome;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.FileUtils;
import com.antizon.uit_android.utilities.ProgressBarAnimation;
import com.antizon.uit_android.utilities.ProgressRequestBody;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.io.File;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantAddCoverLetterActivity extends BaseActivity implements ProgressRequestBody.UploadCallbacks {
    private static final int PICK_FILE_RESULT_CODE = 1;
    Context context;
    GetDataService service;

    ImageView backIcon, redNoah2;
    SessionManagement sessionManagement;
    TextView next;

    String pdfFilePath = "";
    RoundedImageView addCover, ic_pdfFile;
    RelativeLayout layout_pdfSelected, layout_doneIc;
    ProgressBar progressBar;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_add_cover_letter);
        Utilities.setWhiteBars(ApplicantAddCoverLetterActivity.this);

        context = ApplicantAddCoverLetterActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        type= getIntent().getStringExtra("type");

        initViews();

    }

    void initViews() {

        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);

        addCover = findViewById(R.id.addCover);
        layout_pdfSelected = findViewById(R.id.layout_pdfSelected);
        progressBar = findViewById(R.id.progressBar);
        ic_pdfFile = findViewById(R.id.ic_pdfFile);
        layout_doneIc = findViewById(R.id.layout_doneIc);
        next = findViewById(R.id.next);

        sessionManagement = new SessionManagement(ApplicantAddCoverLetterActivity.this);
        loadProfile(ApplicantAddCoverLetterActivity.this, sessionManagement.getProfileImage(), redNoah2);

        backIcon.setOnClickListener(view -> onBackPressed());

        addCover.setOnClickListener(view -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("application/pdf");
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile = Intent.createChooser(chooseFile, "Choose a pdf file");
            startActivityIfNeeded(chooseFile, PICK_FILE_RESULT_CODE);
        });

        if (type.equals("edit")){
            next.setText(R.string.add);
        }else {
            next.setText(R.string.addLater);
        }


        next.setOnClickListener(view -> {
            if (type.equals("edit")){
                if (pdfFilePath.isEmpty()) {
                    CustomCookieToast.showRequiredToast(ApplicantAddCoverLetterActivity.this, "Please select your resume");
                } else {
                    next.setEnabled(false);
                    uploadCoverLetter("Bearer " + sessionManagement.getToken(), pdfFilePath);
                }
            }else {
                if (pdfFilePath.isEmpty()) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                } else {
                    next.setEnabled(false);
                    uploadCoverLetter("Bearer " + sessionManagement.getToken(), pdfFilePath);
                }
            }
        });


        backIcon.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_FILE_RESULT_CODE){
            assert data != null;
            Uri pdfFileUri = data.getData();
            if (pdfFileUri != null){
                pdfFilePath = FileUtils.getPath(context, pdfFileUri);
                if (pdfFilePath !=null){
                    if (pdfFilePath.isEmpty()){
                        Toast.makeText(context, "Pdf is Not Selected", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addCover.setVisibility(View.GONE);
                        layout_pdfSelected.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(context, "Pdf file path is empty", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(context, "Pdf file path is empty", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void uploadCoverLetter(String authToken, String pdfFilePath) {
        File file = new File(pdfFilePath);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        MultipartBody.Part mediasToUpload = MultipartBody.Part.createFormData("cover_letter", file.getName(), fileBody);
        Call<MainResponseModel> call = service.uploadApplicantCoverLetter(authToken, mediasToUpload);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        progressBar.setProgress(100);
                        ic_pdfFile.setVisibility(View.GONE);
                        layout_doneIc.setVisibility(View.VISIBLE);

                        sessionManagement.setCoverLetterSaved("true");
                        sessionManagement.setKeyProfileUpdated("true");

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        },500);
                    }else {
                        next.setEnabled(true);
                        CustomCookieToast.showFailureToast(ApplicantAddCoverLetterActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    next.setEnabled(true);
                    CustomCookieToast.showFailureToast(ApplicantAddCoverLetterActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                next.setEnabled(true);
                CustomCookieToast.showFailureToast(ApplicantAddCoverLetterActivity.this, "Failure!", t.getMessage());
            }
        });

    }

    @Override
    public void onProgressUpdate(int percentage) {
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, progressBar.getProgress(), percentage);
        anim.setDuration(500);
        progressBar.startAnimation(anim);
    }

    @Override
    public void onError() {
        CustomCookieToast.showFailureToast(ApplicantAddCoverLetterActivity.this, "Failure!", "Some Error occurred");
    }

    @Override
    public void onFinish() {
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


}