package com.ixigua.common.meteor.render.draw.bitmap

import android.graphics.Bitmap
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.utils.DRAW_TYPE_BITMAP

/**
 * Created by dss886 on 2019-05-20.
 */
class BitmapData: DanmakuData() {

    override var drawType: Int = DRAW_TYPE_BITMAP

    /**
     * The bitmap will not be automatically recycled,
     * and will occupy memory before you recycle it manually.
     * Please check the possible memory problems carefully.
     */
    var bitmap: Bitmap? = null
    /**
     * A url associate to the bitmap.
     * This is just an extra storage field, how to use it is up to you.
     */
    var url: String? = null
    /**
     * TintColor will be used with the mode of PorterDuff.Mode.SRC_IN
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