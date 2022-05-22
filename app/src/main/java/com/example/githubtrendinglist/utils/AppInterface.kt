package com.example.githubtrendinglist.utils

import com.example.githubtrendinglist.model.Datum

interface AppInterface {

    interface MediaItemCallback {
        fun onItemClick(item: Datum, position: Int)
    }
}