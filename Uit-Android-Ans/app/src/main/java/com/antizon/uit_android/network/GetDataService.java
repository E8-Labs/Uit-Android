package com.antizon.uit_android.network;

import com.antizon.uit_android.models.AppliedJobApplicantsResponseModel;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreesListResponseModel;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentsListResponseModel;
import com.antizon.uit_android.models.applicant.filter.GetIndustriesResponse;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
import com.antizon.uit_android.models.applicant.interest.ApplicantInStageResponseModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobFilterResponseModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobsListResponseModel;
import com.antizon.uit_android.models.applicant.skills.ApplicantSkillsResponseModel;
import com.antizon.uit_android.models.chat.CreateChatResponse;
import com.antizon.uit_android.models.chat.GetConversationResponse;
import com.antizon.uit_android.models.chat.GetInboxResponse;
import com.antizon.uit_android.models.chat.SendMessageResponse;
import com.antizon.uit_android.models.comments.CommentsResponseModel;
import com.antizon.uit_android.models.community.AllChannelsResponseModel;
import com.antizon.uit_android.models.community.CommunityFeedsResponseModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.models.company.signup.CompanySignupResponseModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
import com.antizon.uit_android.models.invites.CompanyInvitesResponseModel;
import com.antizon.uit_android.models.jobs.JobDetailsResponseModel;
import com.antizon.uit_android.models.jobs.JobsListResponseModel;
import com.antizon.uit_android.models.profile.ApplicantProfileResponseModel;
import com.antizon.uit_android.models.report.flaguser.ApplicantFlaggedUsersResponseModel;
import com.antizon.uit_android.notifications.models.GetNotificationsResponse;
import com.antizon.uit_android.recruiter.models.GetCompaniesResponseModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GetDataService {

    @FormUrlEncoded
    @POST("forgot_password")
    Call<MainResponseModel> forgotPassword(@Field("email") String email);

    @GET("applicant_home")
    Call<ApplicantHomeResponseModel> getApplicantHome(@Header("Authorization") String token, @Query("lat") String lat, @Query("lang") String lan);

    @GET("filter_jobs")
    Call<ApplicantJobFilterResponseModel> filterJobs(@Header("Authorization") String token,
                                                     @Query("job_title") String job_title,
                                                     @Query("min_salary") String min_salary,
                                                     @Query("max_salary") String max_salary,
                                                     @Query("work_locations[]") String[] work_locations,
                                                     @Query("employment_types[]") String[] employment_types,
                                                     @Query("lat") String lat,
                                                     @Query("lang") String lang);
    @GET("applied_jobs_list?off_set=1")
    Call<ApplicantJobFilterResponseModel> getAppliedJobs(@Header("Authorization") String token, @Query("off_set") String off_set);


    @GET("saved_jobs_list?off_set=1")
    Call<ApplicantJobFilterResponseModel> getSavedJobsList(@Header("Authorization") String token, @Query("off_set") String off_set);



    @POST("admin_dashboard")
    Call<AdminHomeResponseModel> getAdminHome(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("like_post")
    Call<MainResponseModel> likePost(@Header("Authorization") String token, @Field("post_id") String postId);

    @GET("channels_list")
    Call<AllChannelsResponseModel> getAllChannels(@Header("Authorization") String token);

    @GET("community_feed")
    Call<CommunityFeedsResponseModel> getCommunityPosts(@Header("Authorization") String token);

    @GET("community_feed")
    Call<CommunityFeedsResponseModel> getCommunityPostsByChannel(@Header("Authorization") String token, @Query("channel_id") String channel_id);


    @GET("get_post_comments")
    Call<CommentsResponseModel> getPostComments(@Header("Authorization") String token, @Query("post_id") String postId);

    @GET("get_comment_replies")
    Call<CommentsResponseModel> getCommentReplies(@Header("Authorization") String token, @Query("comment_id") String comment_id);


    @FormUrlEncoded
    @POST("comment_on_post")
    Call<MainResponseModel> addComment(@Header("Authorization") String token, @Field("post_id") String postId, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("reply_to_comment")
    Call<MainResponseModel> addReply(@Header("Authorization") String token, @Field("comment_id") String postId, @Field("comment") String comment);


    @FormUrlEncoded
    @POST("like_comment")
    Call<MainResponseModel> likeComment(@Header("Authorization") String token, @Field("comment_id") String comment_id);


    @GET("chat_message_list")
    Call<GetConversationResponse> getUserConversation(@Header("Authorization") String token, @Query("chat_id") String chatId);

    @GET("chat_list")
    Call<GetInboxResponse> getUserInboxList(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("create_chat")
    Call<CreateChatResponse> createChat(@Header("Authorization") String token, @Field("user") String receiverId);

    @FormUrlEncoded
    @POST("delete_chat")
    Call<MainResponseModel> deleteChat(@Header("Authorization") String token, @Field("chat_id") String chatId);

    @FormUrlEncoded
    @POST("send_message")
    Call<SendMessageResponse> sendMessage(@Header("Authorization") String token, @Field("chat_id") String chat_id, @Field("message") String message);

    @Multipart
    @POST("send_message")
    Call<SendMessageResponse> sendMessageWithImage(@Header("Authorization") String token, @Part("chat_id") RequestBody chatId, @Part("message") RequestBody message, @Part MultipartBody.Part file);

    @POST("add_demographic_info")
    Call<MainResponseModel> addDemographicInfo(@Header("Authorization") String token, @Body RequestBody params);

    @POST("update_demographic_info")
    Call<MainResponseModel> updateDemographicInfo(@Header("Authorization") String token, @Body RequestBody params);


    @POST("add_professional_info")
    Call<MainResponseModel> addProfessionalInfo(@Header("Authorization") String token, @Body RequestBody params);

    @POST("edit_professional_info")
    Call<MainResponseModel> updateProfessionalInfo(@Header("Authorization") String token, @Body RequestBody params);


    @Multipart
    @POST("add_community_post")
    Call<MainResponseModel> uploadImagePostForCommunity(@Header("Authorization") String token,
                                                        @Part("post_title") RequestBody post_title,
                                                        @Part("post_description") RequestBody post_description,
                                                        @Part("post_url") RequestBody post_url,
                                                        @Part("channels[]") RequestBody[] channelsList,
                                                        @Part MultipartBody.Part file);

    @Multipart
    @POST("edit_community_post")
    Call<MainResponseModel> updatePostImageForCommunity(@Header("Authorization") String token,
                                                        @Part("post_id") RequestBody post_id,
                                                        @Part("post_title") RequestBody post_title,
                                                        @Part("post_description") RequestBody post_description,
                                                        @Part("post_url") RequestBody post_url,
                                                        @Part("channels[]") RequestBody[] channelsList,
                                                        @Part MultipartBody.Part file
    );

    @Multipart
    @POST("add_community_post")
    Call<MainResponseModel> uploadVideoPostForCommunity(@Header("Authorization") String token,
                                                        @Part("post_title") RequestBody post_title,
                                                        @Part("post_description") RequestBody post_description,
                                                        @Part("post_url") RequestBody post_url,
                                                        @Part("channels[]") RequestBody[] channelsList,
                                                        @Part MultipartBody.Part videoFile,
                                                        @Part MultipartBody.Part imageFile);

    @Multipart
    @POST("edit_community_post")
    Call<MainResponseModel> updateVideoPostForCommunity(@Header("Authorization") String token,
                                                        @Part("post_id") RequestBody post_id,
                                                        @Part("post_title") RequestBody post_title,
                                                        @Part("post_description") RequestBody post_description,
                                                        @Part("post_url") RequestBody post_url,
                                                        @Part("channels[]") RequestBody[] channelsList,
                                                        @Part MultipartBody.Part videoFile,
                                                        @Part MultipartBody.Part imageFile);


    @FormUrlEncoded
    @POST("edit_community_post")
    Call<MainResponseModel> updatePostInfo(@Header("Authorization") String token,
                                           @Field("post_id") String post_id,
                                           @Field("post_title") String post_title,
                                           @Field("post_description") String post_description,
                                           @Field("post_url") String post_url,
                                           @Field("channels[]") List<String> channelsList
    );


    @Multipart
    @POST("add_channel")
    Call<MainResponseModel> addCommunityChannel(@Header("Authorization") String token,
                                                @Part("name") RequestBody name,
                                                @Part MultipartBody.Part channel_image);

    @FormUrlEncoded
    @POST("edit_channel")
    Call<MainResponseModel> updateCommunityChannelName(@Header("Authorization") String token,
                                                       @Field("channel_id") String channel_id,
                                                       @Field("name") String name
    );

    @Multipart
    @POST("edit_channel")
    Call<MainResponseModel> updateCommunityChannelMedia(@Header("Authorization") String token,
                                                        @Part("channel_id") RequestBody channel_id,
                                                        @Part("name") RequestBody name,
                                                        @Part MultipartBody.Part channel_image);


    @GET("industries?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<GetIndustriesResponse> getIndustries();

    @GET("degrees?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDegreesListResponseModel> getDegreesList();

    @GET("languages?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDegreesListResponseModel> getLanguagesList();

    @GET("job_experience_titles?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDegreesListResponseModel> getCompanyIndustriesList();

    @GET("departments?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDepartmentsListResponseModel> getDepartmentsList();

    @GET("departments?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDegreesListResponseModel> getSelectWorkExperienceDepartmentList();

    @GET("jobs?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantJobsListResponseModel> getApplicantJobsList();

    @GET("company-stages?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantInStageResponseModel> getCompanyStagesList();

    @GET("company-sizes?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantInStageResponseModel> getCompanySizesList();

    @GET("professional_interests?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantInStageResponseModel> getProfessionInterestsList();

    @GET("skills?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantSkillsResponseModel> getApplicantSkills();

    @FormUrlEncoded
    @POST("flag_job")
    Call<MainResponseModel> flagJob(@Header("Authorization") String token, @Field("job_id") String jobId, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("flag_user")
    Call<MainResponseModel> flagUser(@Header("Authorization") String token, @Field("user_id") String user_id, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("flag_post")
    Call<MainResponseModel> flagPost(@Header("Authorization") String token, @Field("post_id") String user_id);


    @Multipart
    @POST("upload_resume")
    Call<MainResponseModel> uploadApplicantResume(@Header("Authorization") String token,
                                                  @Part MultipartBody.Part resume);

    @Multipart
    @POST("upload_cover_letter")
    Call<MainResponseModel> uploadApplicantCoverLetter(@Header("Authorization") String token,
                                                       @Part MultipartBody.Part resume);

    @FormUrlEncoded
    @POST("getProfileById")
    Call<ApplicantProfileResponseModel> getApplicantProfile(@Header("Authorization") String token, @Field("user_id") String user_id);

    @Multipart
    @POST("update_profile")
    Call<MainResponseModel> updateApplicantProfileImage(@Header("Authorization") String token, @Part MultipartBody.Part profile_image);

    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateApplicantProfileBio(@Header("Authorization") String token, @Field("bio") String bio);

    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateApplicantProfileEmail(@Header("Authorization") String token, @Field("email") String email);

    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateUserPhoneNumber(@Header("Authorization") String token, @Field("phone_number") String PhoneNumber);


    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateUserJobTitle(@Header("Authorization") String token, @Field("job_title") String jobTitle);


    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateCompanyStage(@Header("Authorization") String token, @Field("stages[]") List<String> companyStagesList);

    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateCompanySize(@Header("Authorization") String token, @Field("sizes[]") List<String> companySizeList);


    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateCompanyWebsite(@Header("Authorization") String token, @Field("website") String website);


    @Multipart
    @POST("register-company")
    Call<CompanySignupResponseModel> registerCompany(@Part("name") RequestBody name,
                                                     @Part("email") RequestBody email,
                                                     @Part("password") RequestBody password,
                                                     @Part("stage") RequestBody stage,
                                                     @Part("size") RequestBody size,
                                                     @Part("bio") RequestBody bio,
                                                     @Part("industries[]") RequestBody[] industriesList,
                                                     @Part("lat") RequestBody lat,
                                                     @Part("lang") RequestBody lang,
                                                     @Part("city") RequestBody city,
                                                     @Part("state") RequestBody state,
                                                     @Part("website") RequestBody website,
                                                     @Part MultipartBody.Part profile_image);


    @FormUrlEncoded
    @POST("update_profile")
    Call<MainResponseModel> updateGreenHouseToken(@Header("Authorization") String token, @Field("greenhouse_access_token") String greenhouse_access_token);

    @POST("add_dei_statement")
    Call<MainResponseModel> addDeiStatement(@Header("Authorization") String token, @Body RequestBody params);

    @POST("update_dei")
    Call<MainResponseModel> updateDeiStatement(@Header("Authorization") String token, @Body RequestBody params);

    @POST("add_job")
    Call<MainResponseModel> addJob(@Header("Authorization") String token, @Body RequestBody params);

    @POST("edit_job")
    Call<MainResponseModel> editJob(@Header("Authorization") String token, @Body RequestBody params);

    @FormUrlEncoded
    @POST("save_job")
    Call<MainResponseModel> saveThisJob(@Header("Authorization") String token, @Field("job_id") String job_id);

    @FormUrlEncoded
    @POST("feature_job")
    Call<MainResponseModel> featureThisJob(@Header("Authorization") String token, @Field("job_id") String job_id);

    @POST("send_invites")
    Call<MainResponseModel> sendCompanyInvites(@Header("Authorization") String token, @Body RequestBody params);

    @FormUrlEncoded
    @POST("getProfileById")
    Call<CompanyProfileResponseModel> getCompanyProfile(@Header("Authorization") String token, @Field("user_id") String user_id);


    // Recruiter Account Work

    @GET("companies?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<GetCompaniesResponseModel> getCompaniesList(@Query("off_set") String off_set);

    @GET("companies?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<GetCompaniesResponseModel> searchCompaniesList(@Query("search") String search);


    @GET("companies?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<GetCompaniesResponseModel> getAdminCompanies(@Query("account_status") String account_status, @Query("off_set") String off_set);

    @GET("admin_applicants_list")
    Call<GetAdminApplicantsResponseModel> getAdminApplicantsList(@Header("Authorization") String token, @Query("off_set") String off_set);

    @GET("admin_all_company_members_list")
    Call<GetAdminApplicantsResponseModel> getAdminAllCompanyMembersList(@Header("Authorization") String token, @Query("off_set") String off_set);

    @GET("admin_members_list")
    Call<GetAdminApplicantsResponseModel> getUitMembersList(@Header("Authorization") String token, @Query("account_status") String account_status, @Query("off_set") String off_set);

    @GET("company_team_members")
    Call<GetAdminApplicantsResponseModel> getCompanyMembersList(@Header("Authorization") String token, @Query("account_status") String account_status);

    @GET("invited_members")
    Call<CompanyInvitesResponseModel> getCompanyInvitedMembersList(@Header("Authorization") String token);


    @GET("flagged_users_list")
    Call<ApplicantFlaggedUsersResponseModel> getFlaggedApplicants(@Header("Authorization") String token, @Query("role") String role);


    @FormUrlEncoded
    @POST("approve_company")
    Call<MainResponseModel> approveCompany(@Header("Authorization") String token, @Field("company_id") String company_id);

    @FormUrlEncoded
    @POST("reject_company")
    Call<MainResponseModel> rejectCompany(@Header("Authorization") String token, @Field("company_id") String company_id);

    @FormUrlEncoded
    @POST("pause_profile")
    Call<MainResponseModel> pauseCompany(@Header("Authorization") String token, @Field("user_id") String company_id);

    @FormUrlEncoded
    @POST("delete_user")
    Call<MainResponseModel> deleteUser(@Header("Authorization") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("delete_channel")
    Call<MainResponseModel> deleteChannel(@Header("Authorization") String token, @Field("channel_id") String channel_id);

    @FormUrlEncoded
    @POST("delete_post")
    Call<MainResponseModel> deletePost(@Header("Authorization") String token, @Field("post_id") String post_id);


    @Multipart
    @POST("register-company-team")
    Call<MainResponseModel> registerCompanyTeamMember(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("phone") RequestBody phone,
            @Part("name") RequestBody name,
            @Part("job_title") RequestBody job_title,
            @Part("company_id") RequestBody company_id,
            @Part MultipartBody.Part profile_image);


    @FormUrlEncoded
    @POST("approve_company_member")
    Call<MainResponseModel> approveCompanyMember(@Header("Authorization") String token, @Field("team_id") String team_id);

    @FormUrlEncoded
    @POST("reject_company_member")
    Call<MainResponseModel> rejectCompanyMember(@Header("Authorization") String token, @Field("team_id") String team_id);

    @GET("my_jobs_list")
    Call<JobsListResponseModel> getCompanyJobsList(@Header("Authorization") String token, @Query("job_status") String job_status);

    @GET("admin_jobs_list")
    Call<JobsListResponseModel> getAdminJobsList(@Header("Authorization") String token, @Query("job_status") String job_status);

    @GET("flagged_jobs_list")
    Call<JobsListResponseModel> getFlaggedJobsList(@Header("Authorization") String token);

    @GET("admin_jobs_list")
    Call<JobsListResponseModel> getCompanyRecruiterJobsList(@Header("Authorization") String token, @Query("job_status") String job_status, @Query("user_id") String user_id);


    @GET("recruiter_jobs_list")
    Call<JobsListResponseModel> getRecruiterJobsList(@Header("Authorization") String token, @Query("job_status") String job_status);

    @GET("recruiter_jobs_list")
    Call<JobsListResponseModel> getRecruiterJobsListById(@Header("Authorization") String token, @Query("job_status") String job_status, @Query("user_id") String user_id);

    @GET("my_jobs_list")
    Call<JobsListResponseModel> getCompanyJobsListById(@Header("Authorization") String token, @Query("job_status") String job_status, @Query("user_id") String user_id);

    @GET("get_job_detail")
    Call<JobDetailsResponseModel> getJobDetails(@Header("Authorization") String token, @Query("job_id") String job_id);

    @GET("job_applicants")
    Call<AppliedJobApplicantsResponseModel> getFilledJobApplicantsList(@Header("Authorization") String token, @Query("job_id") String job_id);


    @FormUrlEncoded
    @POST("close_job")
    Call<MainResponseModel> closeJob(@Header("Authorization") String token, @Field("job_id") String job_id);

    @FormUrlEncoded
    @POST("reject_applicant")
    Call<MainResponseModel> rejectApplicant(@Header("Authorization") String token, @Field("job_id") String job_id, @Field("applicant_id") String applicant_id);

    @FormUrlEncoded
    @POST("hire_applicant")
    Call<MainResponseModel> hireApplicant(@Header("Authorization") String token, @Field("job_id") String job_id, @Field("applicant_id") String applicant_id);

    @FormUrlEncoded
    @POST("hire_applicant")
    Call<MainResponseModel> hireAndCloseApplicant(@Header("Authorization") String token, @Field("job_id") String job_id, @Field("applicant_id") String applicant_id, @Field("close_after_hire") String close_after_hire);

    @GET("notifications")
    Call<GetNotificationsResponse> getNotifications(@Header("Authorization") String token, @Query("off_set") String off_set);

    @FormUrlEncoded
    @POST("seen_notification")
    Call<MainResponseModel> seenNotification(@Header("Authorization") String token, @Field("notification_id") String id);

    @FormUrlEncoded
    @POST("contactus")
    Call<MainResponseModel> contactUit(@Header("Authorization") String token, @Field("title") String title, @Field("description") String description);


}
