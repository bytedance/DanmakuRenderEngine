package com.ixigua.common.danmaku.render.engine.render.cache

import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem

/**
 * Created by dss886 on 2019-08-16.
 */
interface IDrawCachePool {

    fun acquire(drawType: Int): DrawItem<DanmakuData>?

    fun release(item: DrawItem<DanmakuData>)

}