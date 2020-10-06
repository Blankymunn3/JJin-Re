package com.jjin_re.api

import com.jjin_re.data.*
import com.jjin_re.model.AdBannerModel
import com.jjin_re.model.UserModel
import com.jjin_re.repository.DownloadReviewListRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceImpl: RemoteDataSource {
    private val jJinReAPI = APIClient.jJinReAPI
    override fun userIdCheck(
        userID: String,
        userToken: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.userIdCheck(userID, userToken).enqueue(object : Callback<SignUpResponse> {
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

    override fun socialLoginUserRepo(
        userModel: UserModel,
        loginType: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.socialLoginUser(userModel, loginType).enqueue(object : Callback<SignUpResponse> {
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

    override fun userTokenChange(
        userID: String,
        userToken: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.userChangeToken(userID, userToken).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
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

    override fun userEditProfile(
        userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.userEditProfile(userModel).enqueue(object : Callback<SignUpResponse> {
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

    override fun userPushChange(
        pushData: PushData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.userPushChange(pushData).enqueue(object : Callback<SignUpResponse> {
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

    override fun profileUploadRepo(
        image: MultipartBody.Part,
        name: RequestBody,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.profileUpload(image, name).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
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

    override fun photoOneUploadRepo(
        image: ArrayList<MultipartBody.Part>,
        name: ArrayList<RequestBody>,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.postOneImage(image, name).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
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

    override fun editReviewRepo(
        writeReviewData: EditReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.editReview(writeReviewData).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun removeReviewRepo(
        uid: String,
        userID: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.removeReview(uid, userID).enqueue(object: Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun downloadReviewList(
        readReviewData: ReadReviewData,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.downloadReviewList(readReviewData).enqueue(object : Callback<DownloadReviewListResponse> {
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

    override fun downloadBestReviewList(
        readReviewData: ReadReviewData,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.downloadBestReviewList(readReviewData).enqueue(object : Callback<DownloadReviewListResponse> {
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

    override fun downloadADBannerList(
        userId: String,
        onResponse: (Response<ADBannerResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.downloadADBannerList(userId).enqueue(object : Callback<ADBannerResponse> {
            override fun onResponse(
                call: Call<ADBannerResponse>,
                response: Response<ADBannerResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<ADBannerResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun downloadNoticeList(
        userId: String,
        onResponse: (Response<NoticeResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.noticeListFromServer(userID = userId).enqueue(object : Callback<NoticeResponse> {
            override fun onResponse(
                call: Call<NoticeResponse>,
                response: Response<NoticeResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun downloadReviewCnt(
        userID: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.downloadReviewCnt(userID).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun reviewMyThumb(
        userID: String,
        uid: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.reviewMyThumb(userID, uid).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                onResponse(response)
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun reviewThumbUp(
        userID: String,
        uid: String,
        userName: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.reviewThumbUp(userID, uid, userName).enqueue(object : Callback<DownloadReviewListResponse> {
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

    override fun reviewThumbDown(
        userID: String,
        uid: String,
        userName: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        jJinReAPI.reviewThumbDown(userID, uid, userName).enqueue(object : Callback<DownloadReviewListResponse> {
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