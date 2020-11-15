package com.home.playandroiddemo.common.base

import android.content.Context
import com.zhy.adapter.recyclerview.CommonAdapter

abstract class BaseCommonAdapter<T : Any>(context: Context, layoutId: Int, data: List<T>) :
    CommonAdapter<T>(context, layoutId, data) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}