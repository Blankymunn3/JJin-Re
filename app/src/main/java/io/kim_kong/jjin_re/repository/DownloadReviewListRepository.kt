package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.DownloadReviewListResponse
import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import io.kim_kong.jjin_re.data.ReadReviewData
import retrofit2.Response

class DownloadReviewListRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun downloadReviewList(
        readReviewData: ReadReviewData,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.downloadReviewList(readReviewData, onResponse, onFailure)
    }

    fun downloadBestReviewList(
        readReviewData: ReadReviewData,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.downloadBestReviewList(readReviewData, onResponse, onFailure)
    }
}