package io.kim_kong.jjin_re.repository

import android.hardware.camera2.CaptureFailure
import io.kim_kong.jjin_re.api.DefaultResponse
import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import io.kim_kong.jjin_re.data.WriteReviewData
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