package com.ixigua.common.meteor.data

import com.ixigua.common.meteor.utils.DRAW_ORDER_DEFAULT
import com.ixigua.common.meteor.utils.DRAW_TYPE_UNDEFINE
import com.ixigua.common.meteor.utils.LAYER_TYPE_UNDEFINE

/**
 * Created by dss886 on 2019-05-19.
 */
abstract class DanmakuData {

    open var showAtTime: Long = 0
    open var layerType: Int = LAYER_TYPE_UNDEFINE
    open var drawType: Int = DRAW_TYPE_UNDEFINE
    open var drawOrder: Int = DRAW_ORDER_DEFAULT

}