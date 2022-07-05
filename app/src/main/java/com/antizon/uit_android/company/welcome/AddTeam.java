package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class AddTeam extends BaseActivity {
    private static final String TAG = AddTeam.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
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

    }

    void initialize() {
        Log.d(TAG, "initialize: ");


    }

    void setListener() {
        Log.d(TAG, "setListener: ");
    }


}