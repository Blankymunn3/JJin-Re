package com.jjin_re.repository

import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.api.SignUpResponse
import retrofit2.Response

class SplashRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun loadUserInfoFromServer(
        userId: String,
        userToken : String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.userIdCheck(userId, userToken, onResponse, onFailure)
    }
}