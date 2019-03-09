package net.appsynth.coroutinestalk.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun buildApiCoroutines(): ProfileApiCoroutines {
    return Retrofit.Builder()
        .baseUrl("http://www.mocky.io/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProfileApiCoroutines::class.java)
}

fun buildApiRx(): ProfileApiRx {
    return Retrofit.Builder()
        .baseUrl("http://www.mocky.io/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ProfileApiRx::class.java)
}