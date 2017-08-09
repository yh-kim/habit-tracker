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

package com.pickth.habit.view.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.pickth.gachi.util.GridSpacingItemDecoration
import com.pickth.habit.R
import com.pickth.habit.view.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by yonghoon on 2017-08-09
 */

class MainActivity: AppCompatActivity(), MainContract.View {

    private lateinit var mPresenter: MainContract.Presenter
    private lateinit var mAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // actionbar
        setSupportActionBar(main_toolbar)

        // adapter
        mAdapter = MainAdapter()

        // presenter
        mPresenter = MainPresenter().apply {
            attachView(this@MainActivity)
            setAdapterView(mAdapter)
            setAdapterModel(mAdapter)
            inputTest()
        }

        rv_main.run {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridSpacingItemDecoration(context,2, 16, false))
        }
    }
}