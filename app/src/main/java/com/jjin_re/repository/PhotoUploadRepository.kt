package com.jjin_re.repository

import com.jjin_re.api.DefaultResponse
import com.jjin_re.api.PhotoUploadResponse
import com.jjin_re.api.RemoteDataSource
import com.jjin_re.api.RemoteDataSourceImpl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

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

    fun photoOneUpload(
        image: ArrayList<MultipartBody.Part>,
        name: ArrayList<RequestBody>,
        onResponse: (Response<DefaultResponse>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        remoteDataSource.photoOneUploadRepo(image, name, onResponse, onFailure)
    }

}