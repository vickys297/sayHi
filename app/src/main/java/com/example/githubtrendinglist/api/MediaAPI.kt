package com.example.githubtrendinglist.api

import com.example.githubtrendinglist.model.MediaListResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaAPI {

    @GET("user_details/select.php")
    suspend fun getMediaList(
        @Query("lastFetchId") lastFetchId: String,
    ): Response<MediaListResponseData>
}