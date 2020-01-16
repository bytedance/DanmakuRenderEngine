package com.ixigua.common.meteor.control

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.data.DataManager
import com.ixigua.common.meteor.render.IRenderLayer
import com.ixigua.common.meteor.render.RenderEngine
import com.ixigua.common.meteor.render.cache.LayerBuffer
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.touch.IItemClickListener
import com.ixigua.common.meteor.touch.TouchHelper
import com.ixigua.common.meteor.utils.*

/**
 * Created by dss886 on 2018/11/6.
 * Main entrance for controlling Danmaku
 */
class DanmakuController(private var mDanmakuView: View): ConfigChangeListener, ICommandMonitor {

    val config: DanmakuConfig = DanmakuConfig().apply {
        addListener(this@DanmakuController)
    }
    val context: Context = mDanmakuView.context
    var itemClickListener: IItemClickListener? = null

    private val mCmdMonitors = mutableListOf<ICommandMonitor>()
    private val mEventListeners = mutableListOf<IEventListener>()
    private val mRenderEngine: RenderEngine = RenderEngine(this)
    private val mDataManager: DataManager = DataManager(this)
    private val mTouchHelper: TouchHelper = TouchHelper()

    private var mIsPlaying = false
    private var mIsTouchable = true

    init {
        mCmdMonitors.add(this)
    }

    //////////////////////////////////////////////////////
    //                 Public Interface                 //
    //////////////////////////////////////////////////////

    /**
     * Play or resume from paused
     */
    fun start(playTime: Long) {
        if (mIsPlaying) {
            return
        }
        mIsPlaying = true
        mDataManager.onPlay(playTime)
        mDanmakuView.postInvalidateCompat()
    }

    /**
     * Pause Danmakus and keep them on the screen
     */
    fun pause() {
        mIsPlaying = false
        mDataManager.onPause()
    }

    /**
     * Stop playing and clear all items on the screen
     */
    fun stop() {
        mIsPlaying = false
        mDataManager.onStop()
        clear()
    }

    /**
     * Simply clear the items on the screen,
     * and has no effect on the data set or timeline.
     */
    fun clear(layerType: Int = LAYER_TYPE_UNDEFINE, notClearOneself : Boolean = false) {
        mRenderEngine.clear(layerType, notClearOneself)
        if (layerType == LAYER_TYPE_UNDEFINE) {
            mDanmakuView.postInvalidateCompat()
        }
    }

    /**
     * Add your custom RenderLayer to RenderEngine.
     * Make sure your LayerType is not the same as the others.
     * Changing the LayerZIndex to adjust the order of your RenderLayer.
     */
    @Suppress("unused")
    fun addRenderLayer(layer: IRenderLayer) {
        mRenderEngine.addRenderLayer(layer)
    }

    /**
     * Register the factory of your custom DrawItem.
     * Make sure your DrawType is not the same as the others.
     */
    fun registerDrawItemFactory(factory: IDrawItemFactory) {
        mRenderEngine.registerDrawItemFactory(factory)
    }

    /**
     * Set data to the [DataManager].
     * Replacing the data source when started is allowed.
     */
    fun setData(dataList: List<DanmakuData>, current: Long = 0) {
        mDataManager.setData(dataList)
        if (current > 0) {
            mDataManager.onPlay(current)
        }
    }

    /**
     * Append data to end of the [DataManager].
     * You need to ensure the chronological order of the data by yourself.
     */
    fun appendData(dataList: List<DanmakuData>) {
        mDataManager.appendData(dataList)
    }

    /**
     * Fake items will be added to the DataManager and push into RenderEngine immediately.
     * Even so, there is no guarantee that them will be displayed 100%,
     * as the render layers will discard data when the amount of data is too large.
     * See [LayerBuffer]
     */
    fun addFakeData(data: DanmakuData) {
        mDataManager.addFakeData(data)
    }

    /**
     * If you change the configs of Danmakus when paused, please invalidate the view.
     */
    fun invalidateView() {
        mDanmakuView.postInvalidateCompat()
    }

    /**
     * Tell [DanmakuController] to do something.
     */
    fun executeCommand(cmd: Int, data: DanmakuData? = null, param: Any? = null) {
        executeCommand(DanmakuCommand(cmd, data, param))
    }

    /**
     * Add event listener to the [DanmakuController].
     * Event is used by [DanmakuController] to tell you what is happening.
     */
    fun addEventListener(listener: IEventListener) {
        mEventListeners.add(listener)
    }

    /**
     * Remove event listener to the [DanmakuController].
     * Event is used by [DanmakuController] to tell you what is happening.
     */
    @Suppress("unused")
    fun removeEventListener(listener: IEventListener) {
        mEventListeners.remove(listener)
    }

    //////////////////////////////////////////////////////
    //                Internal Methods                  //
    //////////////////////////////////////////////////////

    override fun onConfigChanged(type: Int) {
        when (type) {
            DanmakuConfig.TYPE_COMMON_PLAY_SPEED -> mDataManager.onPlaySpeedChanged()
            DanmakuConfig.TYPE_TEXT_SIZE -> mRenderEngine.typesetting(mDataManager.queryPlayTime(), mIsPlaying, true)
        }

        /**
         * Render a frame effect with pause
         */
        if (mIsPlaying) {
            return
        }
        when (type) {
            DanmakuConfig.TYPE_COMMON_TOP_CENTER_VISIBLE_CHANGE -> {
                if (!config.common.topVisible) {
                    mRenderEngine.clear(LAYER_TYPE_TOP_CENTER, true)
                }
            }
            DanmakuConfig.TYPE_COMMON_BOTTOM_CENTER_VISIBLE_CHANGE -> {
                if (!config.common.bottomVisible) {
                    mRenderEngine.clear(LAYER_TYPE_BOTTOM_CENTER, true)
                }
            }
        }
        mDanmakuView.postInvalidateCompat()
    }

    fun getDanmakuData() : List<DanmakuData>{
        return mDataManager.getData()
    }

    internal fun onLayoutSizeChanged(width: Int, height: Int) {
        mRenderEngine.onLayoutSizeChanged(width, height)
    }

    internal fun draw(view: View, canvas: Canvas) {
        val playTime = mDataManager.queryPlayTime()
        if (mIsPlaying) {
            val t0 = System.nanoTime()
            val newItems = mDataManager.queryDanmaku()
            val t1 = System.nanoTime()
            mRenderEngine.addItems(playTime, newItems)
            val t2 = System.nanoTime()
            mRenderEngine.typesetting(playTime, true)
            val t3 = System.nanoTime()
            mRenderEngine.draw(canvas)
            val t4 = System.nanoTime()
            view.postInvalidateCompat()
            if (config.debug.printDrawTimeCostLog) {
                Log.d("DanmakuController", "draw(): query=${String.format("%07d", t1-t0)}, add=${String.format("%07d", t2-t1)}, typesetting=${String.format("%07d", t3-t2)}, draw=${String.format("%07d", t4-t3)}")
            }
        } else {
            mRenderEngine.typesetting(playTime, false)
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

    internal fun notifyEvent(event: DanmakuEvent) {
        mEventListeners.forEach {
            it.onEvent(event)
        }
        Events.recycleEvent(event)
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
            CMD_PAUSE_ITEM -> mDanmakuView.postInvalidateCompat()
            CMD_RESUME_ITEM -> mDanmakuView.postInvalidateCompat()
        }
    }
}
