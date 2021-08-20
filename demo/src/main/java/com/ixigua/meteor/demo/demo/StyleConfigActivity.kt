package com.ixigua.meteor.demo.demo

import android.graphics.Color
import com.ixigua.common.meteor.DanmakuView
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.render.draw.text.TextData
import com.ixigua.common.meteor.utils.LAYER_TYPE_BOTTOM_CENTER
import com.ixigua.common.meteor.utils.LAYER_TYPE_SCROLL
import com.ixigua.common.meteor.utils.LAYER_TYPE_TOP_CENTER
import com.ixigua.meteor.demo.R
import com.ixigua.meteor.demo.base.BaseDemoActivity
import com.ixigua.meteor.demo.utils.VideoListenerAdapter
import com.ixigua.meteor.demo.view.RadioButtonGroupView

/**
 * Created by dss886 on 2021/07/02.
 */
class StyleConfigActivity : BaseDemoActivity() {

    private lateinit var mController: DanmakuController

    override fun getLayoutId(): Int {
        return R.layout.activity_style_config
    }

    override fun init() {
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.setData(mDanmakuData)
        mVideoView.videoListener = object : VideoListenerAdapter() {
            override fun onVideoCompletion() {
                mVideoView.start()
                mController.start()
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_1_1)?.apply {
            setUp("ShowBounds", arrayOf("no", "yes"), 0) { index, _ ->
                mController.config.debug.showBounds = index > 0
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_1_2)?.apply {
            setUp("ShowProfiler", arrayOf("no", "yes"), 0) { index, _ ->
                mController.config.debug.showDrawTimeCost = index > 0
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_2_1)?.apply {
            setUp("Alpha", arrayOf("0.3", "0.5", "0.8", "1.0"), 3) { index, _ ->
                val value = (255 * when (index) {
                    0 -> 0.3f
                    1 -> 0.5f
                    2 -> 0.8f
                    else -> 1.0f
                }).toInt()
                mController.config.common.alpha = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_2_2)?.apply {
            setUp("PlaySpeed", arrayOf("x0.5", "x1", "x2"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 0.5f
                    1 -> 1f
                    2 -> 2f
                    else -> 1f
                }
                mVideoView.setPlaySpeed(value)
                mController.config.common.playSpeed = (100 * value).toInt()
                mController.config.scroll.moveTime = (8000L / value).toLong()
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_2_3)?.apply {
            setUp("TextSize", arrayOf("40px", "48px", "56px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 36f
                    1 -> 48f
                    2 -> 54f
                    else -> 48f
                }
                mController.config.text.size = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_2_4)?.apply {
            setUp("TextColor", arrayOf("Red", "Blue", "White"), 2) { index, _ ->
                val value = when (index) {
                    0 -> Color.RED
                    1 -> Color.BLUE
                    else -> Color.WHITE
                }
                mController.config.text.color = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_2_5)?.apply {
            setUp("StrokeWidth", arrayOf("0", "2.75", "5"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 0f
                    1 -> 2.74f
                    else -> 5f
                }
                mController.config.text.strokeWidth = value
                mController.config.underline.strokeWidth = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_2_6)?.apply {
            setUp("Underline", arrayOf("no", "yes"), 0) { index, _ ->
                mDanmakuData.forEach {
                    (it as? TextData)?.hasUnderline = index > 0
                }
                mController.config.underline.width = 2.75f
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_3_1)?.apply {
            setUp("LineHeight", arrayOf("48px", "54px", "60px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 48f
                    1 -> 54f
                    else -> 60f
                }
                mController.config.scroll.lineHeight = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_3_2)?.apply {
            setUp("LineMargin", arrayOf("0px", "18px", "36px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 0f
                    1 -> 18f
                    else -> 36f
                }
                mController.config.scroll.lineMargin = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_3_3)?.apply {
            setUp("LineCount", arrayOf("2", "4", "6"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 2
                    1 -> 4
                    2 -> 6
                    else -> 6
                }
                mController.config.scroll.lineCount = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_3_4)?.apply {
            setUp("MarginTop", arrayOf("0px", "18px", "36px"), 0) { index, _ ->
                val value = when (index) {
                    1 -> 18f
                    2 -> 36f
                    else -> 0f
                }
                mController.config.scroll.marginTop = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_4_1)?.apply {
            setUp("LineHeight", arrayOf("48px", "54px", "60px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 48f
                    1 -> 54f
                    else -> 60f
                }
                mController.config.top.lineHeight = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_4_2)?.apply {
            setUp("LineMargin", arrayOf("0px", "18px", "36px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 0f
                    1 -> 18f
                    else -> 36f
                }
                mController.config.top.lineMargin = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_4_3)?.apply {
            setUp("LineCount", arrayOf("2", "4", "6"), 0) { index, _ ->
                val value = when (index) {
                    0 -> 2
                    1 -> 4
                    2 -> 6
                    else -> 6
                }
                mController.config.top.lineCount = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_4_4)?.apply {
            setUp("MarginTop", arrayOf("0px", "18px", "36px"), 0) { index, _ ->
                val value = when (index) {
                    1 -> 18f
                    2 -> 36f
                    else -> 0f
                }
                mController.config.top.marginTop = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_5_1)?.apply {
            setUp("LineHeight", arrayOf("48px", "54px", "60px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 48f
                    1 -> 54f
                    else -> 60f
                }
                mController.config.bottom.lineHeight = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_5_2)?.apply {
            setUp("LineMargin", arrayOf("0px", "18px", "36px"), 1) { index, _ ->
                val value = when (index) {
                    0 -> 0f
                    1 -> 18f
                    else -> 36f
                }
                mController.config.bottom.lineMargin = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_5_3)?.apply {
            setUp("LineCount", arrayOf("2", "4", "6"), 0) { index, _ ->
                val value = when (index) {
                    0 -> 2
                    1 -> 4
                    2 -> 6
                    else -> 6
                }
                mController.config.bottom.lineCount = value
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_5_4)?.apply {
            setUp("MarginBottom", arrayOf("0px", "18px", "36px"), 1) { index, _ ->
                val value = when (index) {
                    1 -> 18f
                    2 -> 36f
                    else -> 0f
                }
                mController.config.bottom.marginBottom = value
            }
        }

        mController.start()
    }

    override fun onDestroy() {
        mController.stop()
        super.onDestroy()
    }

    override fun buildDanmakuData(): List<TextData> {
        val contents = resources.getStringArray(R.array.danmaku_text_contents)
        val list = mutableListOf<TextData>()
        for (i in 0..99) {
            list.add(TextData().apply {
                text = contents.random()
                val random = Math.random()
                layerType = when {
                    random < 0.2 -> LAYER_TYPE_TOP_CENTER
                    random < 0.4 -> LAYER_TYPE_BOTTOM_CENTER
                    else -> LAYER_TYPE_SCROLL
                }
                showAtTime = i * 600L +  (0..300L).random()
            })
        }
        return list
    }

}