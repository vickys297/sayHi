package com.example.githubtrendinglist.model

import java.io.Serializable



data class GalleryModel(
    val id: Long,
    val name: String,
    val size: Int,
    val createdAt: String,
    val fileDataType: Int,
    val mimeType: String,
    val contentUri: String,
    var isSelected: Boolean = false
) : Serializable
