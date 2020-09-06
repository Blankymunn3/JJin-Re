package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import io.kim_kong.jjin_re.api.SignUpResponse
import io.kim_kong.jjin_re.data.LoginData
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
}