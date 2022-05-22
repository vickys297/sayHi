package com.example.githubtrendinglist.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class FileUploadResponseModel(
    val result: String,
    val data: ArrayList<ResultData>,
    val message: String,
    val statusCode: String
) {
    data class ResultData(
        val result: String,
        val file_type: String
    )
}


sealed class FileUploadResponse {
    data class Success(val result: FileUploadResponseModel.ResultData) : FileUploadResponse()
    data class Failure(val message: String) : FileUploadResponse()

    sealed class HttpErrorCode : FileUploadResponse() {
        data class Exception(val exception: String) : FileUploadResponse()
    }
}