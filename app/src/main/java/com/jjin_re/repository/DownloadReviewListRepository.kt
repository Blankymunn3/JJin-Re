package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.DownloadReviewListResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.data.ReadReviewData
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


    fun removeReview(
        uid: String,
        userID: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.removeReviewRepo(uid, userID, onResponse, onFailure)
    }

    fun reviewMyThumb(
        userID: String,
        uid: String,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.reviewMyThumb(userID, uid, onResponse, onFailure)
    }

    fun reviewThumbUpAndThumbUp(
        userID: String,
        uid: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.reviewThumbUp(userID, uid, onResponse, onFailure)
    }

    fun reviewThumbUpAndThumbDown(
        userID: String,
        uid: String,
        onResponse: (Response<DownloadReviewListResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.reviewThumbDown(userID, uid, onResponse, onFailure)
    }
}