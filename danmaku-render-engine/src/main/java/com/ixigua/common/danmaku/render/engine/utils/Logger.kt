/*
 * Copyright (C) 2022 ByteDance Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ixigua.common.danmaku.render.engine.utils

import android.util.Log
import com.ixigua.common.danmaku.render.engine.BuildConfig

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