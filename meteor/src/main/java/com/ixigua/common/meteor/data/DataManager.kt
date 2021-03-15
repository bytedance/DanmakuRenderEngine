package com.ixigua.common.meteor.data

import android.support.annotation.UiThread
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.render.cache.LayerBuffer
import java.util.*
import kotlin.math.max

/**
 * Created by dss886 on 2018/11/6.
 * DataManager is the timeline controller for the danmakus
 */
class DataManager(controller: DanmakuController) {

    private val mList = LinkedList<DanmakuData>()
    private val mQueryList = LinkedList<DanmakuData>()
    /**
     * Fake items will be added to the return list in [queryDanmaku] immediately.
     * Even so, there is no guarantee that them will be displayed 100%,
     * as the render layers will discard data when the amount of data is too large.
     * See [LayerBuffer]
     */
    private val mFakeList = LinkedList<DanmakuData>()
    private val mConfig: DanmakuConfig = controller.config

    private var mIsPlaying = false
    private var mCurrentIndex = 0
    private var mStartPlayTime = 0L
    private var mStartTimestamp = 0L
    private var mCurrentPlayTime = 0L

    @UiThread
    fun setData(dataList: List<DanmakuData>) {
        mList.clear()
        mList.addAll(dataList)
    }

    @UiThread
    fun appendData(dataList: List<DanmakuData>) {
        mList.addAll(dataList)
    }

    @UiThread
    fun addFakeData(data: DanmakuData) {
        mFakeList.add(data)
    }

    @UiThread
    fun getData() : List<DanmakuData>{
        return mList
    }

    @UiThread
    fun onPlay(playTime: Long) {
        mIsPlaying = true
        mCurrentIndex = 0
        mStartPlayTime = max(0, playTime)
        mStartTimestamp = System.currentTimeMillis()
        mCurrentPlayTime = mStartPlayTime
        mList.forEachIndexed { index, danmaku ->
            if (danmaku.showAtTime >= playTime) {
                return
            }
            mCurrentIndex = index + 1
        }
    }

    @UiThread
    fun onPlaySpeedChanged() {
        mStartPlayTime = mCurrentPlayTime
        mStartTimestamp = System.currentTimeMillis()
    }

    @UiThread
    fun queryPlayTime(): Long {
        if (!mIsPlaying) {
            return mCurrentPlayTime
        }
        val now = System.currentTimeMillis()
        val playTime = ((now - mStartTimestamp) * mConfig.common.playSpeed / 100f + mStartPlayTime).toLong()
        mCurrentPlayTime = playTime
        return mCurrentPlayTime
    }

    @UiThread
    fun queryDanmaku(): List<DanmakuData> {
        if (!mIsPlaying) {
            return mQueryList.apply { clear() }
        }
        mQueryList.clear()
        mQueryList.addAll(mFakeList)
        mFakeList.clear()
        while (true) {
            if (mCurrentIndex < 0 || mCurrentIndex >= mList.size) {
                break
            }
            val item = mList[mCurrentIndex]
            if (item.showAtTime > mCurrentPlayTime) {
                break
            }
            mQueryList.add(item)
            mCurrentIndex++
        }
        return mQueryList
    }

    @UiThread
    fun getRemainDanmakuCount(): Int {
        return mList.size - mCurrentIndex
    }

    @UiThread
    fun nextDanmakuShowAfter(): Long {
        return if (mList.size > 0 && mCurrentIndex < mList.size) {
            mList[mCurrentIndex].showAtTime - mCurrentPlayTime
        } else {
            -1
        }
    }

    @UiThread
    fun onPause() {
        mIsPlaying = false
    }

    @UiThread
    fun onStop() {
        mIsPlaying = false
        mList.clear()
        mQueryList.clear()
        mFakeList.clear()
        mCurrentIndex = 0
        mStartPlayTime = 0L
        mStartTimestamp = 0L
        mCurrentPlayTime = 0L
    }

}
