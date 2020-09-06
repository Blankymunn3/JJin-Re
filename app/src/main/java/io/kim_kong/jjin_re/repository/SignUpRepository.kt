package io.kim_kong.jjin_re.repository

import io.kim_kong.jjin_re.api.RemoteDataSource
import io.kim_kong.jjin_re.api.RemoteDataSourceImpl
import io.kim_kong.jjin_re.api.SignUpResponse
import io.kim_kong.jjin_re.model.UserModel
import retrofit2.Response

class SignUpRepository {
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl()

    fun userIdCheck(
        userId: String,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit) {
        remoteDataSource.userIdCheck(userId, onResponse, onFailure)
    }
    fun signUpUser(
        userModel: UserModel,
        onResponse: (Response<SignUpResponse>) -> Unit,
        onFailure: (Throwable) -> Unit)
    {
        remoteDataSource.signUpUserRepo(userModel, onResponse, onFailure)
    }
}