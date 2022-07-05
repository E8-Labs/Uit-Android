package com.antizon.uit_android.uit_admin.community;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class CreatePost extends BaseActivity {
    private static final String TAG = CreatePost.class.getSimpleName();

    TextView remote, hybrid, onSite,next;
    ImageView backIcon;
    ConstraintLayout pendingOneLayout, approvedOneLayout, pausedOneLayout;
    EditText title,typeHere,url;
    String titleValue, descriptionValue,urlValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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
        title = findViewById(R.id.title);
        typeHere = findViewById(R.id.typeHere);
        url = findViewById(R.id.url);
        pendingOneLayout = findViewById(R.id.pendingOneLayout);
        approvedOneLayout = findViewById(R.id.approvedOneLayout);
        pausedOneLayout = findViewById(R.id.pausedOneLayout);
        remote = findViewById(R.id.remote);
        hybrid = findViewById(R.id.hybrid);
        onSite = findViewById(R.id.onSite);
        next = findViewById(R.id.next);
        backIcon = findViewById(R.id.backIcon);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        next.setText("Post");
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(v -> {
            hideSoftKeyboard(CreatePost.this, title);
            if (!validate()) {

            } else {

            }
        });

        backIcon.setOnClickListener(view -> onBackPressed());

        pendingOneLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");

            setPendingOneLayout();

        });
        approvedOneLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");

            setApprovedOneLayout();

        });
        pausedOneLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");

            setPausedOneLayout();

        });
    }
    public boolean validate() {
        Log.d(TAG, "validate: ");

        titleValue = title.getText().toString().trim();
        descriptionValue = typeHere.getText().toString().trim();
        urlValue = url.getText().toString().trim();

        boolean valid = true;
        if (titleValue.isEmpty()) {

            valid = false;
            title.setError("Please enter Title");
        }
        if (descriptionValue.isEmpty()) {

            typeHere.setError("Please enter Description");
            valid = false;
        }
        if (urlValue.isEmpty()) {

            url.setError("Please enter URL");
            valid = false;
        }
        return valid;
    }

    void setPendingOneLayout() {

        pendingOneLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        approvedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        pausedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        remote.setTextColor(getResources().getColor(R.color.white));
        onSite.setTextColor(getResources().getColor(R.color.black));
        hybrid.setTextColor(getResources().getColor(R.color.black));
    }
    void setApprovedOneLayout() {

        pendingOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        approvedOneLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        pausedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        remote.setTextColor(getResources().getColor(R.color.black));
        onSite.setTextColor(getResources().getColor(R.color.white));
        hybrid.setTextColor(getResources().getColor(R.color.black));
    }
    void setPausedOneLayout() {

        pendingOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        approvedOneLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        pausedOneLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));

        remote.setTextColor(getResources().getColor(R.color.black));
        onSite.setTextColor(getResources().getColor(R.color.black));
        hybrid.setTextColor(getResources().getColor(R.color.white));

    }


}