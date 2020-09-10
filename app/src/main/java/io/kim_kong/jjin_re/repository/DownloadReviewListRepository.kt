package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.DownloadReviewListResponse
import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import retrofit2.Response

class DownloadReviewListRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun downloadReviewList(
        userId: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.downloadReviewList(userId, onResponse, onFailure)
    }
}