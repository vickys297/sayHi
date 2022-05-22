package com.example.githubtrendinglist.ui.uploadVideo

import androidx.lifecycle.ViewModel
import com.example.githubtrendinglist.model.FileUploadResponse
import com.example.githubtrendinglist.repository.AppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadVideoViewModel(private  val repository: AppRepository) : ViewModel() {


    suspend fun uploadFile(fileToUpload: MultipartBody.Part, fileType: RequestBody): FileUploadResponse {
       return repository.uploadFile(fileToUpload, fileType)
    }
}