package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.slider.Slider;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyGenderProportionActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;
    GetDataService service;
    ProgressDialog progressDialog;

    ImageView backIcon, redNoah;
    TextView next, text_genderProportion, notSure;
    Slider menSeekBar, womenSeekBar, nonBinarySeekBar;

    int totalPercentage = 0, menPercentage = 0, womenPercentage = 0, nonBinaryPercentage = 0;

    String genderProportionPercentage = "", deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "", from;

    float men, women, nonBinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_gender_proportion);
        Utilities.setCustomStatusAndNavColor(CompanyGenderProportionActivity.this, R.color.white_dash, R.color.white_dash);
        context = CompanyGenderProportionActivity.this;
        sessionManagement = new SessionManagement(context);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            men = getIntent().getFloatExtra("men", 0.0f);
            women = getIntent().getFloatExtra("women", 0.0f);
            nonBinary = getIntent().getFloatExtra("nonBinary", 0.0f);
        }else {
            deiStatementValue = getIntent().getStringExtra("deiStatement");
            leadValue = getIntent().getStringExtra("lead");
            ergValue = getIntent().getStringExtra("erg");
            programmingValue = getIntent().getStringExtra("programming");
            trainingValue = getIntent().getStringExtra("training");

        }

        initViews();
        setListener();

        if (from.equals("edit")){
            menSeekBar.setValue(men);
            womenSeekBar.setValue(women);
            nonBinarySeekBar.setValue(nonBinary);
        }
    }


    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        notSure = findViewById(R.id.notSure);
        redNoah = findViewById(R.id.redNoah);
        menSeekBar = findViewById(R.id.men_seekbar);
        womenSeekBar = findViewById(R.id.women_seekbar);
        nonBinarySeekBar = findViewById(R.id.non_binary_meter);
        text_genderProportion = findViewById(R.id.text_genderProportion);

        menSeekBar.setLabelFormatter(value -> (int) value + "%");
        womenSeekBar.setLabelFormatter(value -> (int) value + "%");
        nonBinarySeekBar.setLabelFormatter(value -> (int) value + "%");

        loadProfile(CompanyGenderProportionActivity.this, sessionManagement.getProfileImage(), redNoah);
    }


    private void setListener() {

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            menPercentage = (int) (menSeekBar.getValue());
            womenPercentage = (int) (womenSeekBar.getValue());
            nonBinaryPercentage = (int) (nonBinarySeekBar.getValue());

            if (totalPercentage != 100) {
                CustomCookieToast.showRequiredToast(CompanyGenderProportionActivity.this, "Please select 100% gender proportion");
            } else {
                if (from.equals("edit")){
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    requestForUpdateCompanyGenderProportion(menPercentage, womenPercentage, nonBinaryPercentage);
                }else{
                    openNextScreen();
                }

            }
        });


        addSliderListener(menSeekBar, womenSeekBar, nonBinarySeekBar);
        addSliderListener(womenSeekBar, menSeekBar, nonBinarySeekBar);
        addSliderListener(nonBinarySeekBar, menSeekBar, womenSeekBar);

        notSure.setOnClickListener(v -> {
            if (from.equals("edit")){
                onBackPressed();
            }else {
                openNextScreen();
            }

        });

    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyGenderProportionActivity.this, CompanyGenderOrientationActivity.class);
        intent.putExtra("from",from);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        intent.putExtra("erg", ergValue);
        intent.putExtra("programming", programmingValue);
        intent.putExtra("training", trainingValue);
        intent.putExtra("genderProportion", totalPercentage);
        intent.putExtra("menPercentage", menPercentage);
        intent.putExtra("womenPercentage", womenPercentage);
        intent.putExtra("nonBinaryPercentage", nonBinaryPercentage);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void addSliderListener(Slider proportionSlider, Slider firstSlider, Slider secondSlider){
        proportionSlider.addOnChangeListener((slider, value, fromUser) -> {
            totalPercentage = 0;
            totalPercentage = (int) (firstSlider.getValue() + secondSlider.getValue());
            totalPercentage = (int) (totalPercentage + value);
            genderProportionPercentage = totalPercentage + "%";
            text_genderProportion.setText(genderProportionPercentage);
        });
    }

    private void requestForUpdateCompanyGenderProportion(int men, int women, int non_binary) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("men", men);
        mainObject.addProperty("women", women);
        mainObject.addProperty("non_binary", non_binary);
        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<MainResponseModel> call = service.updateDeiStatement("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("men", men);
                        intent.putExtra("women", women);
                        intent.putExtra("nonBinary", non_binary);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(CompanyGenderProportionActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyGenderProportionActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyGenderProportionActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}