package com.ixigua.danmaku.render.engine.demo.demo

import android.view.View
import com.ixigua.common.meteor.DanmakuView
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.render.draw.text.TextData
import com.ixigua.danmaku.render.engine.demo.R
import com.ixigua.danmaku.render.engine.demo.base.BaseDemoActivity
import com.ixigua.danmaku.render.engine.demo.demo.layer.meteor.MeteorLayer
import com.ixigua.danmaku.render.engine.demo.demo.layer.rotate.RotateLayer
import com.ixigua.danmaku.render.engine.demo.utils.LAYER_TYPE_METEOR
import com.ixigua.danmaku.render.engine.demo.utils.LAYER_TYPE_ROTATE
import com.ixigua.danmaku.render.engine.demo.view.RadioButtonGroupView


/**
 * Created by dss886 on 2021/04/20.
 */
class CustomLayerActivity : BaseDemoActivity() {

    private lateinit var mController: DanmakuController

    override fun getLayoutId(): Int {
        return R.layout.activity_custom_layer
    }

    override fun init() {
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.addRenderLayer(RotateLayer())
        mController.addRenderLayer(MeteorLayer())
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

        findViewById<View>(R.id.add_danmaku_to_custom_layer_1).setOnClickListener {
            mController.addFakeData(TextData().apply {
                text = contents.last()
                layerType = LAYER_TYPE_ROTATE
            })
        }

        findViewById<View>(R.id.add_danmaku_to_custom_layer_2).setOnClickListener {
            mController.addFakeData(TextData().apply {
                text = "🌟" + contents[contents.size - 2]
                layerType = LAYER_TYPE_METEOR
            })
        }
    }

    override fun buildDanmakuData(): List<TextData> {
        return emptyList()
    }

    override fun onDestroy() {
        mController.stop()
        super.onDestroy()
    }

}