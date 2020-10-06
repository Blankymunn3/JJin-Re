package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.data.EditReviewData
import com.jjin_re.data.WriteReviewData
import retrofit2.Response

class EditReviewRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun editReview(
        writeReviewData: EditReviewData,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.editReviewRepo(writeReviewData, onResponse, onFailure)
    }
}