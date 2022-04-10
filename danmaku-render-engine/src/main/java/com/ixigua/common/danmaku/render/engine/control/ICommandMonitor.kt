package com.ixigua.common.danmaku.render.engine.control

import com.ixigua.common.danmaku.render.engine.control.DanmakuCommand

/**
 * Created by dss886 on 2019-05-10.
 *
 * Command is used by you to tell [DanmakuController] to do something.
 * See: [IEventListener]
 */
interface ICommandMonitor {

    fun onCommand(cmd: DanmakuCommand)

}