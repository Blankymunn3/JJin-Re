package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import io.kim_kong.jjin_re.api.SignUpResponse
import retrofit2.Response

class SplashRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun loadUserInfoFromServer(
        userId: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.userIdCheck(userId, onResponse, onFailure)
    }
}