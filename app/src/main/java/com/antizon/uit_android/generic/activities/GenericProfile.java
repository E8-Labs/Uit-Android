package com.antizon.uit_android.generic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.antizon.uit_android.generic.model.ModelApplicantDepartment;

import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_admin.welcome.AdminMessage;
import com.antizon.uit_android.uit_admin.welcome.DemographicInformation;
import com.antizon.uit_android.uit_admin.welcome.ProfessionalInformation;
import com.bumptech.glide.Glide;
import com.greentoad.turtlebody.docpicker.DocPicker;
import com.greentoad.turtlebody.docpicker.core.DocPickerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GenericProfile extends BaseActivity {
    private static final String TAG = GenericProfile.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView arrow, arrowTwo, noahImage, grayBack,deleteIcon;
    TextView applicantEmail, phoneNumber, nameApplicant, product, roleText,addResume,addCover,message;
    ModelAdminApplicants dataModel;
    ModelAllJobs allJobs;
    File file;
    String role ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_profile);

        setIds();
        initialize();
        initializeViewsAsPerRole();
        getIntentData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        companyProfile();

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        arrow = findViewById(R.id.arrow);
        arrowTwo = findViewById(R.id.arrowTwo);
        applicantEmail = findViewById(R.id.applicantEmail);
        noahImage = findViewById(R.id.noahImage);
        phoneNumber = findViewById(R.id.phoneNumber);
        nameApplicant = findViewById(R.id.nameApplicant);
        product = findViewById(R.id.product);
        roleText = findViewById(R.id.roleText);
        deleteIcon = findViewById(R.id.deleteIcon);
        grayBack = findViewById(R.id.grayBack);
        message = findViewById(R.id.message);
        addResume = findViewById(R.id.addResume);
        addCover = findViewById(R.id.addCover);
    }

    void getIntentData() {
        if (getIntent().getExtras().getParcelable("dataModel") != null) {
            dataModel = (ModelAdminApplicants) getIntent().getExtras().getParcelable("dataModel");

            applicantEmail.setText(dataModel.getEmail());
//            hi.setText(dataModel.getName());
            product.setText(dataModel.getJob_title());
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(GenericProfile.this);
        role=sessionManagement.getRole();
        loadProfile(GenericProfile.this, sessionManagement.getProfileImage(), noahImage);

    }

    void initializeViewsAsPerRole() {

//        case UITAdmin = 1
//        case Company = 2
//        case UITMember = 3
//        case CompanyMember = 4
//        case Applicant = 5

//        Set Visible to all the widgets (who are changing there)

        if (role.equalsIgnoreCase("1")) {

            deleteIcon.setVisibility(View.VISIBLE);


        } else if (role.equalsIgnoreCase("2")) {

        } else if (role.equalsIgnoreCase("3")) {

            deleteIcon.setVisibility(View.GONE);
        } else if (role.equalsIgnoreCase("4")) {

        } else if (role.equalsIgnoreCase("5")) {

        }
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( GenericProfile.this, AdminMessage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        addResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> docs = new ArrayList<>();
                docs.add(DocPicker.DocTypes.PDF);
                docs.add(DocPicker.DocTypes.MS_POWERPOINT);
                docs.add(DocPicker.DocTypes.MS_EXCEL);
                docs.add(DocPicker.DocTypes.TEXT);

                DocPickerConfig pickerConfig = new DocPickerConfig()
                        .setAllowMultiSelection(false)
                        .setShowConfirmationDialog(true)
                        .setExtArgs(docs);

                DocPicker.with(GenericProfile.this)
                        .setConfig(pickerConfig)
                        .onResult()
                        .subscribe(new Observer<ArrayList<Uri>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: d: " + d.toString());
                            }

                            @Override
                            public void onNext(ArrayList<Uri> uris) {
                                //uris: list of uri
//                                Get file from uri:
//                                Get mime from uri:
//                                Used in Glide:
                                Log.d(TAG, "onNext: uris: " + uris.size());
                                Uri uri = uris.get(0);
                                file = new File(uris.get(0).getPath());
                                String mimeType = getContentResolver().getType(uri);
//                                Glide.with(Apply.this)
//                                        .load(uri)
//                                        .into(addResume);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: throwable: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");

                            }
                        });
            }
        });
        addCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> docs = new ArrayList<>();
                docs.add(DocPicker.DocTypes.PDF);
                docs.add(DocPicker.DocTypes.MS_POWERPOINT);
                docs.add(DocPicker.DocTypes.MS_EXCEL);
                docs.add(DocPicker.DocTypes.TEXT);

                DocPickerConfig pickerConfig = new DocPickerConfig()
                        .setAllowMultiSelection(false)
                        .setShowConfirmationDialog(true)
                        .setExtArgs(docs);

                DocPicker.with(GenericProfile.this)
                        .setConfig(pickerConfig)
                        .onResult()
                        .subscribe(new Observer<ArrayList<Uri>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: d: " + d.toString());
                            }

                            @Override
                            public void onNext(ArrayList<Uri> uris) {
                                //uris: list of uri
//                                Get file from uri:
//                                Get mime from uri:
//                                Used in Glide:
                                Log.d(TAG, "onNext: uris: " + uris.size());
                                Uri uri = uris.get(0);
                                file = new File(uris.get(0).getPath());
                                String mimeType = getContentResolver().getType(uri);
//                                Glide.with(Apply.this)
//                                        .load(uri)
//                                        .into(addResume);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: throwable: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");

                            }
                        });
            }
        });

        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GenericProfile.this, ProfessionalInformation.class);
