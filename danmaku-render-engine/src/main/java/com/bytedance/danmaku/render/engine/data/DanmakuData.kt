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
package com.bytedance.danmaku.render.engine.data

import com.bytedance.danmaku.render.engine.utils.DRAW_ORDER_DEFAULT
import com.bytedance.danmaku.render.engine.utils.DRAW_TYPE_UNDEFINE
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_UNDEFINE

/**
 * Created by dss886 on 2019-05-19.
 * Fields in [DanmakuData] should be the property of danmaku, not related to the drawing.
 */
abstract class DanmakuData {

    open var showAtTime: Long = 0
    open var layerType: Int = LAYER_TYPE_UNDEFINE
    open var drawType: Int = DRAW_TYPE_UNDEFINE
    /**
     * The order danmaku will be drawn.
     * The larger the value, the later the danmaku is drawn.
     * If the drawOrder is the same, item with smaller showAtTime will be drawn first.
     */
    open var drawOrder: Int = DRAW_ORDER_DEFAULT

}