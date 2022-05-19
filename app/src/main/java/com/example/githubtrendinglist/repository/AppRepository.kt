package com.example.githubtrendinglist.repository

import com.example.githubtrendinglist.network.Api
import com.example.githubtrendinglist.network.RetrofitServices

class AppRepository {

    fun searchRepository(searchString:String){
        val service = RetrofitServices.getInstance().createService(Api::class.java)

    }
}