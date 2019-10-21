package com.ixigua.common.meteor.render.draw

import android.graphics.Canvas
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019-05-13.
 * Define how a danmaku will be drawn, and some drawing related properties.
 * Information of the danmaku properties is in the [DanmakuData].
 * DrawItem will be recycled and reused after disappearing from screen.
 */
abstract class DrawItem<T: DanmakuData> {

    open var data: T? = null

    open var x: Float = 0F
    open var y: Float = 0F
    open var width: Float = 0F
    open var height: Float = 0F

    /**
     * When this item has been put on the screen.
     * Noticed that [showTime] is the play time of video, not a real timestamp.
     */

    open var showTime: Long = 0L
    /**
     * How long this item has been shown on the screen.
     */

    open var showDuration: Long = 0L

    /**
     * If paused, [showDuration] will not increase.
     */
    open var isPaused: Boolean = false

    /**
     * DrawType, DrawItem, and DanmakuData are one-to-one correspondence.
     */
    abstract fun getDrawType(): Int

    /**
     * Tell DrawItem which data should be drawn.
     */
    abstract fun bindData(data: T)

    /**
     * Measure x/y/width/height according to the content of [data].
     */
    abstract fun measure(config: DanmakuConfig)

    /**
     * Actually do the drawing work in main thread.
     */
    abstract fun draw(canvas: Canvas, config: DanmakuConfig)

    /**
     * If you define extra fields in subclass,
     * make sure overriding this method to reset them.
     */
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