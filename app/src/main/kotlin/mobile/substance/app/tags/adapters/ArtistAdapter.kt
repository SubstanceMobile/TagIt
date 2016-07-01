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

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import mobile.substance.app.tags.R
import mobile.substance.app.tags.viewholders.ItemViewHolder
import mobile.substance.sdk.music.core.objects.Album
import mobile.substance.sdk.music.core.objects.Artist
import mobile.substance.sdk.music.core.objects.MediaObject
import mobile.substance.sdk.music.core.objects.Song
import mobile.substance.sdk.music.core.utils.ModularAsyncTask
import mobile.substance.sdk.music.loading.Library
import mobile.substance.sdk.music.loading.LibraryData

/**
 * Created by Julian on 03.06.16.
 */
class ArtistAdapter<T : MediaObject>(context: Context, artist: Artist, type: LibraryData) : RecyclerView.Adapter<ItemViewHolder>() {
    val context = context
    var items: List<T>? = null
    val type = type

    init {
        when(type) {
            LibraryData.ALBUMS -> Library.findAlbumsForArtistAsync(artist, object : ModularAsyncTask.TaskCallback<List<Album>> {
                override fun onTaskFailed(e: Exception) {

                }

                override fun onTaskResult(result: List<Album>) {
                    items = result as List<T>
                    notifyDataSetChanged()
                }

                override fun onTaskStart() {

                }
            })
            LibraryData.SONGS -> Library.findSongsForArtistAsync(artist, object : ModularAsyncTask.TaskCallback<List<Song>> {
                override fun onTaskFailed(e: Exception) {

                }

                override fun onTaskResult(result: List<Song>) {
                    items = result as List<T>
                    notifyDataSetChanged()
                }

                override fun onTaskStart() {

                }
            })
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        val item = items!![position]

        if(item is Album) {
            holder?.title?.text = item.albumName
            holder?.subtitle?.text = item.albumYear?.toString()
            Glide.with(context).load(item.albumArtworkPath).crossFade().into(holder?.image!!)
        } else if(item is Song) {
            holder?.title?.text = item.songTitle
            holder?.subtitle?.text = item.songAlbumName
            Library.findAlbumById(item.songAlbumID!!)?.requestArt(holder?.image!!)
        }
    }

    override fun getItemCount(): Int {
        return if(items == null) 0 else items!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        when(type) {
            LibraryData.ALBUMS -> return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_album_vertical, parent, false), null)
            LibraryData.SONGS -> return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_song, parent, false), null)
            else -> return null
        }
    }



}