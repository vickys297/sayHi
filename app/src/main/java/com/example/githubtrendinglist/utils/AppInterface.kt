package com.example.githubtrendinglist.utils

import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.model.GalleryModel

interface AppInterface {

    interface MediaItemCallback {
        fun onItemClick(item: Datum, position: Int)
    }

    interface GalleryOnItemSelect {
        fun onItemClick(item: GalleryModel)
    }
}