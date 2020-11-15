package com.home.playandroiddemo.home.model

import retrofit2.Call
import retrofit2.http.GET

interface HomeService {

    @GET("posts")
    fun getArticle(): Call<List<HomeModel.HomeItem>>
}