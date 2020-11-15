package com.home.playandroiddemo.home.view

import android.content.Context
import android.view.View
import com.home.playandroiddemo.R
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.home.playandroiddemo.common.base.BaseCommonAdapter
import com.home.playandroiddemo.home.model.HomeModel
import kotlinx.android.synthetic.main.adapter_home.view.*

class HomeAdapter(
    context: Context,
    list: ArrayList<HomeModel.HomeItem>,
    layoutId: Int = R.layout.adapter_home
) : BaseCommonAdapter<HomeModel.HomeItem>(context, layoutId, list) {

    override fun convert(holder: ViewHolder, item: HomeModel.HomeItem, position: Int) {
        if (position == 0) {
            holder.itemView.image_view.visibility = View.VISIBLE
            holder.itemView.card_view.visibility = View.GONE
        } else {
            holder.itemView.image_view.visibility = View.GONE
            holder.itemView.card_view.visibility = View.VISIBLE
        }
        holder.itemView.text_view_title.text = item.title
        holder.itemView.text_view_body.text = item.body
    }
}
