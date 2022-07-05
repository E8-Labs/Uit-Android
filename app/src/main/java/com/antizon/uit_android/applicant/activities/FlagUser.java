package com.antizon.uit_android.applicant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class FlagUser extends BaseActivity {
    private static final String TAG = FlagUser.class.getSimpleName();
    TextView send;
    EditText typeHere;
    String typeHereValue;
    ImageView backIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_user);
        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        send = findViewById(R.id.send);
        typeHere = findViewById(R.id.typeHere);
        backIcon=findViewById(R.id.backIcon);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");


    }


    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(FlagUser.this, typeHere);
                if (!validate()) {

                } else {

                }
            }

        });
    }
    public boolean validate() {
        Log.d(TAG, "validate: ");

        typeHereValue = typeHere.getText().toString().trim();

        boolean valid = true;
        if (typeHereValue.isEmpty()) {

            valid = false;
            typeHere.setError("Please enter description");
        }

        return valid;
    }


}