package com.antizon.uit_android.generic.uit_interface;

import com.antizon.uit_android.generic_utils.AppConstants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonPlaceHolderAPI {

//        values = ["name": name, "email": email, "password": password, "profile_image": image,
//                "phone": self.phone ?? "", "dob": self.dob ?? ""]

    @Multipart
    @POST(AppConstants.REGISTER_UIT_TEAM)
    Call<String> registerUitMember(@Part("name") RequestBody name, @Part("email") RequestBody email,
                                   @Part("password") RequestBody password, @Part("phone") RequestBody phone,
                                   @Part MultipartBody.Part file);

//   @Part("job_title") RequestBody job_title this is removed from the registerApplicant

    @Multipart
    @POST(AppConstants.REGISTER_APPLICANT_FOR_RETROFIT)
    Call<String> registerApplicant(@Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("password") RequestBody password,
                                   @Part("phone") RequestBody phone,
                                   @Part("dob") RequestBody dob,
                                   @Part MultipartBody.Part file);

@Multipart
@POST(AppConstants.REGISTER_COMPANY_FOR_RETROFIT)
Call<String> registerCompany(@Part("name") RequestBody name, @Part("email") RequestBody email,
                               @Part("password") RequestBody password, @Part("website") RequestBody website,
                               @Part("lat") RequestBody lat,@Part("lang") RequestBody lang,
                               @Part("city") RequestBody city,@Part("state") RequestBody state,
                               @Part("address") RequestBody address,@Part("bio") RequestBody bio,
                               @Part MultipartBody.Part file);

}
