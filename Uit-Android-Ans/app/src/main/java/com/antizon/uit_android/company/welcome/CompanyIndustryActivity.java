package com.antizon.uit_android.company.welcome;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class CompanyIndustryActivity extends AppCompatActivity {
    Context context;

    ImageView backIcon;
    RoundedImageView redNoah2;
    TextView btn_selectIndustries, next;
    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "";
    TagContainerLayout tagContainerLayout;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_industry);
        Utilities.setWhiteBars(CompanyIndustryActivity.this);
        context = CompanyIndustryActivity.this;
        initViews();
    }


    void initViews() {
        selectedCompanyIndustries = new ArrayList<>();

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        btn_selectIndustries = findViewById(R.id.btn_selectIndustries);
        tagContainerLayout = findViewById(R.id.tag_container_industries);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            Utilities.loadImage(CompanyIndustryActivity.this, encodedImageData, redNoah2);
        }

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            if (selectedCompanyIndustries.size() == 0){
                CustomCookieToast.showRequiredToast(CompanyIndustryActivity.this, "Please select minimum one company industry");
            }else {
                openNextScreen();
            }
        });

        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
            }

            @Override
            public void onTagLongClick(int position, String text) {
            }

            @Override
            public void onSelectedTagDrag(int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
                if (selectedCompanyIndustries != null && selectedCompanyIndustries.size() > position) {
                    tagContainerLayout.removeTag(position);
                    selectedCompanyIndustries.remove(position);

                    if (selectedCompanyIndustries.size() == 0){
                        tagContainerLayout.setVisibility(View.GONE);
                    }else{
                        tagContainerLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        btn_selectIndustries.setOnClickListener(v -> {
            Intent selectIndustries = new Intent(context, SelectCompanyIndustryActivity.class);
            onSelectedIndustriesLauncher.launch(selectIndustries);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });


    }


    ActivityResultLauncher<Intent> onSelectedIndustriesLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ApplicantDegreeDataModel selectedIndustry = intent.getParcelableExtra("degreeDataModel");
                tagContainerLayout.setVisibility(View.VISIBLE);
                tagContainerLayout.addTag(selectedIndustry.getName());
                selectedCompanyIndustries.add(selectedIndustry);
            }
        }
    });

    private void openNextScreen() {
        Intent intent = new Intent(CompanyIndustryActivity.this, SelectCompanyStageActivity.class);
        intent.putExtra("from", "add");
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}
