package com.bytedance.danmaku.render.engine.demo.demo

import android.view.View
import com.bytedance.danmaku.render.engine.DanmakuView
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.render.draw.text.TextData
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_BOTTOM_CENTER
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_SCROLL
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_TOP_CENTER
import com.bytedance.danmaku.render.engine.demo.R
import com.bytedance.danmaku.render.engine.demo.base.BaseDemoActivity
import com.bytedance.danmaku.render.engine.demo.view.RadioButtonGroupView

/**
 * Created by dss886 on 2021/07/02.
 */
class BuildInLayersActivity : BaseDemoActivity() {

    private lateinit var mController: DanmakuController

    override fun getLayoutId(): Int {
        return R.layout.activity_build_in_layers
    }

    override fun init() {
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.setData(mDanmakuData)
        mController.start()

        val contents = resources.getStringArray(R.array.danmaku_text_contents)
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
        findViewById<View>(R.id.add_scroll).setOnClickListener {
            mController.addFakeData(TextData().apply {
                text = contents.random()
                layerType = LAYER_TYPE_SCROLL
            })
        }
        findViewById<View>(R.id.add_top).setOnClickListener {
            mController.addFakeData(TextData().apply {
                text = contents.random()
                layerType = LAYER_TYPE_TOP_CENTER
            })
        }
        findViewById<View>(R.id.add_bottom).setOnClickListener {
            mController.addFakeData(TextData().apply {
                text = contents.random()
                layerType = LAYER_TYPE_BOTTOM_CENTER
            })
        }
    }

    override fun onDestroy() {
        mController.stop()
        super.onDestroy()
    }

    override fun buildDanmakuData(): List<TextData> {
        return emptyList()
    }

}