package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyConnectGreenHouseActivity extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout btnConnect;
    ImageView backIcon, redNoah2;
    TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_connect_green_house);
        Utilities.setWhiteBars(CompanyConnectGreenHouseActivity.this);
        context = CompanyConnectGreenHouseActivity.this;
        sessionManagement = new SessionManagement(context);

        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);
        skip = findViewById(R.id.skip);
        btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(v -> {
            Intent connectIntent = new Intent(context, CompanyAddGreenHouseAccountActivity.class);
            startActivity(connectIntent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        skip.setOnClickListener(v -> {
            Intent connectIntent = new Intent(context, CompanyDEIStatementActivity.class);
            connectIntent.putExtra("from", "add");
            startActivity(connectIntent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), redNoah2);

    }
}