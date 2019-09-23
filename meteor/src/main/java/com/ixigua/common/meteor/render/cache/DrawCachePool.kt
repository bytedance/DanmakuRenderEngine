package com.ixigua.common.meteor.render.cache

import android.support.v4.util.Pools
import android.support.v4.util.SparseArrayCompat
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.draw.IDrawItemFactory

/**
 * Created by dss886 on 2019-08-15.
 */
class DrawCachePool : IDrawCachePool {

    private val mCachedPool = SparseArrayCompat<Pools.SimplePool<DrawItem<DanmakuData>>>()
    private val mFactoryMap = SparseArrayCompat<IDrawItemFactory>()

    fun registerFactory(factory: IDrawItemFactory) {
        mFactoryMap.put(factory.getDrawType(), factory)
    }

    override fun acquire(drawType: Int): DrawItem<DanmakuData> {
        var pool = mCachedPool[drawType]
        if (pool == null) {
            pool = Pools.SimplePool(8)
            mCachedPool.put(drawType, pool)
        }
        @Suppress("UNCHECKED_CAST")
        return pool.acquire() ?: mFactoryMap[drawType]?.generateDrawItem() as DrawItem<DanmakuData>
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