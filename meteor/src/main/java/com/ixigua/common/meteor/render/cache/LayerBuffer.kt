package com.ixigua.common.meteor.render.cache

import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem
import java.util.*

/**
 * Created by dss886 on 2019/9/22.
 */
class LayerBuffer(private val mConfig: DanmakuConfig,
                  private val mCachePool: IDrawCachePool,
                  private var mBufferSize: Int,
                  private var mBufferMaxTime: Long) {

    private val mBufferItems = LinkedList<DrawItem<DanmakuData>>()

    fun addItems(list: List<DrawItem<DanmakuData>>) {
        mBufferItems.addAll(list)
    }

    /**
     * Read and deal with items from the buffer.
     * action: return true if you have processed the item successfully and want to remove it from buffer.
     */
    fun forEach(action: (DrawItem<DanmakuData>) -> Boolean) {
        val iterator = mBufferItems.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (action(next)) {
                iterator.remove()
            }
        }
    }

    fun trimBuffer(playTime: Long) {
        if (mBufferItems.isEmpty() || mBufferItems.size <= mBufferSize) {
            return
        }
        mBufferItems.removeWhen {
            playTime - (it.data?.showAtTime ?: 0L) > mBufferMaxTime
        }
        while (mBufferItems.size > mBufferSize) {
            val item = mBufferItems.minBy {
                @Suppress("UNCHECKED_CAST")
                mConfig.common.bufferDiscardRule.invoke(it.data) as Comparable<Any?>
            }
            item?.let {
                mBufferItems.remove(it)
                mCachePool.release(it)
            }
        }
    }

    fun measureItems() {
        mBufferItems.forEach {
            it.measure(mConfig)
        }
    }

    fun clear() {
        mBufferItems.clear()
    }

    /**
     * Changing the buffer size will clear items in the buffer right now.
     */
    fun onBufferChanged(newSize: Int, maxTime: Long) {
        mBufferItems.forEach {
            mCachePool.release(it)
        }
        mBufferItems.clear()
        mBufferSize = newSize
        mBufferMaxTime = maxTime
    }

    /**
     * Copy from Collection.removeIf(), api 26+.
     */
    private fun <T> MutableList<T>.removeWhen(filter: (T) -> Boolean): Boolean {
        var removed = false
        val each = iterator()
        while (each.hasNext()) {
            if (filter.invoke(each.next())) {
                each.remove()
                removed = true
            }
        }
        return removed
    }
}