package com.ixigua.common.meteor.data

import android.support.annotation.UiThread
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.control.DanmakuController
import java.util.*
import kotlin.math.max

/**
 * Created by dss886 on 2018/11/6.
 */
class DataManager(controller: DanmakuController) {

    private val mList = LinkedList<IDanmakuData>()
    private val mQueryList = LinkedList<IDanmakuData>()
    private val mFakeList = LinkedList<IDanmakuData>()
    private val mConfig: DanmakuConfig = controller.config

    private var mIsPlaying = false
    private var mCurrentIndex = 0
    private var mStartPlayTime = 0L
    private var mStartTimestamp = 0L
    private var mCurrentPlayTime = 0L

    @UiThread
    fun setData(dataList: List<IDanmakuData>) {
        mList.clear()
        mList.addAll(dataList)
    }

    @UiThread
    fun appendData(dataList: List<IDanmakuData>) {
        mList.addAll(dataList)
    }

    @UiThread
    fun addFakeData(data: IDanmakuData) {
        mFakeList.add(data)
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
    fun queryDanmaku(): List<IDanmakuData> {
        if (!mIsPlaying) {
            return mQueryList.apply { clear() }
        }
        val now = System.currentTimeMillis()
        val playTime = ((now - mStartTimestamp) * mConfig.playSpeed / 100f + mStartPlayTime).toLong()
        mCurrentPlayTime = playTime
        mQueryList.clear()
        mQueryList.addAll(mFakeList)
        mFakeList.clear()
        while (true) {
            if (mCurrentIndex < 0 || mCurrentIndex >= mList.size) {
                break
            }
            val item = mList[mCurrentIndex]
            if (item.showAtTime > playTime) {
                break
            }
            mQueryList.add(item)
            mCurrentIndex++
        }
        return mQueryList
    }

    @UiThread
    fun onStop() {
        mIsPlaying = false
        mList.clear()
        mQueryList.clear()
        mCurrentIndex = 0
        mStartPlayTime = 0L
        mStartTimestamp = 0L
        mCurrentPlayTime = 0L
    }

}
