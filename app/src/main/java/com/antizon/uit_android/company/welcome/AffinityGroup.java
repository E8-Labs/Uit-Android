package com.antizon.uit_android.company.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class AffinityGroup extends BaseActivity {
    private static final String TAG = AffinityGroup.class.getSimpleName();

    ImageView backIcon;
    TextView yes, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affinity_group);
        setIds();
        initialize();
        setListener();
    }

    void setIds() {

        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        yes = findViewById(R.id.yes);
        skip = findViewById(R.id.skip);

    }
    void initialize(){
        Log.d(TAG, "initialize: ");

    }

    void setListener()
    {
        Log.d(TAG, "setListener: ");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AffinityGroup.this, CompanyDEIProgrammingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AffinityGroup.this, CompanyDEIProgrammingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_out,R.anim.left_out);
            }
        });


    }

}