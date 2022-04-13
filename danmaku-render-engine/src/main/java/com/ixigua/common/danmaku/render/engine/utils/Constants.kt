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
package com.ixigua.common.danmaku.render.engine.utils

import android.graphics.RectF
import com.ixigua.common.danmaku.render.engine.data.DanmakuData

/**
 * Created by dss886 on 2019-08-16.
 */

const val STEPPER_TIME = 16L

/**
 * The maximum interval time for the view to be drawn on a high-FPS mobile phone.
 * Considering that the draw interval is about 11ms at 90Hz and about 8ms at 120Hz,
 * set the HIGH_REFRESH_MAX_TIME to 14.
 */
const val HIGH_REFRESH_MAX_TIME = 14L

const val NEXT_DANMAKU_SHOW_MIN_INTERVAL = 160L

const val LAYER_TYPE_UNDEFINE = 1000
const val LAYER_TYPE_SCROLL = 1001
const val LAYER_TYPE_TOP_CENTER = 1002
const val LAYER_TYPE_BOTTOM_CENTER = 1003
const val LAYER_TYPE_MASK = 1004

const val LAYER_Z_INDEX_SCROLL = 1000
const val LAYER_Z_INDEX_TOP_CENTER = 1100
const val LAYER_Z_INDEX_BOTTOM_CENTER = 1200
const val LAYER_Z_INDEX_MASK = Integer.MAX_VALUE    // Make sure the mask be drawn in the last

val LAYER_Z_INDEXES = arrayOf(
    LAYER_Z_INDEX_SCROLL,
    LAYER_Z_INDEX_TOP_CENTER,
    LAYER_Z_INDEX_BOTTOM_CENTER,
    LAYER_Z_INDEX_MASK
)

const val DRAW_TYPE_UNDEFINE = 1000
const val DRAW_TYPE_TEXT = 1001
const val DRAW_TYPE_BITMAP = 1002
const val DRAW_TYPE_MASK = 1003

const val DRAW_ORDER_DEFAULT = 1

/**
 * Set the DanmakuView is touchable or not.
 */
const val CMD_SET_TOUCHABLE = 1000
/**
 * Pause a danmaku item.
 * param type: [DanmakuData]
 */
const val CMD_PAUSE_ITEM = 1001
/**
 * Resume a paused danmaku item.
 * param type: [DanmakuData]
 */
const val CMD_RESUME_ITEM = 1002
/**
 * Measure a danmaku item manually
 * param type: [DanmakuData]
 */
const val CMD_REMEASURE_ITEM = 1003

/**
 * A Danmaku item has been shown.
 * param type: [DanmakuData]
 */
const val EVENT_DANMAKU_SHOW = 1000
/**
 * A Danmaku item has been dismissed.
 * param type: [DanmakuData]
 */
const val EVENT_DANMAKU_DISMISS = 1001
/**
 * Tell the new itemRect of the item when you execute command [CMD_REMEASURE_ITEM]
 * param type: [RectF]
 */
const val EVENT_DANMAKU_REMEASURE = 1002

/**
 * Item discard type
 */
const val DISCARD_TYPE_EXPIRE = 1
const val DISCARD_TYPE_SCORE = 2

const val LOG_TAG_PERFORMANCE = "DanmakuRenderEnginePerformance"