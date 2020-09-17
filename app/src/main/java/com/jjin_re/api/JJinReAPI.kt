package com.jjin_re.api

import com.jjin_re.data.LoginData
import com.jjin_re.data.PushData
import com.jjin_re.data.ReadReviewData
import com.jjin_re.data.WriteReviewData
import com.jjin_re.model.AdBannerModel
import com.jjin_re.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface JJinReAPI {
    @POST("/user/id_check")
    fun userIdCheck(@Query("user_id") userID: String, @Query("user_token")userToken: String) : Call<SignUpResponse>

    @POST("/user/join")
    fun signUpUser(@Body userModel: UserModel): Call<SignUpResponse>

    @POST("/user/login")
    fun loginUser(@Body loginData: LoginData): Call<SignUpResponse>

    @POST("/user/social_login")
    fun socialLoginUser(@Body userModel: UserModel, @Query("login_type") loginType: String): Call<SignUpResponse>

    @POST("/user/token_change")
    fun userChangeToken(@Query("user_id") userID: String, @Query("user_token") userToken: String): Call<DefaultResponse>

    @POST("/user/edit_profile")
    fun userEditProfile(@Body userModel: UserModel): Call<SignUpResponse>

    @POST("/user/push_change")
    fun userPushChange(@Body pushData: PushData): Call<SignUpResponse>

    @POST("/user/adbanner")
    fun downloadADBannerList(@Query("user_id") userID: String): Call<ADBannerResponse>

    @POST("/general/notice")
    fun noticeListFromServer(@Query("user_id") userID: String): Call<NoticeResponse>

    @Multipart
    @POST("/review/photo_upload")
    fun postImage(@Part image: ArrayList<MultipartBody.Part>, @Part("img") name: ArrayList<RequestBody>): Call<PhotoUploadResponse>


    @Multipart
    @POST("/user/profile_upload")
    fun profileUpload(@Part image: MultipartBody.Part, @Part("img") name: RequestBody): Call<DefaultResponse>

    @POST("/review/review_write")
    fun writeReview(@Body writeReviewData: WriteReviewData): Call<DefaultResponse>

    @POST("/review/review_list")
    fun downloadReviewList(@Body readReviewData: ReadReviewData): Call<DownloadReviewListResponse>

    @POST("/review/review_best")
    fun downloadBestReviewList(@Body readReviewData: ReadReviewData): Call<DownloadReviewListResponse>

    @POST("/review/review_cnt")
    fun downloadReviewCnt(@Query("user_id") userID: String): Call<DefaultResponse>

}