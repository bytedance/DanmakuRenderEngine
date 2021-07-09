package com.ixigua.common.meteor.render.cache

import androidx.collection.SparseArrayCompat
import androidx.core.util.Pools
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.utils.DRAW_TYPE_UNDEFINE

/**
 * Created by dss886 on 2019-08-15.
 * Cache Pools for multi-type DrawItem.
 */
class DrawCachePool : IDrawCachePool {

    private val mCachedPool = SparseArrayCompat<Pools.SimplePool<DrawItem<DanmakuData>>>()
    private val mFactoryMap = SparseArrayCompat<IDrawItemFactory>()

    fun registerFactory(factory: IDrawItemFactory) {
        mFactoryMap.put(factory.getDrawType(), factory)
    }

    override fun acquire(drawType: Int): DrawItem<DanmakuData> {
        if (drawType == DRAW_TYPE_UNDEFINE) {
            throw IllegalArgumentException("drawType is DRAW_TYPE_UNDEFINE! Did you forget to " +
                    "define the drawType in your custom DanmakuData?")
        }
        var pool = mCachedPool[drawType]
        if (pool == null) {
            pool = Pools.SimplePool(8)
            mCachedPool.put(drawType, pool)
        }
        @Suppress("UNCHECKED_CAST")
        return pool.acquire()
            ?: mFactoryMap[drawType]?.generateDrawItem() as? DrawItem<DanmakuData>
            ?: throw IllegalArgumentException("Unknown drawType=${drawType}, did you forget to " +
                    "register your custom DanmakuFactory?")
    }

    override fun release(item: DrawItem<DanmakuData>) {
        var pool = mCachedPool[item.getDrawType()]
        if (pool == null) {
            pool = Pools.SimplePool(8)
            mCachedPool.put(item.getDrawType(), pool)
        }
        pool.release(item.apply { recycle() })
    }
}