package com.home.playandroiddemo.common.util

import com.blankj.utilcode.util.ToastUtils

fun showToast(msg: String) {
    ToastUtils.showShort(msg)
}

fun showLongToast(msg: String) {
    ToastUtils.showLong(msg)
}