package com.jjin_re.api

import com.jjin_re.utils.BaseApplication
import me.linshen.retrofit2.adapter.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object APIClient {
    const val URL = "http://www.jjin-re.com/"
    private var mRetrofit: Retrofit? = null

    private val instance: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(UserAgentInterceptor("JjinRe/Android " + BaseApplication.appVersion))
                .build()

            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()
            }
            return mRetrofit!!
        }
    val jJinReAPI : JJinReAPI = instance.create(JJinReAPI::class.java)
}