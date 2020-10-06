package com.jjin_re.repository

import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.api.SignUpResponse
import com.jjin_re.data.LoginData
import com.jjin_re.model.UserModel
import retrofit2.Response

class LoginRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun loginUser(
        loginData: LoginData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.loginUserRepo(loginData, onResponse, onFailure)
    }

    fun socialLogin(
        userModel: UserModel,
        loginType: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.socialLoginUserRepo(userModel, loginType, onResponse, onFailure)
    }
}