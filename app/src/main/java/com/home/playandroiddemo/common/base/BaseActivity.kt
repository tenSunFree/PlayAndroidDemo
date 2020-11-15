package com.home.playandroiddemo.common.base

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.blankj.utilcode.util.ConvertUtils
import com.home.playandroiddemo.R
import com.home.playandroiddemo.common.util.AndroidVersion
import com.home.playandroiddemo.common.util.showToast
import com.home.playandroiddemo.common.util.ActivityCollector
import java.lang.ref.WeakReference

abstract class BaseActivity : AppCompatActivity(),
    BaseRequestLifecycle, BaseInit {

    private var loading: ProgressBar? = null
    private var loadErrorView: View? = null
    private var badNetworkView: View? = null
    private var noContentView: View? = null
    private var weakRefActivity: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setContentView(getLayoutId())
        ActivityCollector.add(WeakReference(this))
        weakRefActivity = WeakReference(this)
        initView()
        initData()
    }

    override fun initData() {}

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.remove(weakRefActivity)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViews()
    }

    protected open fun setupViews() {
        val view = View.inflate(this, R.layout.layout_lce, null)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.setMargins(
            0,
            ConvertUtils.dp2px(
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    70f
                } else {
                    55f
                }
            ),
            0,
            0
        )
        addContentView(view, params)
        loading = view.findViewById(R.id.loading)
        noContentView = view.findViewById(R.id.noContentView)
        badNetworkView = view.findViewById(R.id.badNetworkView)
        loadErrorView = view.findViewById(R.id.loadErrorView)
        if (loading == null) Log.e("more", "loading is null")
        if (badNetworkView == null) Log.e("more", "badNetworkView is null")
        if (loadErrorView == null) Log.e("more", "loadErrorView is null")
        loadFinished()
    }

    private fun transparentStatusBar() {
        if (AndroidVersion.hasLollipop()) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun showLoadErrorView(tip: String = getString(R.string.failed_load_data)) {
        loadFinished()
        if (loadErrorView != null) {
            val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
            loadErrorView?.visibility = View.VISIBLE
            return
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

    fun <T> setDataStatus(dataLiveData: LiveData<Result<T>>, onDataStatus: (T) -> Unit) {
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
            }
        }
    }

    protected fun showNoContentView(tip: String) {
        loadFinished()
        val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
        noContentText?.text = tip
        noContentView?.visibility = View.VISIBLE
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

    @CallSuper
    override fun startLoading() {
        hideBadNetworkView()
        hideNoContentView()
        hideLoadErrorView()
        loading?.visibility = View.VISIBLE
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
}
