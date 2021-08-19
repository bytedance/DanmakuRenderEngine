package com.ixigua.common.meteor.control

import android.graphics.Canvas
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
    var itemClickListener: IItemClickListener? = null

    private val mCmdMonitors = mutableListOf<ICommandMonitor>()
    private val mEventListeners = mutableListOf<IEventListener>()
    private val mRenderEngine: RenderEngine = RenderEngine(this)
    private val mDataManager: DataManager = DataManager(this)
    private val mTouchHelper: TouchHelper = TouchHelper()
    private val mProfiler: Profiler = Profiler(config)

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
    fun start(playTime: Long = 0L) {
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
    fun clear(layerType: Int = LAYER_TYPE_UNDEFINE) {
        mRenderEngine.clear(layerType)
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
        if (layer.getLayerType() < 2000) {
            throw IllegalArgumentException("The custom LayerType must not be less than 2000.")
        }
        if (LAYER_Z_INDEXES.contains(layer.getLayerZIndex())) {
            throw IllegalArgumentException("The custom Z-Index conflicts with the built-in Z-Index.")
        }
        mRenderEngine.addRenderLayer(layer)
    }

    /**
     * Register the factory of your custom DrawItem.
     * Make sure your DrawType is not the same as the others.
     */
    fun registerDrawItemFactory(factory: IDrawItemFactory) {
        if (factory.getDrawType() < 2000) {
            throw IllegalArgumentException("The custom DrawType must not be less than 2000.")
        }
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
     * Fake item will be added to the DataManager and pushed into RenderEngine immediately.
     * Even so, there is no guarantee that it will be displayed 100%,
     * as the render layers will discard data when the amount of data is too large.
     * See [LayerBuffer]
     */
    fun addFakeData(data: DanmakuData) {
        mDataManager.addFakeData(data)
        if (config.common.pauseInvalidateWhenBlank) {
            mDanmakuView.postInvalidateCompat()
        }
    }

    /**
     * If you change the configs of Danmakus when paused, please invalidate the view.
     */
    fun invalidateView() {
        if (Logger.isVerboseLoggable()) {
            Logger.v(LOG_TAG_PERFORMANCE, "invalidateView")
        }
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
            DanmakuConfig.TYPE_COMMON_ALPHA -> mDanmakuView.alpha = config.common.alpha / 255f
            DanmakuConfig.TYPE_COMMON_TOP_CENTER_VISIBLE_CHANGE -> {
                if (!config.common.topVisible) {
                    mRenderEngine.clear(LAYER_TYPE_TOP_CENTER)
                }
            }
            DanmakuConfig.TYPE_COMMON_BOTTOM_CENTER_VISIBLE_CHANGE -> {
                if (!config.common.bottomVisible) {
                    mRenderEngine.clear(LAYER_TYPE_BOTTOM_CENTER)
                }
            }
        }
        mDanmakuView.postInvalidateCompat()
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
            val typesettingCount = mRenderEngine.typesetting(playTime, true)
            val t3 = System.nanoTime()
            mRenderEngine.draw(canvas)
            val t4 = System.nanoTime()
            if (!config.common.pauseInvalidateWhenBlank || config.mask.enable) {
                view.postInvalidateCompat()
            } else if (typesettingCount > 0) {
                view.postInvalidateCompat()
            } else if (mDataManager.getRemainDanmakuCount() > 0) {
                mDataManager.nextDanmakuShowAfter().let { showAfter ->
                    if (showAfter in 0..NEXT_DANMAKU_SHOW_MIN_INTERVAL) {
                        view.postInvalidateCompat()
                    } else if (showAfter >= 0) {
                        view.postDelayed({
                            view.postInvalidateCompat()
                        }, showAfter - NEXT_DANMAKU_SHOW_MIN_INTERVAL / 2)
                    }
                }
            }
            mProfiler.profilerDrawTimeCost(canvas, t0, t1, t2, t3, t4)
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

    fun registerCmdMonitor(monitor: ICommandMonitor) {
        mCmdMonitors.add(monitor)
    }

    fun unRegisterCmdMonitor(monitor: ICommandMonitor) {
        mCmdMonitors.remove(monitor)
    }

    fun notifyEvent(event: DanmakuEvent) {
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
