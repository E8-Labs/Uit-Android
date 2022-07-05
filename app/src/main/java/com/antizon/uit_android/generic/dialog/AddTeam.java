package com.antizon.uit_android.generic.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.fragment.BaseFragment;

public class AddTeam extends Dialog {

    private static final String TAG = AddTeam.class.getSimpleName();
    public Context c;
    public Dialog d;
    ConstraintLayout constraintLayout;
    TextView airBnb,travel,requestToJoin,sendRequest;
    ProgressDialog progressDialog;


    public AddTeam(@NonNull Context context) {

        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_member);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setIds();
        initialize();
        setListener();
    }

    void setIds() {

        airBnb = findViewById(R.id.airBnb);
        travel = findViewById(R.id.travel);
        requestToJoin = findViewById(R.id.requestToJoin);
        sendRequest = findViewById(R.id.sendRequest);
        airBnb = findViewById(R.id.airBnb);
        constraintLayout = findViewById(R.id.constraintLayout);

    }

    void initialize() {

        progressDialog = new ProgressDialog(c);
    }

    void setListener() {

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
    }
}