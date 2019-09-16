package com.ixigua.common.meteor.control

import android.graphics.Color
import com.ixigua.common.meteor.data.IDanmakuData

/**
 * Created by dss886 on 2018/11/6.
 */
class DanmakuConfig : AbsConfig() {

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        const val TYPE_DEBUG_SHOW_LAYOUT_BOUNDS = 1000
        const val TYPE_DEBUG_PRINT_DRAW_TIME_COST = 1001

        const val TYPE_COMMON_PLAY_SPEED = 2000
        const val TYPE_COMMON_TYPESET_BUFFER_SIZE = 2001
        const val TYPE_COMMON_BUFFER_DISCARD_RULE = 2002

        const val TYPE_TEXT_SIZE = 3001
        const val TYPE_TEXT_COLOR = 3002
        const val TYPE_TEXT_STROKE_WIDTH = 3003
        const val TYPE_TEXT_STROKE_COLOR = 3004
        const val TYPE_TEXT_INCLUDE_FONT_PADDING = 3005

        const val TYPE_UNDERLINE_WIDTH = 4001
        const val TYPE_UNDERLINE_COLOR = 4002
        const val TYPE_UNDERLINE_STROKE_WIDTH = 4003
        const val TYPE_UNDERLINE_STROKE_COLOR = 4004
        const val TYPE_UNDERLINE_MARGIN_TOP = 4005

        const val TYPE_SCROLL_MOVE_TIME = 5001
        const val TYPE_SCROLL_LINE_HEIGHT = 5002
        const val TYPE_SCROLL_LINE_COUNT = 5003
        const val TYPE_SCROLL_LINE_SPACE = 5004
        const val TYPE_SCROLL_MARGIN_TOP = 5005
        const val TYPE_SCROLL_ITEM_MARGIN = 5006
    }

    /**
     * Config for Debug
     */
    val debug = DebugConfig(this)

    /**
     * Config for common usage
     */
    val common = CommonConfig(this)

    /**
     * Config for text drawing
     */
    val text = TextConfig(this)

    /**
     * Config for item underline
     */
    val underline = UnderlineConfig(this)

    /**
     * Config for ScrollLayer
     */
    val scroll = ScrollLayerConfig(this)

    ////////////////////////////////////////////////////
    //               Config Definition                //
    ////////////////////////////////////////////////////

    class DebugConfig(private val config: AbsConfig) {
        /**
         * Works like the 'Show Layout Bounds' option in Android Developer Settings
         */
        var showLayoutBounds = false
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_DEBUG_SHOW_LAYOUT_BOUNDS)
            }

        /**
         * Print the cost time of every method in draw(),
         * usually used to debug the draw performance.
         * Log Tag: DanmakuController
         */
        var printDrawTimeCostLog = false
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_DEBUG_PRINT_DRAW_TIME_COST)
            }
    }

    class CommonConfig(private val config: AbsConfig) {
        /**
         * The speed of playback in percent, used for time axis synchronous by DataManager
         * default is 100 (means 100%)
         */
        var playSpeed = 100
            set(value) {
                field = if (value <= 0) 100 else value
                config.notifyConfigChanged(TYPE_COMMON_PLAY_SPEED)
            }

        /**
         * The buffer size when there are no enough space to add item right now.
         * Large buffer size will have slight impact on performance.
         */
        var typesetBufferSize = 10
            set(value) {
                field = if (value <= 0) 10 else value
                config.notifyConfigChanged(TYPE_COMMON_TYPESET_BUFFER_SIZE)
            }

        /**
         * The discard rule when the buffer size is out of limit.
         * The rule is used in the minBy() function, that is to say the minimal one will be discard first.
         * By default, items in the buffer will be discard by their showAtTime.
         */
        var bufferDiscardRule: ((IDanmakuData?) -> Comparable<*>) = { it?.showAtTime ?: 0}
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_COMMON_BUFFER_DISCARD_RULE)
            }
    }

    class TextConfig(private val config: AbsConfig) {
        /**
         * text size in pixel units.
         */
        var size = 48f
            set(value) {
                field = if (value <= 0) 48f else value
                config.notifyConfigChanged(TYPE_TEXT_SIZE)
            }

        var color = Color.WHITE
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_COLOR)
            }

        /**
         * stroke width of text in pixel units
         */
        var strokeWidth = 2.75f
            set(value) {
                field = if (value <= 0) 2.75f else value
                config.notifyConfigChanged(TYPE_TEXT_STROKE_WIDTH)
            }

        var strokeColor = Color.argb(97, 0, 0, 0)
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_STROKE_COLOR)
            }

        var includeFontPadding = true
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_INCLUDE_FONT_PADDING)
            }
    }

    class UnderlineConfig(private val config: AbsConfig) {
        var width = 0f
            set(value) {
                field = if (value <= 0) 0f else value
                config.notifyConfigChanged(TYPE_UNDERLINE_WIDTH)
            }

        var color = Color.argb(230, 255, 255, 255)
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_UNDERLINE_COLOR)
            }

        /**
         * stroke width of underline in pixel units
         */
        var strokeWidth = 1f
            set(value) {
                field = if (value <= 0) 1f else value
                config.notifyConfigChanged(TYPE_UNDERLINE_STROKE_WIDTH)
            }

        var strokeColor = Color.argb(97, 0, 0, 0)
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_UNDERLINE_STROKE_COLOR)
            }

        var marginTop = 0f
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_UNDERLINE_MARGIN_TOP)
            }
    }

    class ScrollLayerConfig(private val config: AbsConfig) {
        /**
         * Speed of item is based on the screen width, for user experience reasons.
         * Generally, long item move faster and short item move slower.
         * This variable means the millisecond used through the screen.
         * In millisecond units.
         */
        var moveTime = 8000L
            set(value) {
                field = if (value <= 0) 8000L else value
                config.notifyConfigChanged(TYPE_SCROLL_MOVE_TIME)
            }

        /**
         * In pixel units.
         * If LineHeight is larger than the item inside, item will be drawn at the top of the line.
         * Gravity will be supported in future.
         */
        var lineHeight = 54f
            set(value) {
                field = if (value <= 0) 54f else value
                config.notifyConfigChanged(TYPE_SCROLL_LINE_HEIGHT)
            }

        /**
         * The percent of scroll layer's display area by the screen height
         * for example 1/4, 1/2, 3/4, etc.
         */
        var lineCount = 4
            set(value) {
                field = if (value <= 0) 4 else value
                config.notifyConfigChanged(TYPE_SCROLL_LINE_COUNT)
            }

        /**
         * Margin between lines.
         * In pixel units.
         */
        var lineSpace = 18f
            set(value) {
                field = if (value <= 0) 18f else value
                config.notifyConfigChanged(TYPE_SCROLL_LINE_SPACE)
            }

        var marginTop = 0f
            set(value) {
                field = if (value < 0) 0f else value
                config.notifyConfigChanged(TYPE_SCROLL_MARGIN_TOP)
            }

        /**
         * Minimum margin of items horizontal in the same line.
         * In pixel units.
         */
        var itemMargin = 24
            set(value) {
                field = if (value <= 0) 24 else value
                config.notifyConfigChanged(TYPE_SCROLL_ITEM_MARGIN)
            }

    }
}
