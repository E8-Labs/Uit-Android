package com.antizon.uit_android.applicant.welcome;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class ActivityContactUs extends BaseActivity {

    TextView send;
    EditText fieldOfStudy, message, typeHere;
    String fieldOfStudyValue, messageValue, typeHereValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Utilities.setCustomStatusAndNavColor(ActivityContactUs.this, R.color.white_dash, R.color.white_dash);
        initViews();
    }


    void initViews() {
        send = findViewById(R.id.send);
        fieldOfStudy = findViewById(R.id.fieldOfStudy);
        message = findViewById(R.id.message);
        typeHere = findViewById(R.id.typeHere);

        send.setOnClickListener(v -> {
            fieldOfStudyValue = fieldOfStudy.getText().toString().trim();
            messageValue = message.getText().toString().trim();
            typeHereValue = typeHere.getText().toString().trim();
            if (fieldOfStudyValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityContactUs.this, "Please enter your name");
            }else if (messageValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityContactUs.this, "Please enter message title");
            }else if (typeHereValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityContactUs.this, "Please enter message content");
            }else {
                hideSoftKeyboard(ActivityContactUs.this, fieldOfStudy);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}