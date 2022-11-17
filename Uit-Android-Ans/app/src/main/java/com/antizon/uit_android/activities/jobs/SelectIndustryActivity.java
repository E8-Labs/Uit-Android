package com.antizon.uit_android.activities.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.filter.IndustryAdapter;
import com.antizon.uit_android.models.applicant.filter.IndustriesList;
import com.antizon.uit_android.models.applicant.filter.IndustryModel;
import com.antizon.uit_android.utilities.Utilities;

import java.util.List;

public class SelectIndustryActivity extends AppCompatActivity {
    Context context;
    RelativeLayout btnBack;
    RecyclerView recyclerview_industry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_industry);
        Utilities.setCustomStatusAndNavColor(SelectIndustryActivity.this, R.color.white_dash, R.color.white_dash);
        context = SelectIndustryActivity.this;
        btnBack = findViewById(R.id.btnBack);
        recyclerview_industry = findViewById(R.id.recyclerview_industry);

        IndustriesList industriesList = getIntent().getParcelableExtra("list");
        if (industriesList != null) {
            List<IndustryModel> list = industriesList.getData();
            recyclerview_industry.setLayoutManager(new LinearLayoutManager(context));
            IndustryAdapter adapter = new IndustryAdapter(context, list);
            recyclerview_industry.setAdapter(adapter);

            adapter.setOnSelectionListener((position, model) -> {
                Intent intent = new Intent();
                intent.putExtra("industry", model);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
            });

        }





        btnBack.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

}