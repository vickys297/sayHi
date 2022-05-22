package com.example.githubtrendinglist.api

import com.example.githubtrendinglist.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServices {

    private var retrofit: Retrofit.Builder

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(timeout = 30L, TimeUnit.SECONDS)
            .readTimeout(timeout = 30L, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.Network.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())

    }

    companion object {

        private var instance: RetrofitServices? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RetrofitServices().also { instance = it }
            }
    }

    fun <S> createService(serviceClass: Class<S>): S {
        val builder = retrofit.build()
        return builder.create(serviceClass)
    }
}