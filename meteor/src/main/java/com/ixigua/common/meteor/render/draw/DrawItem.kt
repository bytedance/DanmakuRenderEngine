package com.ixigua.common.meteor.render.draw

import android.graphics.Canvas
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019-05-13.
 */
abstract class DrawItem<T: DanmakuData> {

    open var data: T? = null
    open var x: Float = 0F
    open var y: Float = 0F
    open var width: Float = 0F
    open var height: Float = 0F
    open var showTime: Long = 0L
    open var showDuration: Long = 0L
    open var isPaused: Boolean = false

    abstract fun getDrawType(): Int

    abstract fun bindData(data: T)

    abstract fun measure(config: DanmakuConfig)

    abstract fun draw(canvas: Canvas, config: DanmakuConfig)

    open fun recycle() {
        data = null
        x = 0F
        y = 0F
        width = 0F
        height = 0F
        showTime = 0L
        showDuration = 0L
        isPaused = false
    }

}