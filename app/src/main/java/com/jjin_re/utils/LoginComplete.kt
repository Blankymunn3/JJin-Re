package com.jjin_re.utils

interface LoginComplete {
    fun executeAppServiceLogin(
        loginType: String,
        nickname: String,
        email: String,
        imageUrl: String,
        userId: String
    )

}
