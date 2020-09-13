package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.*
import io.kim_kong.jjin_re.model.AdBannerModel
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