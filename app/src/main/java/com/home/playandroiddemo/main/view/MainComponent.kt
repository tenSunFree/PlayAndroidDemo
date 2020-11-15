package com.home.playandroiddemo.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.home.playandroiddemo.R
import com.home.playandroiddemo.common.base.BaseComponent

class MainComponent @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(context, attrs, defStyleAttr, R.layout.layout_main_component),
    View.OnClickListener {

    private var textViewList: ArrayList<TextView>? = null

    override fun initView(view: View) {
        textViewList = arrayListOf(
            view.findViewById(R.id.text_view_home),
            view.findViewById(R.id.text_view_store),
            view.findViewById(R.id.text_view_mine),
            view.findViewById(R.id.text_view_more)
        )
        for (textView in textViewList!!) {
            textView.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.text_view_home -> showFragment(0)
            R.id.text_view_store -> showFragment(1)
            R.id.text_view_mine -> showFragment(2)
            R.id.text_view_more -> showFragment(3)
        }
    }

    override fun showFragment(currentPosition: Int) {
        super.showFragment(currentPosition)
        for (position in textViewList!!.indices) {
            textViewList!![position].isSelected = currentPosition == position
        }
    }

    override fun destroy() {
        super.destroy()
        if (!textViewList.isNullOrEmpty()) {
            textViewList!!.clear()
            textViewList = null
        }
    }
}