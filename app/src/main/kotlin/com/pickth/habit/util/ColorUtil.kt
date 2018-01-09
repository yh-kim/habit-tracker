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

import android.annotation.SuppressLint
import android.graphics.Color


/**
 * Created by yonghoon on 2018-01-07
 * Blog   : http://blog.pickth.com
 */

object ColorUtil {
    val BLACK = 0
    val WHITE = 1

    /**
     * @return 입력한 색상의 명암을 리턴한다. 명 - WHITE, 암 - BLACK
     */
    @SuppressLint("Range")
    fun getContrastColor(color: Int): Int {
        val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
        return if (y >= 128) WHITE else BLACK
    }
}