/*
 * Copyright (C) 2022 ByteDance Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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