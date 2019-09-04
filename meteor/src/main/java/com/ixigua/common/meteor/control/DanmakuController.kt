package com.ixigua.common.meteor.control

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import com.ixigua.common.meteor.data.DataManager
import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.RenderEngine
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.touch.IItemClickListener
import com.ixigua.common.meteor.touch.TouchHelper
import com.ixigua.common.meteor.utils.CMD_SET_TOUCHABLE
import com.ixigua.common.meteor.utils.postInvalidateCompat

/**
 * Created by dss886 on 2018/11/6.
 */
class DanmakuController(private var mDanmakuView: View): ConfigChangeListener, ICommandMonitor {

    val config: DanmakuConfig = DanmakuConfig().apply {
        addListener(this@DanmakuController)
    }
    val context: Context = mDanmakuView.context
    var itemClickListener: IItemClickListener? = null

    private val mRenderEngine: RenderEngine = RenderEngine(this)
    private val mDataManager: DataManager = DataManager(this)
    private val mTouchHelper: TouchHelper = TouchHelper()
    private val mCmdMonitors: MutableList<ICommandMonitor> = mutableListOf<ICommandMonitor>().apply {
        add(this@DanmakuController)
    }

    private var mIsPlaying = false
    private var mIsTouchable = true

    //////////////////////////////////////////////////////
    //                 Public Interface                 //
    //////////////////////////////////////////////////////

    fun start(playTime: Long) {
        if (mIsPlaying) {
            return
        }
        mIsPlaying = true
        mDataManager.onPlay(playTime)
        mDanmakuView.postInvalidateCompat()
    }

    fun pause() {
        mIsPlaying = false
    }

    fun stop() {
        mIsPlaying = false
        mDataManager.onStop()
        clear()
    }

    fun clear() {
        mRenderEngine.clear()
        mDanmakuView.postInvalidateCompat()
    }

    fun registerDrawItemFactory(factory: IDrawItemFactory) {
        mRenderEngine.registerDrawItemFactory(factory)
    }

    fun setData(dataList: List<IDanmakuData>, current: Long = 0) {
        mDataManager.setData(dataList)
        if (current > 0) {
            mDataManager.onPlay(current)
        }
    }

    fun appendData(dataList: List<IDanmakuData>) {
        mDataManager.appendData(dataList)
    }

    fun addFakeData(data: IDanmakuData) {
        mDataManager.addFakeData(data)
    }

    fun invalidateView() {
        mDanmakuView.postInvalidateCompat()
    }

    fun executeCommand(cmd: Int, data: IDanmakuData? = null, param: Any? = null) {
        executeCommand(DanmakuCommand(cmd, data, param))
    }

    //////////////////////////////////////////////////////
    //                Internal Methods                  //
    //////////////////////////////////////////////////////

    override fun onConfigChanged(type: Int) {
        when (type) {
            DanmakuConfig.TYPE_PLAY_SPEED -> mDataManager.onPlaySpeedChanged()
            DanmakuConfig.TYPE_TEXT_SIZE -> mRenderEngine.typesetting(mIsPlaying, true)
        }
    }

    internal fun onLayoutSizeChanged(width: Int, height: Int) {
        mRenderEngine.onLayoutSizeChanged(width, height)
    }

    internal fun draw(view: View, canvas: Canvas) {
        if (mIsPlaying) {
            val newItems = mDataManager.queryDanmaku()
            mRenderEngine.addItems(newItems)
            mRenderEngine.typesetting(true)
            mRenderEngine.draw(canvas)
            view.postInvalidateCompat()
        } else {
            mRenderEngine.typesetting(false)
            mRenderEngine.draw(canvas)
        }
    }

    internal fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mIsTouchable) {
            return false
        }
        return mTouchHelper.onTouchEvent(event, mRenderEngine)
    }

    internal fun registerCmdMonitor(monitor: ICommandMonitor) {
        mCmdMonitors.add(monitor)
    }

    internal fun unRegisterCmdMonitor(monitor: ICommandMonitor) {
        mCmdMonitors.remove(monitor)
    }

    //////////////////////////////////////////////////////
    //                 Private Methods                  //
    //////////////////////////////////////////////////////

    private fun executeCommand(cmd: DanmakuCommand) {
        mCmdMonitors.forEach {
            it.onCommand(cmd)
        }
    }

    override fun onCommand(cmd: DanmakuCommand) {
        when (cmd.what) {
            CMD_SET_TOUCHABLE -> (cmd.param as? Boolean)?.let { mIsTouchable = it }
        }
    }
}
