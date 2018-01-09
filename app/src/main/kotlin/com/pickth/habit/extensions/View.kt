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

package com.pickth.habit.extensions

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AlphaAnimation
import com.pickth.gachi.extensions.convertDpToPixel
import com.pickth.habit.R
import org.jetbrains.anko.backgroundDrawable

/**
 * Created by yonghoon on 2017-08-10
 */
 
fun View.setShowAlphaAnimation(duration: Long) {
    animation = AlphaAnimation(0f, 1f).apply { this.duration = duration }
}

fun View.setHideAlphaAnimation(duration: Long) {
    animation = AlphaAnimation(1f, 0f).apply { this.duration = duration }
}

fun View.setBackgroundColorWithRadius(color: Int, dpValue: Int) {
    backgroundDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = context.convertDpToPixel(dpValue.toFloat())
    }
}

fun View.setBackgroundColorOnAnimation(preColor: Int, postColor: Int) {
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), preColor, postColor)
            .apply {
                duration = 2000
                addUpdateListener {
                    setBackgroundColor(it.animatedValue as Int)
                }
            }
    colorAnimation.start()
}