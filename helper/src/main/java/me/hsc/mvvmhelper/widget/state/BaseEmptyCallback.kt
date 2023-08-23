package me.hsc.mvvmhelper.widget.state

import me.hsc.mvvmhelper.R
import me.hsc.mvvmhelper.loadsir.callback.Callback

/**
 * 作者　: hegaojian
 * 时间　: 2020/12/14
 * 描述　:
 */
class BaseEmptyCallback() : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_base_empty
    }

}