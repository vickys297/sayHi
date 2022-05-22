package com.example.githubtrendinglist.api

import com.example.githubtrendinglist.model.FileUploadResponseModel
import com.example.githubtrendinglist.model.MediaListResponseData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MediaAPI {

    @GET("select.php")
    suspend fun getMediaList(
        @Query("lastFetchId") lastFetchId: String,
    ): Response<MediaListResponseData>

    @Multipart
    @POST("uploader.php")
    suspend fun uploadFile(
        @Part fileToUpload: MultipartBody.Part,
        @Part(value = "fileType") fileType: RequestBody
    ): Response<FileUploadResponseModel>
}