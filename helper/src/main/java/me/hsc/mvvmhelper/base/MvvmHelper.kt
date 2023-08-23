package me.hsc.mvvmhelper.base

import android.app.Application
import android.view.Gravity
import com.hjq.toast.ToastUtils
import me.hsc.mvvmhelper.ext.dp
import me.hsc.mvvmhelper.loadsir.callback.SuccessCallback
import me.hsc.mvvmhelper.loadsir.core.LoadSir
import me.hsc.mvvmhelper.util.KtxActivityLifecycleCallbacks
import me.hsc.mvvmhelper.util.mvvmHelperLog
import me.hsc.mvvmhelper.widget.state.BaseEmptyCallback
import me.hsc.mvvmhelper.widget.state.BaseErrorCallback
import me.hsc.mvvmhelper.widget.state.BaseLoadingCallback

/**
 * 作者　: hegaojian
 * 时间　: 2022/1/13
 * 描述　:
 */

/**
 * 全局上下文，可直接拿
 */
val appContext: Application by lazy { MvvmHelper.app }

object MvvmHelper {

    lateinit var app: Application

    /**
     * 框架初始化
     * @param application Application 全局上下文
     * @param debug Boolean  true为debug模式，会打印Log日志 false 关闭Log日志
     */
    fun init(application: Application, debug: Boolean) {
        app = application
        mvvmHelperLog = debug
        //注册全局 activity生命周期监听
        application.registerActivityLifecycleCallbacks(KtxActivityLifecycleCallbacks())
        LoadSir.beginBuilder()
            .setErrorCallBack(BaseErrorCallback())
            .setEmptyCallBack(BaseEmptyCallback())
            .setLoadingCallBack(BaseLoadingCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .commit()
        ToastUtils.init(app)
        ToastUtils.setGravity(Gravity.BOTTOM, 0, 100.dp)
    }
}