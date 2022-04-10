package com.ixigua.common.danmaku.render.engine.render.draw.text

import android.graphics.Typeface
import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.utils.DRAW_TYPE_TEXT

/**
 * Created by dss886 on 2018/11/6.
 * If defined, attributes in TextData will override the same value in [DanmakuConfig.text]
 */
open class TextData: DanmakuData() {

    override var drawType: Int = DRAW_TYPE_TEXT

    var text: String? = null
    var textSize: Float? = null
    var textColor: Int? = null
    var typeface: Typeface? = null
    var textStrokeWidth: Float? = null
    var textStrokeColor: Int? = null
    var includeFontPadding: Boolean? = null
    var hasUnderline: Boolean = false

}
