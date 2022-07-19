package com.antizon.uit_android.network;

import com.antizon.uit_android.models.applicant.degree.ApplicantDegreesListResponseModel;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentsListResponseModel;
import com.antizon.uit_android.models.applicant.filter.GetIndustriesResponse;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
import com.antizon.uit_android.models.applicant.interest.ApplicantInStageResponseModel;
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
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.models.company.signup.CompanySignupResponseModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
import com.antizon.uit_android.models.profile.ApplicantProfileResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("applicant_home")
    Call<ApplicantHomeResponseModel> getApplicantHome(@Header("Authorization") String token, @Query("lang") String lang, @Query("lat") String lat);

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
    Call<CommunityFeedsResponseModel> getCommunityPostsByChannel(@Header("Authorization") String token, @Query("search") String search, @Query("channel_id") String channel_id);


    @GET("get_post_comments")
    Call<CommentsResponseModel> getPostComments(@Header("Authorization") String token, @Query("post_id") String postId);

    @FormUrlEncoded
    @POST("comment_on_post")
    Call<MainResponseModel> addComment(@Header("Authorization") String token, @Field("post_id") String postId, @Field("comment") String comment);

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
    Call<CreateChatResponse> deleteChat(@Field("chat_id") String chatId);

    @FormUrlEncoded
    @POST("send_message")
    Call<SendMessageResponse> sendMessage(@Header("Authorization") String token, @Field("chat_id") String chat_id, @Field("message") String message);

    @Multipart
    @POST("send_message")
    Call<SendMessageResponse> sendMessageWithImage(@Header("Authorization") String token, @Part("chat_id") RequestBody chatId, @Part("message") RequestBody message, @Part MultipartBody.Part file);

    @POST("add_demographic_info")
    Call<MainResponseModel> addDemographicInfo(@Header("Authorization") String token, @Body RequestBody params);

    @POST("add_professional_info")
    Call<MainResponseModel> addProfessionalInfo(@Header("Authorization") String token, @Body RequestBody params);

    @Multipart
    @POST("add_community_post")
    Call<MainResponseModel> uploadPostForCommunity(@Header("Authorization") String token,
                                                   @Part("post_title") RequestBody post_title,
                                                   @Part("post_description") RequestBody post_description,
                                                   @Part("post_url") RequestBody post_url,
                                                   @Part("channels[]") RequestBody[] channelsList,
                                                   @Part MultipartBody.Part file);

    @GET("industries?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<GetIndustriesResponse> getIndustries();

    @GET("degrees?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDegreesListResponseModel> getDegreesList();

    @GET("languages?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDegreesListResponseModel> getLanguagesList();

    @GET("industries?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
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

    @POST("add_job")
    Call<MainResponseModel> addJob(@Header("Authorization") String token, @Body RequestBody params);

    @FormUrlEncoded
    @POST("save_job")
    Call<MainResponseModel> saveThisJob(@Header("Authorization") String token, @Field("job_id") String job_id);

    @POST("send_invites")
    Call<MainResponseModel> sendCompanyInvites(@Header("Authorization") String token, @Body RequestBody params);

    @FormUrlEncoded
    @POST("getProfileById")
    Call<CompanyProfileResponseModel> getCompanyProfile(@Header("Authorization") String token, @Field("user_id") String user_id);
    
}
