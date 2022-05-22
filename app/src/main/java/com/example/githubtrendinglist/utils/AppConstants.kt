package com.example.githubtrendinglist.utils

object AppConstants {
    object Network {
        const val VERIFY_WEBSITE: String = "https://nextgenerationsocialnetwork.com"
        const val BASE_URL = "https://nextgenerationsocialnetwork.com/"
    }

    object NavigationKey {

        const val FILE_KEY: String = "fileKey"
        const val DATUM_KEY: String = "datumKey"
        const val LAST_ITEM_ID: String = "lastItemId"
    }

    object FileType {
        const val FILE_IMAGE = 0
        const val FILE_VIDEO = 1
    }

    object File {
        const val FILE_LIMIT = 2097152000F
    }

    object RequestCode {
        const val READ_EXTERNAL_STORAGE: Int = 100
    }
}