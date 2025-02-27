package me.hsc.mvvmhelper.widget.alpha

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * 在 pressed 和 disabled 时改变 View 的透明度
 */
open class UIAlphaImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), UIAlphaViewInf {
    private val mAlphaViewHelper: UIAlphaViewHelper by lazy {
        UIAlphaViewHelper(this)
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        mAlphaViewHelper.onPressedChanged(this, pressed)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mAlphaViewHelper.onEnabledChanged(this, enabled)
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    override fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        mAlphaViewHelper.setChangeAlphaWhenPress(changeAlphaWhenPress)
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    override fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        mAlphaViewHelper.setChangeAlphaWhenDisable(changeAlphaWhenDisable)
    }
}