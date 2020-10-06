package com.jjin_re.repository

import com.jjin_re.api.ADBannerResponse
import com.jjin_re.api.NoticeResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import retrofit2.Response

class NoticeRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun downloadNoticeList(
        userId: String,
        onResponse: (Response<NoticeResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.downloadNoticeList(userId, onResponse, onFailure)
    }
}