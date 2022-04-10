package com.ixigua.danmaku.render.engine.demo.demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import com.ixigua.common.danmaku.render.engine.DanmakuView
import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.common.danmaku.render.engine.render.draw.bitmap.BitmapData
import com.ixigua.common.danmaku.render.engine.render.draw.text.TextData
import com.ixigua.common.danmaku.render.engine.utils.LAYER_TYPE_SCROLL
import com.ixigua.danmaku.render.engine.demo.R
import com.ixigua.danmaku.render.engine.demo.base.BaseDemoActivity
import com.ixigua.danmaku.render.engine.demo.demo.advanced.AdvancedDanmakuData
import com.ixigua.danmaku.render.engine.demo.demo.advanced.AdvancedDanmakuFactory
import com.ixigua.danmaku.render.engine.demo.demo.advanced.DiggData
import com.ixigua.danmaku.render.engine.demo.utils.DIGG_STATE_OTHER_DIGG
import com.ixigua.danmaku.render.engine.demo.utils.DIGG_STATE_SELF_DIGG
import com.ixigua.danmaku.render.engine.demo.utils.dp
import com.ixigua.danmaku.render.engine.demo.utils.toColor
import com.ixigua.danmaku.render.engine.demo.view.RadioButtonGroupView

/**
 * Created by dss886 on 2021/04/20.
 */
class AdvancedDanmakuActivity : BaseDemoActivity() {

    private lateinit var mController: DanmakuController
    private var mDiggBitmap: Bitmap? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_advanced_danmaku
    }

    override fun init() {
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.registerDrawItemFactory(AdvancedDanmakuFactory())
        mController.setData(mDanmakuData)
        mController.start()

        val contents = resources.getStringArray(R.array.danmaku_text_contents)
        mDiggBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_digg)

        findViewById<RadioButtonGroupView>(R.id.config_bounds)?.apply {
            setUp("ShowBounds", arrayOf("no", "yes"), 0) { index, _ ->
                mController.config.debug.showBounds = index > 0
            }
        }

        findViewById<RadioButtonGroupView>(R.id.config_profiler)?.apply {
            setUp("ShowProfiler", arrayOf("no", "yes"), 0) { index, _ ->
                mController.config.debug.showDrawTimeCost = index > 0
            }
        }

        findViewById<View>(R.id.add_common_danmaku).setOnClickListener {
            mController.addFakeData(AdvancedDanmakuData().apply {
                textData = TextData().apply {
                    text = contents.random()
                }
                layerType = LAYER_TYPE_SCROLL
            })
        }

        findViewById<View>(R.id.add_danmaku_with_digg).setOnClickListener {
            mController.addFakeData(AdvancedDanmakuData().apply {
                textData = TextData().apply {
                    text = contents.random()
                }
                diggData = DiggData().apply {
                    diggState = DIGG_STATE_OTHER_DIGG
                    diggText = TextData().apply {
                        text = (1..30).random().toString()
                        textColor = R.color.white.toColor
                    }
                    diggIcon = BitmapData().apply {
                        width = 12.dp
                        height = 12.dp
                        bitmap = mDiggBitmap
                        tintColor = R.color.white.toColor
                    }
                }
                layerType = LAYER_TYPE_SCROLL
            })
        }

        findViewById<View>(R.id.add_danmaku_with_self_digg).setOnClickListener {
            mController.addFakeData(AdvancedDanmakuData().apply {
                textData = TextData().apply {
                    text = contents.random()
                }
                diggData = DiggData().apply {
                    diggState = DIGG_STATE_SELF_DIGG
                    diggText = TextData().apply {
                        text = (1..30).random().toString()
                        textColor = R.color.red_500.toColor
                    }
                    diggIcon = BitmapData().apply {
                        width = 12.dp
                        height = 12.dp
                        bitmap = mDiggBitmap
                        tintColor = R.color.red_500.toColor
                    }
                }
                layerType = LAYER_TYPE_SCROLL
            })
        }
    }

    override fun onDestroy() {
        mController.stop()
        mDiggBitmap?.recycle()
        mDiggBitmap = null
        super.onDestroy()
    }

    override fun buildDanmakuData(): List<TextData> {
        return emptyList()
    }

}