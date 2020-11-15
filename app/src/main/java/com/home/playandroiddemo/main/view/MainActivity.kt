package com.home.playandroiddemo.main.view

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import com.home.playandroiddemo.R
import com.home.playandroiddemo.common.util.showToast
import com.home.playandroiddemo.common.base.BaseActivity
import com.home.playandroiddemo.main.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var exitAppTime: Long = 0
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        main_component.init(supportFragmentManager, viewModel)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exitApp() {
        if (System.currentTimeMillis() - exitAppTime > 2000) {
            showToast(getString(R.string.exit_program))
            exitAppTime = System.currentTimeMillis()
        } else {
            exitProcess(0)
        }
    }
}
