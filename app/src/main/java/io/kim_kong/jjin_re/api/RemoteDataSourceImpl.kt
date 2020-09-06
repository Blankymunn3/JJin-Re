package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.data.LoginData
import io.kim_kong.jjin_re.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceImpl: RemoteDataSource {
    private val jJinReAPI = APIClient.jJinReAPI
    override fun userIdCheck(userID: String, onResponse: (Response<SignUpResponse>) -> Unit, onFailure: (Throwable) -> Unit) {
        jJinReAPI.userIdCheck(userID).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun loginUserRepo(loginData: LoginData, onResponse: (Response<SignUpResponse>) -> Unit, onFailure: (Throwable) -> Unit) {
        jJinReAPI.loginUser(loginData).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    override fun signUpUserRepo(userModel: UserModel, onResponse: (Response<SignUpResponse>) -> Unit, onFailure: (Throwable) -> Unit) {
        jJinReAPI.signUpUser(userModel).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                onResponse(response)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure(t)
            }

        })
    }
}