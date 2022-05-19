package com.example.githubtrendinglist.network

import com.example.githubtrendinglist.model.GitRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("repositories")
    suspend fun searchRepositories(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("q") q: String = "a"
    ): Response<GitRepository>
}