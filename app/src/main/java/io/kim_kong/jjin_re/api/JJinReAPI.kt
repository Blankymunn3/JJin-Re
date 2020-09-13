package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.data.ReadReviewData
import io.kim_kong.jjin_re.data.WriteReviewData
import io.kim_kong.jjin_re.model.AdBannerModel
import io.kim_kong.jjin_re.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface JJinReAPI {
    @POST("/user/id_check")
    fun userIdCheck(@Query("user_id") userID: String) : Call<SignUpResponse>

    @POST("/user/join")
    fun signUpUser(@Body userModel: UserModel): Call<SignUpResponse>

    @POST("/user/login")
    fun loginUser(@Body loginData: LoginData): Call<SignUpResponse>

    @POST("/user/adbanner")
    fun downloadADBannerList(@Query("user_id") userID: String): Call<ADBannerResponse>

    @Multipart
    @POST("/review/photo_upload")
    fun postImage(@Part image: ArrayList<MultipartBody.Part>, @Part("img") name: ArrayList<RequestBody>): Call<PhotoUploadResponse>

    @POST("/review/review_write")
    fun writeReview(@Body writeReviewData: WriteReviewData): Call<DefaultResponse>

    @POST("/review/review_list")
    fun downloadReviewList(@Body readReviewData: ReadReviewData): Call<DownloadReviewListResponse>

    @POST("/review/review_best")
    fun downloadBestReviewList(@Body readReviewData: ReadReviewData): Call<DownloadReviewListResponse>

}