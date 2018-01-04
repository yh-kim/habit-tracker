/*
 * Copyright 2017 Yonghoon Kim
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

package com.pickth.habit.util

import android.support.v4.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yonghoon on 2017-08-14
 */

object StringUtil {
    fun getCurrentDay(): String = SimpleDateFormat("yyyy:MM:dd")
            .format(Date(System.currentTimeMillis()))

    fun formatDayToString(day: String): String {
        var result = (System.currentTimeMillis() - SimpleDateFormat("yyyy:MM:dd")
                .parse(day)
                .time) / 1000

        // 초
        result /= 60
        // 분
        result /= 60
        // 시
        result /= 24

        return result.toString()
//        if(result.toInt() == 0) {
//            return "오늘"
//        }
//        return "${result}일 전"
    }
}