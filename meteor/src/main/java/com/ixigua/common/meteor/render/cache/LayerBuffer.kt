package com.ixigua.common.meteor.render.cache

import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem
import java.util.*

/**
 * Created by dss886 on 2019/9/22.
 */
class LayerBuffer(private val mConfig: DanmakuConfig,
                  private val mCachePool: IDrawCachePool) {

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

    fun trimBuffer() {
        if (mBufferItems.isEmpty() || mBufferItems.size <= mConfig.common.typesetBufferSize) {
            return
        }
        // TODO: 2019/9/22 @dss886 It will be better to sort it first, to be optimized in the Future.
        while (mBufferItems.size > mConfig.common.typesetBufferSize) {
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
}