package com.ixigua.common.meteor.render.draw.bitmap

import android.graphics.Bitmap
import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.utils.DRAW_ORDER_DEFAULT
import com.ixigua.common.meteor.utils.DRAW_TYPE_BITMAP
import com.ixigua.common.meteor.utils.LAYER_TYPE_UNDEFINE

/**
 * Created by dss886 on 2019-05-20.
 */
class BitmapData: IDanmakuData {

    override var showAtTime: Long = 0
    override var layerType: Int = LAYER_TYPE_UNDEFINE
    override var drawType: Int = DRAW_TYPE_BITMAP
    override var drawOrder: Int = DRAW_ORDER_DEFAULT

    /**
     * The bitmap will not be automatically recycled,
     * and will occupy memory before you recycle it manually.
     * Please check the possible memory problems carefully.
     */
    var bitmap: Bitmap? = null
    /**
     * Zero means using the origin width of the bitmap
     */
    var width: Float = 0F
    /**
     * Zero means using the origin height of the bitmap
     */
    var height: Float = 0F

}