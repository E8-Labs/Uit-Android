package com.antizon.uit_android.company.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.activities.JobPostingDescription;
import com.antizon.uit_android.generic.model.GenericModel;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompanyPostJobFragment extends BaseFragment {
    private static final String TAG = CompanyPostJobFragment.class.getSimpleName();

    TextView remote, inPerson, hyBird, fullTime, intern, partTime, freeLancer, internship, next;
    ConstraintLayout remoteLayout, hyBirdLayout, inPersonLayout, fullLayout, partTimeLayout,
            contractLayout, freeLancerLayout, internshipLayout;
    EditText jobTitleText, industryEdit, departmentEdit, cityEdit;
    String jobTitleTextValue = "", industryEditValue = "", departmentEditValue = "", cityEditValue = "",
            locationStatus = "Remote", employmentStatus = "Full Time", minSalary = "", maxSalary = "", salaryRange = "",
            minEducationRequired = "", languageRequirement = "", vaccinationRequirement = "";

    ImageView backIcon, companyIV;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    SearchableSpinner degreeSpinner, languageSpinner;
    ArrayList<String> degreeList, languageList;
    ArrayList<GenericModel> degreeModelList;
    ArrayList<GenericModel> languageModelList;

    RangeSeekBar salarySeekBar;
    CheckBox educationNotRequiredCB, languageNotRequiredCB, vaccinationRequirementCB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_job, container, false);

        setIds(view);
        initialize();
        setListener();

        setDegreeSpinnerAdapter();
        setLanguageSpinnerAdapter();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        getDegreeAndLanguageList();
    }

    void setIds(View view) {
        Log.d(TAG, "setIds: ");
        backIcon = view.findViewById(R.id.backIcon);
        next = view.findViewById(R.id.next);
        companyIV = view.findViewById(R.id.companyIV);
        jobTitleText = view.findViewById(R.id.jobTitleText);
        industryEdit = view.findViewById(R.id.industryEdit);
        departmentEdit = view.findViewById(R.id.departmentEdit);
        cityEdit = view.findViewById(R.id.cityEdit);
        remoteLayout = view.findViewById(R.id.remoteLayout);
        hyBirdLayout = view.findViewById(R.id.hyBirdLayout);
        inPersonLayout = view.findViewById(R.id.inPersonLayout);
        fullLayout = view.findViewById(R.id.fullLayout);
        partTimeLayout = view.findViewById(R.id.partTimeLayout);
        contractLayout = view.findViewById(R.id.contractLayout);
        freeLancerLayout = view.findViewById(R.id.freeLancerLayout);
        internshipLayout = view.findViewById(R.id.internshipLayout);
        freeLancer = view.findViewById(R.id.freeLancer);
        internship = view.findViewById(R.id.internship);
        remote = view.findViewById(R.id.remote);
        inPerson = view.findViewById(R.id.inPerson);
        hyBird = view.findViewById(R.id.hyBird);
        fullTime = view.findViewById(R.id.fullTime);
        intern = view.findViewById(R.id.intern);
        partTime = view.findViewById(R.id.partTime);
        salarySeekBar = view.findViewById(R.id.salarySeekbar);
        degreeSpinner = view.findViewById(R.id.degreeSpinner);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        educationNotRequiredCB = view.findViewById(R.id.educationNotRequired);
        languageNotRequiredCB = view.findViewById(R.id.languageNotRequired);
        vaccinationRequirementCB = view.findViewById(R.id.vaccinationRequired);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        sessionManagement = new SessionManagement(getContext());
        progressDialog = new ProgressDialog(getContext());
        Glide.with(getContext())
                .load(sessionManagement.getProfileImage())
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.uit_app_icon_for_background))
                .into(companyIV);
        degreeList = new ArrayList<>();
        languageList = new ArrayList<>();
        degreeModelList = new ArrayList<>();
        languageModelList = new ArrayList<>();
        salarySeekBar.setRange(50, 400);
        salarySeekBar.setProgress(50, 400);
        salarySeekBar.setIndicatorTextDecimalFormat("0"); // it will show only integer values
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(jobTitleText);
                if (!validate()) {
                } else {
                    Intent intent = new Intent(getActivity(), JobPostingDescription.class);
                    intent.putExtra("jobTitle", jobTitleTextValue);
                    intent.putExtra("industryTitle", industryEditValue);
                    intent.putExtra("department", departmentEditValue);
                    intent.putExtra("cityState", cityEditValue);
                    intent.putExtra("locationStatus", locationStatus);
                    intent.putExtra("employmentType", employmentStatus);
                    intent.putExtra("minSalary", minSalary);
                    intent.putExtra("maxSalary", maxSalary);
                    intent.putExtra("salaryRange", salaryRange);
                    intent.putExtra("minEducationRequired", minEducationRequired);
                    intent.putExtra("languageRequirement", languageRequirement);
                    intent.putExtra("vaccinationRequirement", vaccinationRequirement);
                    startActivity(intent);
                }
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackClick();
            }
        });

        remoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                locationStatus = "Remote";
                setPendingOneLayout();

            }
        });

        hyBirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                locationStatus = "HyBird";
                setApprovedOneLayout();

            }
        });
        inPersonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                locationStatus = "InPerson";
                setPausedOneLayout();

            }
        });
        fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                employmentStatus = "Full Time";
                setFullLayout();

            }
        });
        partTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                employmentStatus = "Part Time";
                setPartLayout();

            }
        });
        contractLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                employmentStatus = "Contract";
                setContractLayout();

            }
        });
        freeLancerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                employmentStatus = "FreeLance";
                setFreeLancerLayout();

            }
        });
        internshipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");
                employmentStatus = "Internship";
                setInternshipLayout();

            }
        });

        salarySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                int minValue = (int) leftValue;
                int maxValue = (int) rightValue;
                Log.d(TAG, "onRangeChanged: " + minValue + " and " + maxValue);

                minSalary = "" + minValue;
                maxSalary = "" + maxValue;
                salaryRange = "$" + minSalary + "k - $" + maxSalary + "k";
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


    }

    public boolean validate() {
        Log.d(TAG, "validate: ");

        jobTitleTextValue = jobTitleText.getText().toString().trim();
        industryEditValue = industryEdit.getText().toString().trim();
        departmentEditValue = departmentEdit.getText().toString().trim();
        cityEditValue = cityEdit.getText().toString().trim();

        boolean valid = true;
        if (jobTitleTextValue.isEmpty()) {

            valid = false;
            jobTitleText.setError("Please enter your Job title");
        }
        if (industryEditValue.isEmpty()) {

            industryEdit.setError("Please enter your industry");
            valid = false;
        }
        if (departmentEditValue.isEmpty()) {

            departmentEdit.setError("Please enter your department");
            valid = false;
        }
        if (cityEditValue.isEmpty()) {

            cityEdit.setError("Please enter your city");
            valid = false;
        }

        if (educationNotRequiredCB.isChecked()) {
            minEducationRequired = "Not Required";
        }

        if (languageNotRequiredCB.isChecked()) {
            languageRequirement = "Not Required";
        }


        if (vaccinationRequirementCB.isChecked()) {
            vaccinationRequirement = "Not Required";
        } else {
            vaccinationRequirement = "Required";
        }

        return valid;
    }

    void getDegreeAndLanguageList() {
        sendServerRequestGET(AppConstants.GET_DEGREE_LIST, sessionManagement.getToken());
        sendServerRequestGET(AppConstants.GET_LANGUAGE_LIST, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();

        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: status: " + message);
            Log.d(TAG, "onResponse: status: " + status);
            Log.d(TAG, "onResponseReceived: response: " + response);

            if (urlCalled.equalsIgnoreCase(AppConstants.GET_DEGREE_LIST)) {
                if (status) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jsonObject1 = dataArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String name = jsonObject1.getString("name");

                        degreeList.add(name);
                        degreeModelList.add(new GenericModel(id, name));
                    }


                }
            } else if (urlCalled.equalsIgnoreCase(AppConstants.GET_LANGUAGE_LIST)) {
                if (status) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jsonObject1 = dataArray.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        String name = jsonObject1.getString("name");

                        languageList.add(name);
                        languageModelList.add(new GenericModel(id, name));
                    }
                }
            }

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Log.d(TAG, "onResponseReceived: jsonException: " + jsonException.getMessage());
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
    }


    void setDegreeSpinnerAdapter() {

        degreeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, degreeList));
        degreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideSoftKeyboard(jobTitleText);
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                minEducationRequired = degreeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                hideSoftKeyboard(jobTitleText);
                Log.d(TAG, "onNothingSelected: ");
            }


        });
        degreeSpinner.setTitle("Search Degree");

    }

    void setLanguageSpinnerAdapter() {

        languageSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, languageList));
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideSoftKeyboard(jobTitleText);
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                languageRequirement = languageSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hideSoftKeyboard(jobTitleText);
                Log.d(TAG, "onNothingSelected: ");
            }
        });
        languageSpinner.setTitle("Search Language");
    }

    void setPendingOneLayout() {

        remoteLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        hyBirdLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        inPersonLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        remote.setTextColor(getResources().getColor(R.color.white));
        hyBird.setTextColor(getResources().getColor(R.color.black));
        inPerson.setTextColor(getResources().getColor(R.color.black));
    }

    void setApprovedOneLayout() {

        remoteLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        hyBirdLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        inPersonLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        remote.setTextColor(getResources().getColor(R.color.black));
        hyBird.setTextColor(getResources().getColor(R.color.white));
        inPerson.setTextColor(getResources().getColor(R.color.black));
    }

    void setPausedOneLayout() {

        remoteLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        hyBirdLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        inPersonLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));

        remote.setTextColor(getResources().getColor(R.color.black));
        hyBird.setTextColor(getResources().getColor(R.color.black));
        inPerson.setTextColor(getResources().getColor(R.color.white));

    }

    void setFullLayout() {
        fullLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.white));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.black));
    }

    void setPartLayout() {
        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.white));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.black));

    }

    void setContractLayout() {

        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.white));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.black));
    }

    void setFreeLancerLayout() {

        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.white));
        internship.setTextColor(getResources().getColor(R.color.black));

    }

    void setInternshipLayout() {

        fullLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        partTimeLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        contractLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        freeLancerLayout.setBackground(getResources().getDrawable(R.drawable.text_here_border));
        internshipLayout.setBackground(getResources().getDrawable(R.drawable.full_time_border));

        fullTime.setTextColor(getResources().getColor(R.color.black));
        intern.setTextColor(getResources().getColor(R.color.black));
        partTime.setTextColor(getResources().getColor(R.color.black));
        freeLancer.setTextColor(getResources().getColor(R.color.black));
        internship.setTextColor(getResources().getColor(R.color.white));
    }


}