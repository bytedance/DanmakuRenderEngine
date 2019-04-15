package com.ixigua.common.meteor.render.draw.text

import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.utils.DRAW_ORDER_DEFAULT
import com.ixigua.common.meteor.utils.DRAW_TYPE_TEXT
import com.ixigua.common.meteor.utils.LAYER_TYPE_UNDEFINE

/**
 * Created by dss886 on 2018/11/6.
 */
class TextData: IDanmakuData {

    override var showAtTime: Long = 0
    override var layerType: Int = LAYER_TYPE_UNDEFINE
    override var drawType: Int = DRAW_TYPE_TEXT
    override var drawOrder: Int = DRAW_ORDER_DEFAULT

    var text: String? = null
    var textSize: Float? = null
    var textColor: Int? = null
    var textStrokeWidth: Float? = null
    var textStrokeColor: Int? = null
    var includeFontPadding: Boolean? = null
    var hasUnderline: Boolean = false

}
