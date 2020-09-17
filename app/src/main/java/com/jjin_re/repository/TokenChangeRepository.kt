package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import retrofit2.Response

class TokenChangeRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun tokenChange(
        userID: String,
        token: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.userTokenChange(userID, token, onResponse, onFailure)
    }

}