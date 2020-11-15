package com.home.playandroiddemo.common.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.home.playandroiddemo.R
import com.home.playandroiddemo.home.view.HomeFragment
import com.home.playandroiddemo.main.viewModel.MainViewModel
import com.home.playandroiddemo.store.StoreFragment

abstract class BaseComponent @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    layoutId: Int
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var fragmentManager: FragmentManager? = null
    private var fragmentList: ArrayList<Fragment>? = null
    private lateinit var mainViewModel: MainViewModel
    private var currentFragment: Fragment? = null
    private val mHomeFragment: HomeFragment
            by lazy { HomeFragment.getInstance() }
    private val mProjectFragment: StoreFragment
            by lazy { StoreFragment.getInstance() }
    private val mObjectListFragment: StoreFragment
            by lazy { StoreFragment.getInstance() }
    private val mProfileFragment: StoreFragment
            by lazy { StoreFragment.getInstance() }

    init {
        initView(View.inflate(context, layoutId, this))
    }

    abstract fun initView(view: View)

    fun init(manager: FragmentManager?, viewModel: MainViewModel) {
        fragmentManager = manager
        mainViewModel = viewModel
        if (fragmentList == null) {
            fragmentList = arrayListOf()
            fragmentList?.let {
                it.add(getCurrentFragment(0)!!)
                it.add(getCurrentFragment(1)!!)
                it.add(getCurrentFragment(2)!!)
                it.add(getCurrentFragment(3)!!)
            }
        }
        showFragment(viewModel.getPage() ?: 0)
    }

    private fun getCurrentFragment(index: Int): Fragment? {
        return when (index) {
            0 -> mHomeFragment
            1 -> mProjectFragment
            2 -> mObjectListFragment
            3 -> mProfileFragment
            else -> null
        }
    }

    protected open fun showFragment(position: Int) {
        mainViewModel.setPage(position)
        val targetFg: Fragment = fragmentList!![position]
        val transaction = fragmentManager!!.beginTransaction()
        if (currentFragment != null) transaction.hide(currentFragment!!)
        if (!targetFg.isAdded) {
            transaction.add(R.id.frame_layout_fragment, targetFg).commit()
        } else {
            transaction.show(targetFg).commit()
        }
        currentFragment = targetFg
    }

    open fun destroy() {
        if (null != fragmentManager) {
            if (!fragmentManager!!.isDestroyed) fragmentManager = null
        }
        if (!fragmentList.isNullOrEmpty()) {
            fragmentList?.clear()
            fragmentList = null
        }
    }
}