package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query

interface RemoteDataSource {

    fun userIdCheck(@Query("user_id") userID: String,
                    onResponse: (Response<SignUpResponse>) -> Unit,
                    onFailure: (Throwable) -> Unit)

    fun loginUserRepo(@Body loginData: LoginData,
                      onResponse: (Response<SignUpResponse>) -> Unit,
                      onFailure: (Throwable) -> Unit)

    fun signUpUserRepo(@Body userModel: UserModel,
                       onResponse: (Response<SignUpResponse>) -> Unit,
                       onFailure: (Throwable) -> Unit)

}