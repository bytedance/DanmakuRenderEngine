package com.bytedance.danmaku.render.engine.demo.demo

import android.graphics.PointF
import android.graphics.RectF
import com.bytedance.danmaku.render.engine.DanmakuView
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.control.DanmakuEvent
import com.bytedance.danmaku.render.engine.control.IEventListener
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.draw.text.TextData
import com.bytedance.danmaku.render.engine.touch.IItemClickListener
import com.bytedance.danmaku.render.engine.utils.*
import com.bytedance.danmaku.render.engine.demo.R
import com.bytedance.danmaku.render.engine.demo.base.BaseDemoActivity
import com.bytedance.danmaku.render.engine.demo.view.ConsoleRecyclerView

/**
 * Created by dss886 on 2021/08/19.
 */
class CmdEventActivity : BaseDemoActivity() {

    private lateinit var mController: DanmakuController
    private lateinit var mConsoleView: ConsoleRecyclerView
    private var mPausingItem: DanmakuData? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_command_and_event
    }

    override fun init() {
        mConsoleView = findViewById(R.id.console_recycler_view)
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.setData(mDanmakuData)
        mController.addEventListener(object : IEventListener {
            override fun onEvent(event: DanmakuEvent) {
                val eventName = when(event.what) {
                    EVENT_DANMAKU_SHOW -> "Show"
                    EVENT_DANMAKU_DISMISS -> "Dismiss"
                    EVENT_DANMAKU_REMEASURE -> "Re-measure"
                    else -> "Unknown"
                }
                mConsoleView.log("Event", "${eventName}: text=${(event.data as? TextData)?.text}")
            }
        })
        mController.itemClickListener = object : IItemClickListener {
            override fun onDanmakuClick(data: DanmakuData, itemRect: RectF, clickPoint: PointF) {
                mPausingItem = if (mPausingItem == null) {
                    mController.executeCommand(CMD_PAUSE_ITEM, data)
                    data.drawOrder = Integer.MAX_VALUE
                    mConsoleView.log("Cmd", "Pause: text=${(data as? TextData)?.text}")
                    data
                } else {
                    mController.executeCommand(CMD_RESUME_ITEM, mPausingItem)
                    data.drawOrder = DRAW_ORDER_DEFAULT
                    mConsoleView.log("Cmd", "Resume: text=${(mPausingItem as? TextData)?.text}")
                    null
                }
            }
        }
        mController.start()
    }

    override fun onDestroy() {
        mController.stop()
        super.onDestroy()
    }

}