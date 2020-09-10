package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.data.WriteReviewData
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.repository.DownloadReviewListRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.Query

interface RemoteDataSource {

    fun userIdCheck(
        @Query("user_id") userID: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun loginUserRepo(
        @Body loginData: LoginData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun signUpUserRepo(
        @Body userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun photoUploadRepo(
        @Part image: ArrayList<MultipartBody.Part>,
        @Part("img") name: ArrayList<RequestBody>,
        onResponse: (Response<PhotoUploadResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun writeReviewRepo(
        writeReviewData: WriteReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )

    fun downloadReviewList(
        userID: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}