package com.example.githubtrendinglist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubtrendinglist.api.MediaAPI
import com.example.githubtrendinglist.api.RetrofitServices
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.model.FileUploadResponse
import com.example.githubtrendinglist.model.MediaListResponse
import com.example.githubtrendinglist.repository.dataSource.MediaListPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException

class AppRepository {

    private val mediaAPI = RetrofitServices.getInstance().createService(MediaAPI::class.java)
    fun getMediaListRepository(): Flow<PagingData<Datum>> {
        return Pager(
            PagingConfig(pageSize = 10)
        ) {
            MediaListPagingSource(mediaAPI)
        }.flow
    }


    suspend fun getFixedSizeMediaList(lastItemId: String): MediaListResponse {
        return try {
            val response = mediaAPI.getMediaList(lastItemId)
            if (response.isSuccessful) {
                val data = response.body()
                if (data == null) {
                    MediaListResponse.Failure("No data found")
                } else {
                    MediaListResponse.Success(data.data!!)
                }
            } else {
                when (response.code()) {
                    400 -> MediaListResponse.HttpErrorCode.Exception("Bad Request")
                    403 -> MediaListResponse.HttpErrorCode.Exception("Access to resource is forbidden")
                    404 -> MediaListResponse.HttpErrorCode.Exception("Resource not found")
                    500 -> MediaListResponse.HttpErrorCode.Exception("Internal server error")
                    502 -> MediaListResponse.HttpErrorCode.Exception("Bad Gateway")
                    301 -> MediaListResponse.HttpErrorCode.Exception("Resource has been removed permanently")
                    302 -> MediaListResponse.HttpErrorCode.Exception("Resource moved, but has been found")
                    else -> MediaListResponse.HttpErrorCode.Exception("Something went wrong")
                }
            }

        } catch (e: Exception) {
            MediaListResponse.HttpErrorCode.Exception(e.message!!)
        } catch (e: IOException) {
            MediaListResponse.HttpErrorCode.Exception(e.message!!)
        }
    }

    suspend fun uploadFile(
        fileToUpload: MultipartBody.Part,
        fileType: RequestBody
    ): FileUploadResponse {
        return try {
            val response = mediaAPI.uploadFile(fileToUpload, fileType)
            if (response.isSuccessful) {
                val data = response.body()
                if (data == null) {
                    FileUploadResponse.Failure("No data found")
                } else {
                    FileUploadResponse.Success(data.data[0])
                }
            } else {
                when (response.code()) {
                    400 -> FileUploadResponse.HttpErrorCode.Exception("Bad Request")
                    403 -> FileUploadResponse.HttpErrorCode.Exception("Access to resource is forbidden")
                    404 -> FileUploadResponse.HttpErrorCode.Exception("Resource not found")
                    500 -> FileUploadResponse.HttpErrorCode.Exception("Internal server error")
                    502 -> FileUploadResponse.HttpErrorCode.Exception("Bad Gateway")
                    301 -> FileUploadResponse.HttpErrorCode.Exception("Resource has been removed permanently")
                    302 -> FileUploadResponse.HttpErrorCode.Exception("Resource moved, but has been found")
                    else -> FileUploadResponse.HttpErrorCode.Exception("Something went wrong")
                }
            }

        } catch (e: Exception) {
            FileUploadResponse.HttpErrorCode.Exception(e.message!!)
        } catch (e: IOException) {
            FileUploadResponse.HttpErrorCode.Exception(e.message!!)
        }
    }


}