package com.ixigua.common.meteor.render.cache

import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.draw.IDrawItem

/**
 * Created by dss886 on 2019-08-16.
 */
interface IDrawCachePool {

    fun acquire(drawType: Int): IDrawItem<IDanmakuData>?

    fun release(item: IDrawItem<IDanmakuData>)
}