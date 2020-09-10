package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.data.WriteReviewData
import io.kim_kong.jjin_re.model.UserModel
import io.kim_kong.jjin_re.repository.DownloadReviewListRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part

class RemoteDataSourceImpl: RemoteDataSource {
    private val jJinReAPI = APIClient.jJinReAPI
    override fun userIdCheck(
        userID: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.userIdCheck(userID).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun loginUserRepo(
        loginData: LoginData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.loginUser(loginData).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun signUpUserRepo(
        userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.signUpUser(userModel).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun photoUploadRepo(
        image: ArrayList<MultipartBody.Part>,
        name: ArrayList<RequestBody>,
        onResponse: (Response<PhotoUploadResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.postImage(image, name).enqueue(object : Callback<PhotoUploadResponse> {
            override fun onResponse(call: Call<PhotoUploadResponse>, response: Response<PhotoUploadResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<PhotoUploadResponse>, t: Throwable) {
                onFailure(t)

            }
        })
    }

    override fun writeReviewRepo(
        writeReviewData: WriteReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.writeReview(writeReviewData).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun downloadReviewList(
        userID: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.downloadReviewList(userID).enqueue(object : Callback<DownloadReviewListResponse> {
            override fun onResponse(
                call: Call<DownloadReviewListResponse>,
                response: Response<DownloadReviewListResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DownloadReviewListResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }
}