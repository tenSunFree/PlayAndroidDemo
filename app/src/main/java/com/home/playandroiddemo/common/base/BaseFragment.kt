package com.home.playandroiddemo.common.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.blankj.utilcode.util.ConvertUtils
import com.home.playandroiddemo.R
import com.home.playandroiddemo.common.util.showToast

abstract class BaseFragment : Fragment(),
    BaseRequestLifecycle, BaseInit {

    private var loadErrorView: RelativeLayout? = null
    private var badNetworkView: RelativeLayout? = null
    private var noContentView: RelativeLayout? = null
    private var loading: ProgressBar? = null
    private var isFragmentVisible = false

    protected open fun isHaveHeadMargin(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frameLayout = FrameLayout(context!!)
        val lce = View.inflate(context, R.layout.layout_lce, null)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        params.setMargins(
            0,
            if (isHaveHeadMargin()) {
                ConvertUtils.dp2px(if (isPort) 70f else 55f)
            } else 0,
            0,
            0
        )
        lce.layoutParams = params
        val content = inflater.inflate(getLayoutId(), container, false)
        frameLayout.addView(content)
        frameLayout.addView(lce)
        initLceView(lce)
        return frameLayout
    }

    private fun showLoadErrorView(tip: String = getString(R.string.failed_load_data)) {
        loadFinished()
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            val loadErrorText =
                loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
            return
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    fun <T> setDataStatus(
        dataLiveData: LiveData<Result<T>>,
        onBadNetwork: () -> Unit = {},
        onDataStatus: (T) -> Unit
    ) {
        dataLiveData.observe(this) {
            if (it.isSuccess) {
                val dataList = it.getOrNull()
                if (dataList != null) {
                    loadFinished()
                    onDataStatus(dataList)
                } else {
                    showLoadErrorView()
                }
            } else {
                showToast(getString(R.string.bad_network_view_tip))
                showBadNetworkView(View.OnClickListener { initData() })
                onBadNetwork.invoke()
            }
        }
    }

    private fun showBadNetworkView(listener: View.OnClickListener) {
        loadFinished()
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            badNetworkView?.setOnClickListener(listener)
            return
        }
    }

    protected fun showNoContentView(tip: String) {
        loadFinished()
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
            noContentText?.text = tip
            return
        }
    }

    private fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

    private fun hideNoContentView() {
        noContentView?.visibility = View.GONE
    }

    private fun hideBadNetworkView() {
        badNetworkView?.visibility = View.GONE
    }

    private fun initLceView(view: View) {
        loading = view.findViewById(R.id.loading)
        noContentView = view.findViewById(R.id.noContentView)
        badNetworkView = view.findViewById(R.id.badNetworkView)
        loadErrorView = view.findViewById(R.id.loadErrorView)
        if (loading == null) {
            throw NullPointerException("loading is null")
        }
        if (badNetworkView == null) {
            throw NullPointerException("badNetworkView is null")
        }
        if (loadErrorView == null) {
            throw NullPointerException("loadErrorView is null")
        }
    }

    @CallSuper
    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    @CallSuper
    override fun loadFinished() {
        loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    @CallSuper
    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isFragmentVisible = hidden
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isFragmentVisible = isVisibleToUser
        lazyLoad()
    }

    private fun lazyLoad() {
        if (isFragmentVisible) return
        initView()
        initData()
    }
}
