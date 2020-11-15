package com.home.playandroiddemo.home.model

class HomeModel : ArrayList<HomeModel.HomeItem>() {
    data class HomeItem(
        val userId: Int,
        val id: Int,
        val title: String,
        val body: String
    )
}