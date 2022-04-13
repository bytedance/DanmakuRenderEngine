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
package com.ixigua.common.danmaku.render.engine.control

/**
 * Created by dss886 on 2019/9/26.
 *
 * Event is used by [DanmakuController] to tell you what is happening.
 * See: [ICommandMonitor]
 */
interface IEventListener {

    /**
     * The param event will be recycled and reused after onEvent()
     */
    fun onEvent(event: DanmakuEvent)

}