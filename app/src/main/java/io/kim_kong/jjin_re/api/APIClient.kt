package io.kim_kong.jjin_re.api

import io.kim_kong.jjin_re.utils.BaseApplication
import me.linshen.retrofit2.adapter.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object APIClient {
    const val URL = "http://52.79.83.158:3000"
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .addInterceptor(UserAgentInterceptor("Android " + BaseApplication.appVersion))
        .build()

    private var mRetrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()

    val instance: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .addInterceptor(UserAgentInterceptor("Android " + BaseApplication.appVersion))
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
    val jJinReAPI : JJinReAPI = mRetrofit!!.create(JJinReAPI::class.java)
}