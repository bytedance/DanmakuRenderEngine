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

import com.ixigua.common.danmaku.render.engine.data.DanmakuData

/**
 * Created by dss886 on 2019-05-09.
 *
 * Use Events.obtainEvent() to gain a empty Event.
 * Events will be recycled automatically after be notified.
 * See: [DanmakuController.notifyEvent]
 */
class DanmakuEvent(var what: Int, var data: DanmakuData? = null, var param: Any? = null) {

    fun reset() {
        what = 0
        data = null
        param = null
    }

}