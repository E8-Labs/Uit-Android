package com.antizon.uit_android.applicant.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class EthnicityOne extends BaseActivity {
    private static final String TAG = EthnicityOne.class.getSimpleName();
    ImageView backIcon;
    TextView yes, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethnicity_one);
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
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EthnicityOne.this, ActivityApplicantSelectRace.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EthnicityOne.this, ActivityApplicantSelectRace.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_out,R.anim.left_out);
            }
        });
    }

}