package com.ixigua.common.meteor.render.cache

import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem

/**
 * Created by dss886 on 2019-08-16.
 */
interface IDrawCachePool {

    fun acquire(drawType: Int): DrawItem<DanmakuData>?

    fun release(item: DrawItem<DanmakuData>)

}