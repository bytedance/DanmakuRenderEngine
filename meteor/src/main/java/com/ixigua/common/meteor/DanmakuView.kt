package com.ixigua.common.meteor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ixigua.common.meteor.control.DanmakuController

/**
 * Created by dss886 on 2018/11/6.
 */
class DanmakuView @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    val controller: DanmakuController = DanmakuController(this)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        controller.onLayoutSizeChanged(right - left, bottom - top)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        controller.draw(this, canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (controller.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

}
