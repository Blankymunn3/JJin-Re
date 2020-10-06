package com.jjin_re.api

import com.jjin_re.data.*
import com.jjin_re.model.UserModel
import com.jjin_re.repository.DownloadReviewListRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.Query

interface RemoteDataSource {

    fun userIdCheck(
        @Query("user_id") userID: String,
        @Query("user_token") userToken: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun loginUserRepo(
        @Body loginData: LoginData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun socialLoginUserRepo(
        @Body userModel: UserModel,
        @Query("login_type") loginType: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun userTokenChange(
        @Query("user_id") userID: String,
        @Query("user_token") userToken: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun signUpUserRepo(
        @Body userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun userEditProfile(
        @Body userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun userPushChange(
        @Body pushData: PushData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit

    )

    fun profileUploadRepo(
        @Part image: MultipartBody.Part,
        @Part("img") name: RequestBody,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun photoUploadRepo(
        @Part image: ArrayList<MultipartBody.Part>,
        @Part("img") name: ArrayList<RequestBody>,
        onResponse: (Response<PhotoUploadResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun photoOneUploadRepo(
        @Part image: ArrayList<MultipartBody.Part>,
        @Part("img") name: ArrayList<RequestBody>,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun writeReviewRepo(
        writeReviewData: WriteReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun editReviewRepo(
        writeReviewData: EditReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun removeReviewRepo(
        @Query("uid") uid: String,
        @Query("user_id") userID: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun downloadReviewList(
        readReviewData: ReadReviewData,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun downloadBestReviewList(
        readReviewData: ReadReviewData,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun downloadADBannerList(
        userId: String,
        onResponse: (Response<ADBannerResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun downloadNoticeList(
        userId: String,
        onResponse: (Response<NoticeResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun downloadReviewCnt(
        userID: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun reviewMyThumb(
        userID: String,
        uid: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun reviewThumbUp(
        userID: String,
        uid: String,
        userName: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun reviewThumbDown(
        userID: String,
        uid: String,
        userName: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}