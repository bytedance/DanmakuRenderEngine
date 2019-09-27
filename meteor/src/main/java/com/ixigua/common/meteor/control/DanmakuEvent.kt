package com.ixigua.common.meteor.control

import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019-05-09.
 */
class DanmakuEvent(var what: Int, var data: DanmakuData? = null, var param: Any? = null) {

    fun reset() {
        what = 0
        data = null
        param = null
    }

}