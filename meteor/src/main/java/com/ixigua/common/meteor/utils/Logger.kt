package com.ixigua.common.meteor.utils

import android.util.Log
import com.ixigua.common.meteor.BuildConfig

/**
 * Created by dss886 on 2021/07/02.
 */
object Logger {

    private const val TAG = "Logger"

    var logLevel = Log.INFO

    init {
        /**
         * Output less log when we are not in debug mode
         */
        if (!BuildConfig.DEBUG) {
            logLevel = Log.ERROR
        }
    }

    fun v(msg: String) {
        v(TAG, msg)
    }

    fun v(tag: String, msg: String) {
        if (isVerboseLoggable()) {
            Log.v(tag, msg)
        }
    }

    fun isVerboseLoggable(): Boolean {
        return logLevel <= Log.VERBOSE
    }

    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        if (isDebugLoggable()) {
            Log.d(tag, msg)
        }
    }

    fun isDebugLoggable(): Boolean {
        return logLevel <= Log.DEBUG
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        if (isInfoLoggable()) {
            Log.i(tag, msg)
        }
    }

    fun isInfoLoggable(): Boolean {
        return logLevel <= Log.INFO
    }

    fun w(msg: String) {
        w(TAG, msg)
    }

    fun w(tag: String, msg: String) {
        if (isWarnLoggable()) {
            Log.w(tag, msg)
        }
    }

    fun isWarnLoggable(): Boolean {
        return logLevel <= Log.WARN
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        if (isErrorLoggable()) {
            Log.e(tag, msg)
        }
    }

    fun isErrorLoggable(): Boolean {
        return logLevel <= Log.ERROR
    }

}