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

package mobile.substance.app.tags.adapters

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import mobile.substance.app.tags.R
import mobile.substance.app.tags.lastfm.LastFMSearchAlbum

/**
 * Created by Julian on 06.06.16.
 */
class SearchResultsAdapter(items: List<LastFMSearchAlbum>) : ListAdapter {
    val items = items

    override fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val root = LayoutInflater.from(parent!!.context).inflate(R.layout.dialog_list_item, parent, false)

        (root.findViewById(R.id.item_title) as TextView).text = items[position].name
        (root.findViewById(R.id.item_subtitle) as TextView).text = items[position].artist
        Glide.with(parent.context).load(items[position].image?.last()?.text).placeholder(R.drawable.default_artwork_gem).crossFade().into(root.findViewById(R.id.item_image) as ImageView)

        return root
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {

    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {

    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }
}
