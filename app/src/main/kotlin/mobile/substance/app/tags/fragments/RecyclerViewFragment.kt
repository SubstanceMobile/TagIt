/*
 * Copyright 2016 Substance Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mobile.substance.app.tags.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mobile.substance.app.tags.R
import mobile.substance.sdk.music.core.objects.MediaObject
import mobile.substance.sdk.music.loading.LibraryData


/**
 * Created by Julian on 02.06.16.
 */
class RecyclerViewFragment constructor() : Fragment() {

    constructor(callback: RecyclerViewCallback) : this() {
        this.callback = callback
    }

    private var callback: RecyclerViewCallback? = null
    private var recyclerView: RecyclerView? = null
    var type: LibraryData? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.recyclerview, container, false)
        recyclerView = root.findViewById(R.id.app_general_recyclerview) as RecyclerView

        callback?.onReady(recyclerView!!)
        if(type == null) type = callback?.getType()

        return root

    }

    interface RecyclerViewCallback {

        fun onReady(recyclerView: RecyclerView)

        fun getType(): LibraryData

    }

}