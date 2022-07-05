package com.antizon.uit_android.network;

import com.antizon.uit_android.models.applicant.degree.ApplicantDegreesListResponseModel;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentsListResponseModel;
import com.antizon.uit_android.models.applicant.filter.GetIndustriesResponse;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
import com.antizon.uit_android.models.applicant.interest.ApplicantInStageResponseModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobsListResponseModel;
import com.antizon.uit_android.models.chat.CreateChatResponse;
import com.antizon.uit_android.models.chat.GetConversationResponse;
import com.antizon.uit_android.models.chat.GetInboxResponse;
import com.antizon.uit_android.models.chat.SendMessageResponse;
import com.antizon.uit_android.models.comments.CommentsResponseModel;
import com.antizon.uit_android.models.community.AllChannelsResponseModel;
import com.antizon.uit_android.models.community.CommunityFeedsResponseModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.home.AdminHomeResponseModel;
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

    @GET("departments?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantDepartmentsListResponseModel> getDepartmentsList();

    @GET("jobs?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantJobsListResponseModel> getApplicantJobsList();

    @GET("company-stages?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantInStageResponseModel> getCompanyStagesList();

    @GET("company-sizes?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantInStageResponseModel> getCompanySizesList();

    @GET("professional_interests?api_key=48e6c87a_6760_4b07_b987_7128fbea8545_1db5")
    Call<ApplicantInStageResponseModel> getProfessionInterestsList();

    @FormUrlEncoded
    @POST("flag_job")
    Call<MainResponseModel> flagJob(@Header("Authorization") String token, @Field("job_id") String jobId, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("flag_user")
    Call<MainResponseModel> flagUser(@Header("Authorization") String token, @Field("user_id") String user_id, @Field("comment") String comment);

}
