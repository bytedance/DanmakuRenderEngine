package com.ixigua.common.meteor.render

import android.graphics.Canvas
import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.draw.IDrawItem

/**
 * Created by dss886 on 2018/11/8.
 */
interface IRenderLayer {

    fun getLayerType(): Int

    fun onLayoutSizeChanged(width: Int, height: Int)

    fun addItems(list: List<IDrawItem<IDanmakuData>>)

    fun typesetting(isPlaying: Boolean, configChanged: Boolean = false)

    fun drawLayoutBounds(canvas: Canvas)

    fun getPreDrawItems(): List<IDrawItem<IDanmakuData>>

    fun clear()

}
