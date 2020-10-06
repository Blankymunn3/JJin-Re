package com.jjin_re.utils

import android.util.Log
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.OptionalBoolean
import com.kakao.util.exception.KakaoException
import com.jjin_re.model.UserModel


class SessionCallback internal constructor(private val listener: LoginComplete) : ISessionCallback {

    var userID: String = ""
    var userName: String = ""
    var userEmail: String = ""
    var userImg: String = ""
    val userLoginType = "kakao"

    override fun onSessionOpened() {
        requestMe()
    }

    override fun onSessionOpenFailed(exception: KakaoException) {
        Log.e("KAKAO LOGIN :: ", "onSessionOpenFailure: $exception")
    }

    private fun requestMe() {
        UserManagement.getInstance()
            .me(object : MeV2ResponseCallback() {
                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
                }

                override fun onFailure(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "사용자 정보 요청 실패: $errorResult")
                }

                override fun onSuccess(result: MeV2Response?) {
                    result?.let {
                        userID = result.id.toString()
                        val kakaoAccount = result.kakaoAccount
                        if (kakaoAccount != null) {

                            val email = kakaoAccount.email
                            when {
                                email != null -> {
                                    userEmail = email
                                    Log.i("KAKAO_API", "email: $email")
                                } else -> {
                                    userEmail = "${userLoginType}_${result.id}"
                                }
                            }
                            val profile: com.kakao.usermgmt.response.model.Profile? = kakaoAccount.profile
                            when {
                                profile != null -> {
                                    userName = profile.nickname
                                    profile.profileImageUrl?.let {
                                        userImg = it
                                    }
                                }
                                kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE -> {
                                    userName = profile?.nickname.toString()
                                    userImg = profile?.profileImageUrl.toString()
                                }
                                else -> {
                                    userName = "${result.id}"
                                    userImg = ""
                                }
                            }

                            listener.executeAppServiceLogin(userLoginType, userName, userEmail, userImg, userID)
                        }
                    }

                }
            })
    }
}
