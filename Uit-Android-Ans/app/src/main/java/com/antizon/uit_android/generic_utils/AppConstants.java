package com.antizon.uit_android.generic_utils;


public class AppConstants {
    public static final int REQUEST_CODE_For_IMAGE = 100;

    //    GET REQUESTS
    public static String BASEURL = "https://uitdev.com/uitapis/api/";

    //Get Flagged User
    public static final String GET_FLAGGED_JOB = BASEURL + "flagged_jobs_list";

    //  GET Company industries
    public static final String GET_RACE = BASEURL + "races?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  POST REQUESTS
    //  POST SIGN IN
    public static final String SIGN_IN = BASEURL + "login";


    //  POST Register UIT Team
    public static final String REGISTER_UIT_TEAM = "register-uit-team";

    //  POST Register Applicant
    public static final String REGISTER_APPLICANT_FOR_RETROFIT = "register-applicant";

    public static final String REGISTER_COMPANY_FOR_RETROFIT = "register-company";


    //  POST Send Code
    public static final String SEND_CODE = BASEURL + "send_code";

    //  POST Verify Email
    public static final String VERIFY_EMAIL = BASEURL + "verify_email";


    //  POST Apply For Job
    //job_id=6 , token
    public static final String POST_APPLY_FOR_JOB = BASEURL + "apply_to_job";

}

