package com.ixigua.common.meteor.control

/**
 * Created by dss886 on 2019-05-06.
 */
abstract class AbsConfig {

    private val mListenerList = mutableListOf<ConfigChangeListener>()

    internal fun addListener(listener: ConfigChangeListener) {
        mListenerList.add(listener)
    }

    @Suppress("unused")
    internal fun removeListener(listener: ConfigChangeListener) {
        mListenerList.remove(listener)
    }

    internal fun notifyConfigChanged(type: Int) {
        mListenerList.forEach {
            it.onConfigChanged(type)
        }
    }

}