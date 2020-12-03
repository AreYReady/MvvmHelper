package com.zhixinhuixue.zsyte.xxx.ui.activity

import android.os.Bundle
import com.zhixinhuixue.library.common.base.BaseViewModel
import com.zhixinhuixue.library.common.base.BaseVmActivity
import com.zhixinhuixue.library.common.base.eventViewModel
import com.zhixinhuixue.library.common.ext.toStartActivity
import com.zhixinhuixue.zsyte.xxx.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * ���ߡ�: hegaojian
 * ʱ�䡡: 2020/12/3
 * ������:
 */
class SplashActivity(override val layoutId: Int = R.layout.activity_splash) :BaseVmActivity<BaseViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        jumpToMainActivity()
    }

    /**
     * ��ת��ҳ
     */
    private fun jumpToMainActivity(){
        toStartActivity(MainActivity::class.java)
        finish()
    }

    override fun showToolBar() = false
}