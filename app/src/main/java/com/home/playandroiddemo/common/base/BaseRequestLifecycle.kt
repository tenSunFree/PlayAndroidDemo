package com.home.playandroiddemo.common.base

interface BaseRequestLifecycle {

    fun startLoading()

    fun loadFinished()

    fun loadFailed(msg: String?)
}
