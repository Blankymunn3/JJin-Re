package com.jjin_re.api

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor(private var userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }

}