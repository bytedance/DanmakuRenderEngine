package com.ixigua.common.meteor.control

import android.support.v4.util.Pools
import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019/9/27.
 */
object Events {

    private val mPools = Pools.SimplePool<DanmakuEvent>(20)

    fun obtainEvent(what: Int, data: DanmakuData? = null, param: Any? = null): DanmakuEvent {
        var event = mPools.acquire()
        if (event == null) {
            event = DanmakuEvent(what, data, param)
        } else {
            event.what = what
            event.data = data
            event.param = param
        }
        return event
    }

    fun recycleEvent(event: DanmakuEvent) {
        event.reset()
        mPools.release(event)
    }

}