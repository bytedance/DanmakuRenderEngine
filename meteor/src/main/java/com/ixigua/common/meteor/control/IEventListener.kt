package com.ixigua.common.meteor.control

/**
 * Created by dss886 on 2019/9/26.
 */
interface IEventListener {

    /**
     * The param event will be recycled and reused after onEvent()
     */
    fun onEvent(event: DanmakuEvent)

}