package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.api.SignUpResponse
import com.jjin_re.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class UserEditProfileRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun userEditProfile(
        userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.userEditProfile(userModel, onResponse, onFailure)
    }

    fun profileUpload(
        image: MultipartBody.Part,
        name: RequestBody,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.profileUploadRepo(image, name, onResponse, onFailure)
    }
}