package net.appsynth.coroutinestalk.api

import io.reactivex.Single
import net.appsynth.coroutinestalk.models.Profile
import net.appsynth.coroutinestalk.models.UserActivity
import retrofit2.http.GET

interface ProfileApiRx {
    @GET("5c83926b30000066156b0c94")
    fun getProfile(): Single<Profile>

    @GET("5c83a664300000ed186b0cb8")
    fun getUserActivity(): Single<List<UserActivity>>
}