package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.company.invite.InviteMemberModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class ActivityAddTeamInviteMember extends AppCompatActivity {
    Context context;

    RelativeLayout btnBack;
    TextView btnSave;
    EditText editText_memberName, editText_memberEmail;

    String type;
    InviteMemberModel inviteMemberModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_invite_member);
        Utilities.setWhiteBars(ActivityAddTeamInviteMember.this);
        context = ActivityAddTeamInviteMember.this;
        type = getIntent().getStringExtra("type");

        btnBack = findViewById(R.id.btnBack);
        editText_memberName = findViewById(R.id.editText_memberName);
        editText_memberEmail = findViewById(R.id.editText_memberEmail);
        btnSave = findViewById(R.id.btnSave);
        btnBack.setOnClickListener(v -> onBackPressed());

        if (type.equals("edit")) {
            inviteMemberModel = getIntent().getParcelableExtra("inviteMemberModel");
            editText_memberName.setText(inviteMemberModel.getMemberName());
            editText_memberEmail.setText(inviteMemberModel.getMemberEmail());
        }

        btnSave.setOnClickListener(v -> {
            String name = editText_memberName.getText().toString();
            String email = editText_memberEmail.getText().toString();

            if (name.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityAddTeamInviteMember.this, "Please enter name of team member");
            } else if (email.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityAddTeamInviteMember.this, "Please enter email of team member");
            } else if (!Utilities.isValidEmail(email)) {
                CustomCookieToast.showRequiredToast(ActivityAddTeamInviteMember.this, "Please enter valid email of team member");
            } else {
                Utilities.hideKeyboard(v, context);
                Intent intent = new Intent();
                intent.putExtra("inviteMemberModel", new InviteMemberModel(name, email));
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
            }

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

}