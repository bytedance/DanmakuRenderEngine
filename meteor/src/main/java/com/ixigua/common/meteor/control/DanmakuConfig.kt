package com.ixigua.common.meteor.control

import android.graphics.Color

/**
 * Created by dss886 on 2018/11/6.
 */
class DanmakuConfig : AbsConfig() {

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    companion object {
        const val TYPE_PLAY_SPEED = 1000
        const val TYPE_TEXT_SIZE = TYPE_PLAY_SPEED + 1
        const val TYPE_TEXT_COLOR = TYPE_TEXT_SIZE + 1
        const val TYPE_TEXT_BORDER_WIDTH = TYPE_TEXT_COLOR + 1
        const val TYPE_TEXT_BORDER_COLOR = TYPE_TEXT_BORDER_WIDTH + 1
        const val TYPE_TEXT_UNDERLINE_WIDTH = TYPE_TEXT_BORDER_COLOR + 1
        const val TYPE_TEXT_UNDERLINE_COLOR = TYPE_TEXT_UNDERLINE_WIDTH + 1
        const val TYPE_TEXT_UNDERLINE_STROKE_WIDTH = TYPE_TEXT_UNDERLINE_COLOR + 1
        const val TYPE_TEXT_UNDERLINE_STROKE_COLOR = TYPE_TEXT_UNDERLINE_STROKE_WIDTH + 1
        const val TYPE_TEXT_UNDERLINE_MARGIN_TOP = TYPE_TEXT_UNDERLINE_STROKE_COLOR + 1
        const val TYPE_TEXT_INCLUDE_FONT_PADDING = TYPE_TEXT_UNDERLINE_MARGIN_TOP + 1
        const val TYPE_MOVE_TIME = TYPE_TEXT_INCLUDE_FONT_PADDING + 1
        const val TYPE_SCROLL_LINE_HEIGHT = TYPE_MOVE_TIME + 1
        const val TYPE_SCROLL_DISPLAY_PERCENT = TYPE_SCROLL_LINE_HEIGHT + 1
        const val TYPE_SCROLL_ITEM_MARGIN = TYPE_SCROLL_DISPLAY_PERCENT + 1
        const val TYPE_SCROLL_LINE_MARGIN = TYPE_SCROLL_ITEM_MARGIN + 1
        const val TYPE_DEBUG_SHOW_LAYOUT_BOUNDS = TYPE_SCROLL_LINE_MARGIN + 1
    }

    /**
     * The speed of playback in percent, used for time axis synchronous by DataManager
     * default is 100 (means 100%)
     */
    var playSpeed = 100
        set(value) {
            field = if (value <= 0) 100 else value
            notifyConfigChanged(TYPE_PLAY_SPEED)
        }

    /**
     * Common Config for text drawing
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

    /**
     * Config for Debug
     */
    val debug = DebugConfig(this)

    ////////////////////////////////////////////////////
    //               Config Definition                //
    ////////////////////////////////////////////////////

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
                config.notifyConfigChanged(TYPE_TEXT_BORDER_WIDTH)
            }

        var strokeColor = Color.argb(97, 0, 0, 0)
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_BORDER_COLOR)
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
                config.notifyConfigChanged(TYPE_TEXT_UNDERLINE_WIDTH)
            }

        var color = Color.argb(230, 255, 255, 255)
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_UNDERLINE_COLOR)
            }

        /**
         * stroke width of underline in pixel units
         */
        var strokeWidth = 1f
            set(value) {
                field = if (value <= 0) 1f else value
                config.notifyConfigChanged(TYPE_TEXT_UNDERLINE_STROKE_WIDTH)
            }

        var strokeColor = Color.argb(97, 0, 0, 0)
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_UNDERLINE_STROKE_COLOR)
            }

        var marginTop = 0f
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_TEXT_UNDERLINE_MARGIN_TOP)
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
                config.notifyConfigChanged(TYPE_MOVE_TIME)
            }

        /**
         * In pixel units.
         * If LineHeight is larger than the item inside, item will be drawn at the top of the line.
         * Gravity will be supported in future.
         */
        var lineHeight = 50f
            set(value) {
                field = if (value <= 0) 48f else value
                config.notifyConfigChanged(TYPE_SCROLL_LINE_HEIGHT)
            }

        /**
         * The percent of scroll layer's display area by the screen height
         * for example 1/4, 1/2, 3/4, etc.
         */
        var displayPercent = 0.75f
            set(value) {
                field = if (value <= 0 || value > 1) 0.75f else value
                config.notifyConfigChanged(TYPE_SCROLL_DISPLAY_PERCENT)
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

        /**
         * Margin between lines.
         * In pixel units.
         */
        var lineMargin = 18f
            set(value) {
                field = if (value <= 0) 18f else value
                config.notifyConfigChanged(TYPE_SCROLL_LINE_MARGIN)
            }
    }

    class DebugConfig(private val config: AbsConfig) {
        var showLayoutBounds = false
            set(value) {
                field = value
                config.notifyConfigChanged(TYPE_DEBUG_SHOW_LAYOUT_BOUNDS)
            }
    }
}
