package com.ixigua.common.danmaku.render.engine.control

import androidx.core.util.Pools
import com.ixigua.common.danmaku.render.engine.data.DanmakuData

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
        try {
            mPools.release(event)
        } catch (ignore: IllegalStateException) {
            //Ignored case: "event" may be already released and "Already in the pool!"
        }
    }

}