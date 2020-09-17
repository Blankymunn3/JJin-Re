package com.jjin_re.repository

import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.api.SignUpResponse
import com.jjin_re.model.UserModel
import retrofit2.Response

class SignUpRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun userIdCheck(
        userId: String,
        userToken: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit) {
        remoteDataSource.userIdCheck(userId, userToken, onResponse, onFailure)
    }
    fun signUpUser(
        userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit)
    {
        remoteDataSource.signUpUserRepo(userModel, onResponse, onFailure)
    }
}