package com.ixigua.common.danmaku.render.engine.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import java.text.DecimalFormat

/**
 * Created by dss886 on 2021/07/08.
 * A performance profiler.
 */
class Profiler(private val config: DanmakuConfig) {

    private val mDebugPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mNsFormat: DecimalFormat = DecimalFormat("0,000,000 ns")
    private val mMsFormat: DecimalFormat = DecimalFormat("0.000 ms")

    fun profilerDrawTimeCost(canvas: Canvas, vararg time: Long) {
        if (time.size != 5) {
            return
        }

        if (Logger.isVerboseLoggable()) {
            Logger.v(LOG_TAG_PERFORMANCE, "draw(): " +
                    "all=${mNsFormat.format(time[4] - time[0])}, " +
                    "query=${mNsFormat.format(time[1] - time[0])}, " +
                    "add=${mNsFormat.format(time[2] - time[1])}, " +
                    "typesetting=${mNsFormat.format(time[3] - time[2])}, " +
                    "draw=${mNsFormat.format(time[4] - time[3])}")
        }

        val totalInMs = (time[4] - time[0]).toFloat() / 1000_000

        if (Logger.isWarnLoggable() && totalInMs > 8) {
            Logger.w(LOG_TAG_PERFORMANCE, "The time cost of the DanmakuView's draw() method is more than " +
                    "8ms(${mMsFormat.format(totalInMs)}), it may cause performance issues!")
        }

        if (config.debug.showDrawTimeCost) {
            mDebugPaint.color = Color.argb(128, 0, 255, 0)
            mDebugPaint.style = Paint.Style.FILL
            canvas.drawRect(0f, 0f, 280f, 44f, mDebugPaint)
            mDebugPaint.color = Color.argb(255, 255, 0, 0)
            mDebugPaint.textSize = 32f
            canvas.drawText("draw(): " + mMsFormat.format(totalInMs), 10f, - mDebugPaint.fontMetrics.ascent, mDebugPaint)
        }
    }


}