package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.data.WriteReviewData
import retrofit2.Response

class WriteReviewRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun writeReview(
        writeReviewData: WriteReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.writeReviewRepo(writeReviewData, onResponse, onFailure)
    }
}