//                intent.putExtra("dataModel", dataModel);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
        arrowTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GenericProfile.this, DemographicInformation.class);
//                intent.putExtra("dataModel", dataModel);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

    }

    void companyProfile() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "126");
        sendServerRequestPOST(AppConstants.GET_PROFILE, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());
                String bio = dataObject.getString("bio");
                Log.d(TAG, "onResponse: bio " + bio);
                String job_title = dataObject.getString("job_title");
                Log.d(TAG, "onResponse: job_title " + job_title);
                String phone = dataObject.getString("phone");
                Log.d(TAG, "onResponse: phone " + phone);
                String profile_image = dataObject.getString("profile_image");
                Log.d(TAG, "onResponse: profile_image " + profile_image);
                String name = dataObject.getString("name");
                Log.d(TAG, "onResponse: name " + name);
                String email = dataObject.getString("email");
                Log.d(TAG, "onResponse: email " + email);

                applicantEmail.setText(email);
                roleText.setText(bio);
                product.setText(job_title);
                phoneNumber.setText(phone);
                nameApplicant.setText(name);
                Glide.with(GenericProfile.this)
                        .load(profile_image)
                        .circleCrop()
                        .into(noahImage);

                dataModel = new ModelAdminApplicants();
                dataModel.setName(name);
                dataModel.setEmail(email);
                dataModel.setJob_title(job_title);
                dataModel.setProfile_image(profile_image);


                JSONArray industriesArray = dataObject.getJSONArray("industries");
                Log.d(TAG, "onResponse: industries: size: " + industriesArray.length());
                for (int i = 0; i < industriesArray.length(); i++) {

                    JSONObject jsonObject1 = industriesArray.getJSONObject(i);
                    name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);
                    product.setText(name);

                    ModelApplicantDepartment industryDataModel = new ModelApplicantDepartment();
                    industryDataModel.setName(name);
                    dataModel.setIndustryDataModel(industryDataModel);


                }
                JSONObject professional_infoObject = dataObject.getJSONObject("professional_info");
                Log.d(TAG, "onResponse: professional_info: size: " + professional_infoObject.length());

                String resume = professional_infoObject.getString("resume");
//                resumeIcon.setText(resume);

                JSONArray jobsArray = dataObject.getJSONArray("jobs");
                Log.d(TAG, "onResponse: jobs: size: " + jobsArray.length());
                for (int i = 0; i < jobsArray.length(); i++) {

                    JSONObject jsonObject1 = jobsArray.getJSONObject(i);
                    name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);
                }


                JSONArray user_sizesArray = dataObject.getJSONArray("user_sizes");
                Log.d(TAG, "onResponse: user_sizes: size: " + user_sizesArray.length());
                for (int i = 0; i < user_sizesArray.length(); i++) {

                    JSONObject jsonObject1 = user_sizesArray.getJSONObject(i);
                    name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    dataModel.setName(name);
                    ModelApplicantDepartment industryDataModel = new ModelApplicantDepartment();
                    industryDataModel.setName(name);
                    dataModel.setIndustryDataModel(industryDataModel);
                }


                JSONArray user_stagesArray = dataObject.getJSONArray("user_stages");
                Log.d(TAG, "onResponse: user_stages: size: " + user_stagesArray.length());
                for (int i = 0; i < user_stagesArray.length(); i++) {

                    JSONObject jsonObject1 = user_stagesArray.getJSONObject(i);
                    name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);
                    ModelApplicantDepartment industryDataModel = new ModelApplicantDepartment();

                    industryDataModel.setName(name);
                    dataModel.setIndustryDataModel(industryDataModel);

//                    pendingModel.setName(name);
                }
//
//                JSONArray jobsArray = dataObject.getJSONArray("jobs");
//                Log.d(TAG, "onResponse: jobs: size: " + jobsArray.length());
//                for (int i = 0; i < jobsArray.length(); i++) {
//
//                }
//                JSONArray departmentsArray = dataObject.getJSONArray("departments");
//                Log.d(TAG, "onResponse: departments: size: " + departmentsArray.length());
//                for (int i = 0; i < departmentsArray.length(); i++) {
//
//                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }




}