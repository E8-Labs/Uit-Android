package com.antizon.uit_android.generic_utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class SessionManagement {

    private static final String TAG = SessionManagement.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "UIT";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_ENABLE = "IsEnable";
    public static final String KEY_ID = "idValue";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD_VALUE = "passwordValue";
    public static final String KEY_USER_PROFILE_IMAGE_URL = "profile_image";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_ACCOUNT_STATUS = "account_status";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_PHONE_NUMBER = "phone";
    public static final String KEY_BIO = "bio";
    public static final String KEY_DOB = "dob";
    public static final String KEY_JOB_TITLE = "job_title";
    public static final String KEY_APPLICATION_STATUS = "application_status";
    public static final String KEY_ROLE = "role";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_RESUME_SAVED = "resume_saved";
    public static final String KEY_COVER_LETTER_SAVED = "cover_letter_saved";
    public static final String KEY_PARENT_ID= "parent_id";
    public static final String KEY_PARENT_PROFILE_IMAGE= "parent_profile_image";
    public static final String KEY_PARENT_PROFILE_NAME= "parent_profile_name";
    public static final String KEY_PROFILE_UPDATED= "profile_updated";
    public static final String KEY_RESUME_LINK = "resume_link";
    public static final String KEY_COVER_LETTER_LINK = "cover_letter_link";
    public static final String KEY_UNREAD_NOTIFICATIONS = "unread_notifications";
    public static final String KEY_UNREAD_MESSAGES = "unread_messages";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String idValue, String email, String name, String passwordValue, String website, String profile_image,
                                   String address, String city, String state, String phone,
                                   String account_status, String bio, String dob, String job_title, String role,
                                   String access_token, String application_status, String unread_notifications, String unread_messages) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_ENABLE, true);

        // Storing name in pref

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD_VALUE, passwordValue);
        editor.putString(KEY_ID, idValue);
        editor.putString(KEY_USER_PROFILE_IMAGE_URL, profile_image);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_WEBSITE, website);
        editor.putString(KEY_PHONE_NUMBER, phone);
        editor.putString(KEY_ACCOUNT_STATUS, account_status);
        editor.putString(KEY_APPLICATION_STATUS, application_status);
        editor.putString(KEY_JOB_TITLE, job_title);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_BIO, bio);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_ACCESS_TOKEN, access_token);
        editor.putString(KEY_UNREAD_NOTIFICATIONS, unread_notifications);
        editor.putString(KEY_UNREAD_MESSAGES, unread_messages);

        // commit changes
        editor.commit();

        Log.d(TAG, "createLoginSession: isUserLoggedIn: " + isLoggedIn());
    }

    public boolean checkLogin() {
        // Check login status
        return this.isLoggedIn();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_ID, pref.getString(KEY_ID, null));


        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_PASSWORD_VALUE, pref.getString(KEY_PASSWORD_VALUE, null));
        user.put(KEY_USER_PROFILE_IMAGE_URL, pref.getString(KEY_USER_PROFILE_IMAGE_URL, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));
        user.put(KEY_STATE, pref.getString(KEY_STATE, null));
        user.put(KEY_DOB, pref.getString(KEY_DOB, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
        user.put(KEY_BIO, pref.getString(KEY_BIO, null));
        user.put(KEY_JOB_TITLE, pref.getString(KEY_JOB_TITLE, null));
        user.put(KEY_APPLICATION_STATUS, pref.getString(KEY_APPLICATION_STATUS, null));
        user.put(KEY_ACCOUNT_STATUS, pref.getString(KEY_ACCOUNT_STATUS, null));
        user.put(KEY_PHONE_NUMBER, pref.getString(KEY_PHONE_NUMBER, null));
        user.put(KEY_WEBSITE, pref.getString(KEY_WEBSITE, null));
        user.put(KEY_ACCESS_TOKEN, pref.getString(KEY_ACCESS_TOKEN, null));

        return user;
    }

    public void logoutUser() {

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getKeyCity() {

        return pref.getString(KEY_CITY, null);
    }

    public String getKeyState() {

        return pref.getString(KEY_STATE, null);
    }

    public String getKeyAddress() {

        return pref.getString(KEY_ADDRESS, null);
    }

    public String getKeyId() {
        return pref.getString(KEY_ID, null);

    }

    public String getKeyPhoneNumber() {

        return pref.getString(KEY_PHONE_NUMBER, null);
    }

    public void setKeyPhoneNumber(String phone){
        editor.putString(KEY_PHONE_NUMBER, phone);
        editor.commit();
    }

    public String getKeyPassword() {

        return pref.getString(KEY_PASSWORD_VALUE, null);
    }

    public void setKeyEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getKeyEmail() {
        return pref.getString(KEY_EMAIL, null);
    }
    public String getKeyBio() {

        return pref.getString(KEY_BIO, "");
    }

    public String getRole() {

        return pref.getString(KEY_ROLE, "");
    }

    public String getToken() {

        return pref.getString(KEY_ACCESS_TOKEN, "");
    }

    public void setProfileImage(String profileImage) {
        editor.putString(KEY_USER_PROFILE_IMAGE_URL, profileImage);
        editor.commit();
    }

    public String getProfileImage() {

        return pref.getString(KEY_USER_PROFILE_IMAGE_URL, "");
    }

    public String getUserName() {
        return pref.getString(KEY_NAME, "");
    }

    public String getKeyJobTitle() {
        return pref.getString(KEY_JOB_TITLE, "");
    }

    public void setKeyJobTitle(String jobTitle) {
        editor.putString(KEY_JOB_TITLE, "");
        editor.commit();
    }

    public String getKeyApplicationStatus(){
        return pref.getString(KEY_APPLICATION_STATUS, "");
    }

    public void setApplicationStatus(String account_status){
        editor.putString(KEY_APPLICATION_STATUS, account_status);
        editor.commit();
    }

    public String getKeyResumeSaved(){
        return pref.getString(KEY_RESUME_SAVED, "");
    }

    public void setResumeSaved(String resumeSaved){
        editor.putString(KEY_RESUME_SAVED, resumeSaved);
        editor.commit();
    }

    public String getKeyCoverLetterSaved(){
        return pref.getString(KEY_COVER_LETTER_SAVED, "");
    }

    public void setCoverLetterSaved(String coverLetterSaved){
        editor.putString(KEY_COVER_LETTER_SAVED, coverLetterSaved);
        editor.commit();
    }

    public String getKeyParentId(){
        return pref.getString(KEY_PARENT_ID, "");
    }

    public void setKeyParentId(String coverLetterSaved){
        editor.putString(KEY_PARENT_ID, coverLetterSaved);
        editor.commit();
    }

    public String getKeyParentProfileImage(){
        return pref.getString(KEY_PARENT_PROFILE_IMAGE, "");
    }

    public void setKeyParentProfileImage(String coverLetterSaved){
        editor.putString(KEY_PARENT_PROFILE_IMAGE, coverLetterSaved);
        editor.commit();
    }

    public String getKeyParentProfileName(){
        return pref.getString(KEY_PARENT_PROFILE_NAME, "");
    }

    public void setKeyParentProfileName(String coverLetterSaved){
        editor.putString(KEY_PARENT_PROFILE_NAME, coverLetterSaved);
        editor.commit();
    }

    public String getKeyProfileUpdated(){
        return pref.getString(KEY_PROFILE_UPDATED, "");
    }

    public void setKeyProfileUpdated(String coverLetterSaved){
        editor.putString(KEY_PROFILE_UPDATED, coverLetterSaved);
        editor.commit();
    }

    public String getKeyResumeLink(){
        return pref.getString(KEY_RESUME_LINK, "");
    }

    public void setKeyResumeLink(String resumeLink){
        editor.putString(KEY_RESUME_LINK, resumeLink);
        editor.commit();
    }

    public String getKeyCoverLetterLink(){
        return pref.getString(KEY_COVER_LETTER_LINK, "");
    }

    public void setKeyCoverLetterLink(String coverLetterLink){
        editor.putString(KEY_COVER_LETTER_LINK, coverLetterLink);
        editor.commit();
    }

    public String getKeyUnreadNotifications(){
        return pref.getString(KEY_UNREAD_NOTIFICATIONS, "");
    }

    public void setKeyUnreadNotification(String unread_notifications){
        editor.putString(KEY_UNREAD_NOTIFICATIONS, unread_notifications);
        editor.commit();
    }

}
