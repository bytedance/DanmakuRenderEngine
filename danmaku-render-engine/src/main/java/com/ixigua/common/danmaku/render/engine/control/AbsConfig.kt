package com.ixigua.common.danmaku.render.engine.control

/**
 * Created by dss886 on 2019-05-06.
 */
abstract class AbsConfig {

    private val mListenerList = mutableListOf<ConfigChangeListener>()

    fun addListener(listener: ConfigChangeListener) {
        mListenerList.add(listener)
    }

    @Suppress("unused")
    fun removeListener(listener: ConfigChangeListener) {
        mListenerList.remove(listener)
    }

    internal fun notifyConfigChanged(type: Int) {
        mListenerList.forEach {
            it.onConfigChanged(type)
        }
    }

}