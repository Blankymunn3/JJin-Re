package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.DefaultResponse
import io.kim_kong.jjin_re.api.PhotoUploadResponse
import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part

class PhotoUploadRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()
    fun photoUpload(
        image: ArrayList<MultipartBody.Part>,
        name: ArrayList<RequestBody>,
        onResponse: (Response<PhotoUploadResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.photoUploadRepo(image, name, onResponse, onFailure)
    }

}