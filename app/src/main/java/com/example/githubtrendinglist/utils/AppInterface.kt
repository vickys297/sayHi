package com.example.githubtrendinglist.utils

import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.model.GalleryModel

interface AppInterface {

    interface MediaItemCallback {
        fun onItemClick(item: Datum, position: Int)
        fun onDeleteItem(item: Datum, bindingAdapterPosition: Int)
    }

    interface GalleryOnItemSelect {
        fun onItemClick(item: GalleryModel)
    }
}