package com.ixigua.common.meteor.render.layer.line

import android.graphics.Canvas
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem

/**
 * Created by dss886 on 2019/9/22.
 */
interface IRenderLine {

    fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float)

    fun addItem(playTime:Long, item: DrawItem<DanmakuData>): Boolean

    fun typesetting(playTime:Long, isPlaying: Boolean, configChanged: Boolean = false)

    fun getPreDrawItems(): List<DrawItem<DanmakuData>>

    fun drawLayoutBounds(canvas: Canvas)

    fun clearRender()
    
}