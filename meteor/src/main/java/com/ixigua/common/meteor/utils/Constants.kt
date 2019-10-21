package com.ixigua.common.meteor.utils

import android.graphics.RectF
import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019-08-16.
 */

const val STEPPER_TIME = 16L

const val LAYER_TYPE_UNDEFINE = 1000
const val LAYER_TYPE_SCROLL = 1001
const val LAYER_TYPE_TOP_CENTER = 1002
const val LAYER_TYPE_BOTTOM_CENTER = 1003

const val DRAW_TYPE_UNDEFINE = 1000
const val DRAW_TYPE_TEXT = 1001
const val DRAW_TYPE_BITMAP = 1002

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