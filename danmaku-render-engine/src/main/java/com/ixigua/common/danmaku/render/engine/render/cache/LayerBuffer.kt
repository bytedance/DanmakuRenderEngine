/*
 * Copyright (C) 2022 ByteDance Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ixigua.common.danmaku.render.engine.render.cache

import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.utils.DISCARD_TYPE_EXPIRE
import com.ixigua.common.danmaku.render.engine.utils.DISCARD_TYPE_SCORE
import java.util.*

/**
 * Created by dss886 on 2019/9/22.
 * Buffer for render layers.
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

    /**
     * Trim Buffer down to the mBufferSize.
     * Using [DanmakuConfig.CommonConfig.bufferDiscardRule] to discard items.
     */
    fun trimBuffer(playTime: Long) {
        if (mBufferItems.isEmpty() || mBufferItems.size <= mBufferSize) {
            return
        }
        mBufferItems.removeWhen {
            if (mConfig.common.bufferExpireRule != null) {
                mConfig.common.bufferExpireRule?.invoke(it.data, playTime) == true
            } else {
                if (playTime - (it.data?.showAtTime ?: 0L) > mBufferMaxTime) {
                    mConfig.common.discardListener?.invoke(it.data, DISCARD_TYPE_EXPIRE)
                    true
                } else {
                    false
                }
            }
        }
        while (mBufferItems.size > mBufferSize) {
            val item = mBufferItems.minBy {
                @Suppress("UNCHECKED_CAST")
                mConfig.common.bufferDiscardRule.invoke(it.data) as Comparable<Any?>
            }
            item?.let {
                mConfig.common.discardListener?.invoke(it.data, DISCARD_TYPE_SCORE)
                mBufferItems.remove(it)
                mCachePool.release(it)
            }
        }
    }

    /**
     * When need to measure (config changed, or measure items manually),
     * items in the buffer should be measured too.
     */
    fun measureItems() {
        mBufferItems.forEach {
            it.measure(mConfig)
        }
    }

    fun clear() {
        mBufferItems.clear()
    }

    /**
     * Changing the buffer size will clear items in the buffer.
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