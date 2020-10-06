package com.jjin_re.repository

import com.jjin_re.api.ADBannerResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import com.jjin_re.model.AdBannerModel
import retrofit2.Response

class ADBannerRepository  {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun adBannerList(
        userId: String,
        onResponse: (Response<ADBannerResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.downloadADBannerList(userId, onResponse, onFailure)
    }
}