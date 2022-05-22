package com.example.githubtrendinglist.model

data class GalleryListingModel(
    val contentType: Int,
    val name: String = "",
    val data: GalleryModel? = null
)

data class GalleryPagingModel(
    val data: ArrayList<GalleryModel>
)