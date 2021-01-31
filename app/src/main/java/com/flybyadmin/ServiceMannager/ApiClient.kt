package com.flybyadmin.hoori.controller

import com.flybyadmin.ServiceMannager.URLConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object Factory {
        private var retrofit: Retrofit? = null

        fun getRetrofit(): Retrofit? {
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build()
                retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(URLConstants.BASE_URL_API)
                    .client(okHttpClient)
                    .build()
            }
            return retrofit
        }

    }

}