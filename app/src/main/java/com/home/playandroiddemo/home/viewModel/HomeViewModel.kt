package com.home.playandroiddemo.home.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.home.playandroiddemo.home.model.HomeModel
import com.home.playandroiddemo.home.model.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _listLiveData = MutableLiveData<String>()
    val listLiveData = ArrayList<HomeModel.HomeItem>()
    val dataStatusLiveData =
        Transformations.switchMap(_listLiveData) { HomeRepository.getArticleList() }

    fun getList() {
        _listLiveData.value = ""
    }
}