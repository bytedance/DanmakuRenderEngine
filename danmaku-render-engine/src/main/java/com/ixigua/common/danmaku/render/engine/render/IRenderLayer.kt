package com.ixigua.common.danmaku.render.engine.render

import android.graphics.Canvas
import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.cache.IDrawCachePool
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem

/**
 * Created by dss886 on 2018/11/8.
 */
interface IRenderLayer {

    fun init(controller: DanmakuController, cachePool: IDrawCachePool)

    fun getLayerType(): Int

    fun getLayerZIndex(): Int

    /**
     * Calculate the width and height in this callback and rearrange your RenderLines (if has).
     */
    fun onLayoutSizeChanged(width: Int, height: Int)

    /**
     * Items has been measured already.
     */
    fun addItems(playTime: Long, list: List<DrawItem<DanmakuData>>)

    fun releaseItem(item: DrawItem<DanmakuData>)

    fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean = false): Int

    fun drawBounds(canvas: Canvas)

    fun getPreDrawItems(): List<DrawItem<DanmakuData>>

    fun clear()

}