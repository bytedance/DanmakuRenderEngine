package com.ixigua.common.meteor.render

import android.graphics.Canvas
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem

/**
 * Created by dss886 on 2018/11/8.
 */
interface IRenderLayer {

    fun getLayerType(): Int

    fun onLayoutSizeChanged(width: Int, height: Int)

    /**
     * Items has been measured already.
     */
    fun addItems(playTime: Long, list: List<DrawItem<DanmakuData>>)

    fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean = false)

    fun drawLayoutBounds(canvas: Canvas)

    fun getPreDrawItems(): List<DrawItem<DanmakuData>>

    fun clear()

}
