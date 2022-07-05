package com.antizon.uit_android.generic_utils;


public class AppConstants {
    public static final String keyId = "AIzaSyAdmwtlpPETj-1ymTkgmmhhalnQQYGS_8Q";
    public static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //    GET REQUESTS
    public static String BASEURL = "http://www.zorroapp.tech/uit/api/";
    public static final int REQUEST_CODE_For_IMAGE = 100;

    //  Get My Job List
    //ADMIN send it
    // PARAMS:   job_status, search , off_set , user_id, TOKEN
    public static final String GET_MY_JOB_LIST_ADMIN = BASEURL + "my_jobs_list?job_status=1";

    //  Get My Job List
    //USER  send it
    // PARAMS:   job_status, search , off_set, TOKEN
    public static final String GET_MY_JOB_LIST_USER = BASEURL + "my_jobs_list?job_status=1&search=1&off_set=0 ";

    //  Get Recruiter Company
    // ACCESS TOKEN
    public static final String GET_RECRUITER_COMPANY = BASEURL + "get_recruiter_company ";

    //  Get Applicant HOME
    // lat=9.6 , lang=10
    public static final String APPLICANT_HOME = BASEURL + "applicant_home?lang=10&lat=9.6";

    //  Get Applicant Job List
    // lat=9.6 , lang=10, search=test , off_set=0
    public static final String APPLICANT_JOB_LIST = BASEURL + "applicant_job_list?lat=9.6&lang=10&search=software&off_set=1";

    //  Get Admin Dashboard
    public static final String POST_ADMIN_DASHBOARD = BASEURL + "admin_dashboard";

    //  Get Job Detail
    //job_id=4
    public static final String GET_JOB_DETAIL = BASEURL + "get_job_detail?job_id=";

    //  Get Channel List
    //TOKEN
    public static final String GET_CHANNEL_LIST = BASEURL + "channels_list";


    //  Get Applied Jobs
    //off_set=1,token
    public static final String GET_APPLIED_JOBS = BASEURL + "applied_jobs_list?off_set=1";

    //  Get Saved Jobs
    //off_set=0,token
    public static final String GET_SAVED_JOBS = BASEURL + "saved_jobs_list?off_set=1";

    //Get Flagged User
    //off_set=0,token
    public static final String GET_FLAGGED_USER = BASEURL + "flagged_users_list?role=5";

    //Get Flagged User
    //token
    public static final String GET_FLAGGED_JOB = BASEURL + "flagged_jobs_list";


    //  GET Invited Members
    //status, off_set, search , auth headers
    public static final String INVITED_MEMBERS = BASEURL + "invited_members?off_set=0";

    //  GET Recruiter Jobs List
    //job_status, off_set , auth headers
    public static final String RECRUITER_JOBS_LIST = BASEURL + "recruiter_jobs_list?job_status=1&off_set=1";

    //  GET Company Team Members
    //status, off_set, search , auth headers
    public static final String COMPANY_TEAM_MEMBERS = BASEURL + "company_team_members";

    //  GET Admin Members List
    //status, off_set, search , auth headers
    public static final String ADMIN_MEMBERS_LIST = BASEURL + "admin_members_list";

    //  GET Admin Members List
    //off_set, search
    public static final String ADMIN_APPLICANTS_LIST = BASEURL + "admin_applicants_list";

    //  GET Admin Jobs List
    //off_set, search, job_status
    public static final String ADMIN_JOBS_LIST = BASEURL + "admin_jobs_list";

    //  Get Company Size
    public static final String GET_COMPANY_SIZE = BASEURL + "company-sizes?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  Get Company Stage
    public static final String GET_COMPANY_STAGE = BASEURL + "company-stages?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET Company industries
    public static final String GET_COMPANY_INDUSTRY = BASEURL + "industries?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET Company industries
    public static final String GET_RACE = BASEURL + "races?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET Degree List
    public static final String GET_DEGREE_LIST = BASEURL + "degrees?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET Languages List
    public static final String GET_LANGUAGE_LIST = BASEURL + "languages?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET APPLICANT Department
    public static final String GET_APPLICANT_DEPARTMENT = BASEURL + "departments?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET APPLICANT Jobs
    public static final String GET_APPLICANT_JOBS = BASEURL + "jobs?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET APPLICANT Professional Interest
    public static final String GET_APPLICANT_PROFESSIONAL_INTEREST = BASEURL + "professional_interests?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //  GET Recruiter Find Company
    public static final String GET_RECRUITER_FIND_COMPANY = BASEURL + "companies?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5";

    //    POST REQUESTS
    //  POST SIGN IN
    public static final String SIGN_IN = BASEURL + "login";

    //  POST Register Company Team
    public static final String REGISTER_COMPANY_TEAM = BASEURL + "register-company-team";

    //  POST Register UIT Team
    public static final String REGISTER_UIT_TEAM = "register-uit-team";

    //  POST Register Applicant
    public static final String REGISTER_APPLICANT_FOR_RETROFIT = "register-applicant";

    public static final String REGISTER_COMPANY_FOR_RETROFIT = "register-company";


    //  POST Send Invites
    public static final String SEND_INVITES = BASEURL + "send_invites";

    //  POST Add DEI Statement
    public static final String ADD_DEI_STATEMENT = BASEURL + "add_dei_statement";

    //  POST Send Code
    public static final String SEND_CODE = BASEURL + "send_code";

    //  POST Verify Email
    public static final String VERIFY_EMAIL = BASEURL + "verify_email";

    //  POST Add Demographic Info
    public static final String ADD_DEMOGRAPHIC_INFO = BASEURL + "add_demographic_info";

    //  POST Add Professional Info
    public static final String ADD_PROFESSIONAL_INFO = BASEURL + "add_professional_info";

    //  POST Get Profile
    //user_id , token
    public static final String GET_PROFILE = BASEURL + "getProfileById";

    //  POST Register Company Team
    public static final String ACCEPT_COMPANY = BASEURL + "approve_company";

    //  POST Register Company Team
    public static final String REJECT_COMPANY = BASEURL + "reject_company";

    //  POST Reject Company Member
    //team_id , Auth header
    public static final String REJECT_COMPANY_MEMBER = BASEURL + "reject_company_member";

    //  POST Approve Company Member
    //team_id , Auth header
    public static final String APPROVE_COMPANY_MEMBER = BASEURL + "approve_company_member";

    //  POST Cover Letter
    //cover_letter ,token
    public static final String UPLOAD_COVER_LETTER = BASEURL + "upload_cover_letter";

    //  POST Resume
    //resume
    public static final String UPLOAD_RESUME = BASEURL + "upload_resume";

    //  POST Add Job
    //auth header
    public static final String ADD_JOB = BASEURL + "add_job";

    //  POST Feature Job
    //job_id , token
    public static final String FEATURE_JOB = BASEURL + "feature_job";

    //  POST Apply For Job
    //job_id=6 , token
    public static final String POST_APPLY_FOR_JOB = BASEURL + "apply_to_job";

}

