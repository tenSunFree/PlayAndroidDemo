package com.home.playandroiddemo.home.view

import androidx.lifecycle.ViewModelProvider
import com.home.playandroiddemo.R
import com.home.playandroiddemo.common.base.BaseFragment
import com.home.playandroiddemo.home.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    companion object {
        fun getInstance() = HomeFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }
    private lateinit var adapter: HomeAdapter

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        adapter = HomeAdapter(
            context!!,
            viewModel.listLiveData
        )
        recycler_view.adapter = adapter
    }

    override fun initData() {
        startLoading()
        setDataStatus(viewModel.dataStatusLiveData, {
            if (viewModel.listLiveData.size > 0) loadFinished()
        }) {
            if (viewModel.listLiveData.size > 0) viewModel.listLiveData.clear()
            viewModel.listLiveData.addAll(it)
            adapter.notifyDataSetChanged()
        }
        viewModel.getList()
    }
}