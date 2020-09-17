package com.jjin_re.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jjin_re.model.UserModel


class SignUpResponse {
    @SerializedName("code")
    @Expose
    lateinit var code: String

    @SerializedName("message")
    @Expose
    lateinit var message: String

    @SerializedName("data")
    @Expose
    lateinit var data : UserModel
}