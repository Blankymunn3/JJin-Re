package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface JJinReAPI {

    @POST("/user/id_check")
    fun userIdCheck(@Query("user_id") userID: String) : Call<SignUpResponse>

    @POST("/user/join")
    fun signUpUser(@Body userModel: UserModel): Call<SignUpResponse>

    @POST("/user/login")
    fun loginUser(@Body loginData: LoginData): Call<SignUpResponse>


    @Multipart
    @POST("/user/upload")
    fun postImage(@Part image: MultipartBody.Part, @Part("img") name: RequestBody): Call<DefaultResponse>

}