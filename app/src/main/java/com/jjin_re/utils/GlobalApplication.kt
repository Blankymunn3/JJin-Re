package com.jjin_re.utils

import android.app.Application
import com.kakao.auth.*


class GlobalApplication : Application() {
    companion object {
        @JvmStatic
        lateinit var instance: GlobalApplication

        @JvmStatic
        fun getGlobalApplicationContext(): GlobalApplication {
            if (!::instance.isInitialized) {
                throw IllegalStateException("This Application does not inherit com.kakao.GlobalApplication")
            }
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@GlobalApplication

        KakaoSDK.init(KakaoSDKAdapter())
    }
    override fun onTerminate() {
        super.onTerminate()
        instance
    }

    class KakaoSDKAdapter: KakaoAdapter() {
        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { getGlobalApplicationContext() }
        }

        override fun getSessionConfig(): ISessionConfig? {
            return object : ISessionConfig {
                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                override fun getApprovalType(): ApprovalType? {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isSaveFormData(): Boolean {
                    return true
                }
            }
        }

    }

}