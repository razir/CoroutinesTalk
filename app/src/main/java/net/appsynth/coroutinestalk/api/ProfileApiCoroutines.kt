package net.appsynth.coroutinestalk.api

import net.appsynth.coroutinestalk.models.Profile
import net.appsynth.coroutinestalk.models.UserActivity
import retrofit2.http.GET

interface ProfileApiCoroutines {
    @GET("5c83926b30000066156b0c94")
    suspend fun getProfile(): Profile

    @GET("5c83a664300000ed186b0cb8")
    suspend fun getUserActivity(): List<UserActivity>
}