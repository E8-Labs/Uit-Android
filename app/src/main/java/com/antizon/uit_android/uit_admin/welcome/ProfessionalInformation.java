package com.antizon.uit_android.uit_admin.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.JobPosting;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;

public class ProfessionalInformation extends BaseActivity {
    private static final String TAG = ProfessionalInformation.class.getSimpleName();
    ImageView noahImage,grayBack;
    TextView title, product, employ,art,may,professional,series,department,jobDesign,productDesign,sales,market,experiences
            ,series2,employ2,design;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ModelAdminApplicants dataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_information);
        Utilities.setCustomStatusAndNavColor(ProfessionalInformation.this, R.color.app_color, R.color.white);
        setIds();
        getIntentData();
        initialize();
        setListener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
    void getIntentData() {

        if (getIntent().getParcelableExtra("dataModel") != null) {
            dataModel = getIntent().getParcelableExtra("dataModel");
            title.setText(dataModel.getName());
            Glide.with(ProfessionalInformation.this)
                    .load(dataModel.getProfile_image())
                    .circleCrop()
                    .into(noahImage);
            design.setText(dataModel.getIndustryDataModel().getName());
            employ2.setText(dataModel.getName());
            series2.setText(dataModel.getIndustryDataModel().getName());
        }
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        noahImage = findViewById(R.id.noahImage);
        title = findViewById(R.id.title);
        art = findViewById(R.id.art);
        may = findViewById(R.id.may);
        professional = findViewById(R.id.professional);
        series = findViewById(R.id.series);
        department = findViewById(R.id.department);
        jobDesign = findViewById(R.id.jobDesign);
        productDesign = findViewById(R.id.productDesign);
        sales = findViewById(R.id.sales);
        market = findViewById(R.id.market);
        experiences = findViewById(R.id.experiences);
        employ = findViewById(R.id.employ);
        employ2 = findViewById(R.id.employ2);
        series2 = findViewById(R.id.series2);
        product = findViewById(R.id.product);
        grayBack = findViewById(R.id.grayBack);
        design = findViewById(R.id.design);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(ProfessionalInformation.this);
    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        grayBack.setOnClickListener(view -> onBackPressed());
    }


}