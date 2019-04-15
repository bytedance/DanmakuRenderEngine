package com.ixigua.common.meteor.render

import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.draw.IDrawItem

/**
 * Created by dss886 on 2018/11/8.
 */
interface IRenderLayer {

    fun getLayerType(): Int

    fun onLayoutSizeChanged(width: Int, height: Int)

    fun addItem(item: IDrawItem<IDanmakuData>)

    fun typesetting(isPlaying: Boolean, configChanged: Boolean = false)

    fun getPreDrawItems(): List<IDrawItem<IDanmakuData>>

    fun clear()

}
