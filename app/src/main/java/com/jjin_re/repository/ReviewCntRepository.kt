package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import retrofit2.Response

class ReviewCntRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun downloadReviewCnt(
        userId: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.downloadReviewCnt(userId, onResponse, onFailure)
    }
}