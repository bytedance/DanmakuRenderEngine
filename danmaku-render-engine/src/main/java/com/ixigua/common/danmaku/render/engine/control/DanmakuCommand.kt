package com.ixigua.common.danmaku.render.engine.control

import com.ixigua.common.danmaku.render.engine.data.DanmakuData

/**
 * Created by dss886 on 2019-05-09.
 */
class DanmakuCommand(var what: Int, var data: DanmakuData? = null, var param: Any? = null)