package com.antizon.uit_android.applicant.welcome;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class Bio extends BaseActivity {
    private static final String TAG = Bio.class.getSimpleName();

    ImageView backIcon;
    TextView next;
    EditText typeHere;
    String typeHereValue= "", employeeValue = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio2);
        setIds();
        initialize();
        setListener();
        getIntentData();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        typeHere = findViewById(R.id.typeHere);

    }
    void initialize(){
        Log.d(TAG, "initialize: ");
    }

    void getIntentData() {

        employeeValue = getIntent().getStringExtra("employeStatus");
    }

    void setListener()
    {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Bio.this, typeHere);
                if (!validate()) {

                }
//                else if (typeHereValue.equalsIgnoreCase("Naeem")){
//
//                    Intent intent = new Intent(Bio.this, Education.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
//                }
                else {
                    openNextScreen();
                }
            }
        });
    }
    public boolean validate() {
        Log.d(TAG, "validate: ");
        typeHereValue = typeHere.getText().toString().trim();

        boolean valid = true;
        if (typeHereValue.isEmpty()) {
            typeHere.setError("Please enter applicant bio");
            valid = false;
        }
        return valid;
    }
    void openNextScreen() {

        Intent intent = new Intent(Bio.this, ApplicantEducationActivity.class);

        intent.putExtra("employeStatus", employeeValue);
        intent.putExtra("bio", typeHereValue);
        startActivity(intent);
    }
}