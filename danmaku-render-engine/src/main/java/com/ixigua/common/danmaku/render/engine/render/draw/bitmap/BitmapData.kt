package com.ixigua.common.danmaku.render.engine.render.draw.bitmap

import android.graphics.Bitmap
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.utils.DRAW_TYPE_BITMAP

/**
 * Created by dss886 on 2019-05-20.
 */
open class BitmapData: DanmakuData() {

    override var drawType: Int = DRAW_TYPE_BITMAP

    /**
     * The bitmap will not be automatically recycled,
     * and occupy the memory before you recycle it manually.
     * Please check the memory problems carefully.
     */
    var bitmap: Bitmap? = null
    /**
     * TintColor will be used with the mode of [android.graphics.PorterDuff.Mode.SRC_IN]
     */
    var tintColor: Int? = null
    /**
     * Zero means using the origin width of the bitmap
     */
    var width: Float = 0F
    /**
     * Zero means using the origin height of the bitmap
     */
    var height: Float = 0F

}