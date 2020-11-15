package com.home.playandroiddemo.home.model

import com.home.playandroiddemo.common.remote.NetworkManager
import kotlinx.coroutines.*
import androidx.lifecycle.liveData

object HomeRepository {

    fun getArticleList() = fire {
        coroutineScope {
            val res = arrayListOf<HomeModel.HomeItem>()
            val articleListDeferred =
                async { NetworkManager.getList() }
            val articleList = articleListDeferred.await()
            res.addAll(articleList)
            Result.success(res)
        }
    }
}

fun <T> fire(block: suspend () -> Result<T>) =
    liveData {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }