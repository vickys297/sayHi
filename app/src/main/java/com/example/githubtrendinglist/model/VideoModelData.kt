package com.example.githubtrendinglist.model

import android.net.Uri

data class VideoModelData(
    val name: String,
    val uri: Uri,
    val duration: Int,
    val size: Int
) {
}