package com.jjin_re.repository

import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.api.SignUpResponse
import com.jjin_re.data.PushData
import retrofit2.Response

class PushChangeRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun pushChange(
        pushData: PushData,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.userPushChange(pushData, onResponse, onFailure)
    }
}