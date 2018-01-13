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

package com.pickth.habit.view.main.adapter.item.viewholder

import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeContentAd
import com.google.android.gms.ads.formats.NativeContentAdView
import com.pickth.habit.R
import com.pickth.habit.view.main.adapter.item.Habit
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ad_content.view.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.toast

/**
 * Created by yonghoon on 2018-01-13
 * Blog   : http://blog.pickth.com
 */

class AdViewHolder(val view: View, val builder: AdLoader.Builder): MainViewHolder(view) {
    override fun onBind() {
        val adView = view as NativeContentAdView
        builder.forContentAd { ad ->
            populateContentAdView(ad, adView)
        }

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                (itemView.adView.backgroundDrawable as GradientDrawable).setColor(ContextCompat.getColor(view.context, R.color.colorWhite))
            }
            override fun onAdFailedToLoad(errorCode: Int) {
                view.context.toast("Failed to load native ad: " + errorCode)
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onBind(item: Habit, position: Int) {
    }

    /**
     * Populates a [NativeContentAdView] object with data from a given
     * [NativeContentAd].
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private fun populateContentAdView(nativeContentAd: NativeContentAd,
                                      adView: NativeContentAdView) {

        adView.headlineView = adView.findViewById(R.id.contentad_headline)
        adView.imageView = adView.findViewById(R.id.contentad_image)
        adView.bodyView = adView.findViewById(R.id.contentad_body)
        adView.callToActionView = adView.findViewById(R.id.contentad_call_to_action)
        adView.logoView = adView.findViewById(R.id.contentad_logo)
        adView.advertiserView = adView.findViewById(R.id.contentad_advertiser)

        // Some assets are guaranteed to be in every NativeContentAd.
        (adView.headlineView as TextView).text = nativeContentAd.headline
        (adView.bodyView as TextView).text = nativeContentAd.body
        (adView.callToActionView as TextView).text = nativeContentAd.callToAction
        (adView.advertiserView as TextView).text = nativeContentAd.advertiser

        val images = nativeContentAd.images

        if (images.size > 0) {
            (adView.imageView as ImageView).setImageDrawable(images[0].drawable)
        }

        // Some aren't guaranteed, however, and should be checked.
        val logoImage = nativeContentAd.logo

        if (logoImage == null) {
            adView.logoView.visibility = View.INVISIBLE
        } else {
            (adView.logoView as ImageView).setImageDrawable(logoImage.drawable)
            adView.logoView.visibility = View.VISIBLE
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd)
    }
}