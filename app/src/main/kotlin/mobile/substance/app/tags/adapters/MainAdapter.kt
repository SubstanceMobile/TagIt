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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import mobile.substance.app.tags.R
import mobile.substance.app.tags.activities.AlbumEditorActivity
import mobile.substance.app.tags.activities.ArtistDetailActivity
import mobile.substance.app.tags.viewholders.ItemViewHolder
import mobile.substance.sdk.colors.*
import mobile.substance.sdk.music.core.objects.Album
import mobile.substance.sdk.music.core.objects.Artist
import mobile.substance.sdk.music.core.objects.MediaObject
import mobile.substance.sdk.music.core.objects.Song
import mobile.substance.sdk.music.loading.Library
import mobile.substance.sdk.music.loading.LibraryData
import mobile.substance.sdk.music.loading.tasks.Loader

/**
 * Created by Julian on 02.06.16.
 */
class MainAdapter<T : MediaObject>(context: Context, type: LibraryData, recyclerView: RecyclerView) : RecyclerView.Adapter<ItemViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    val context = context
    var items: List<T>? = null
    val recyclerView = recyclerView

    init {
        when (type) {
            LibraryData.ALBUMS -> {
                Library.registerAlbumListener(object : Loader.TaskListener<Album> {

                    override fun onCompleted(result: List<Album>) {
                        items = result as List<T>
                        notifyDataSetChanged()
                    }

                    override fun onOneLoaded(item: Album, pos: Int) {

                    }

                })
                items = Library.albums as List<T>
            }
            LibraryData.ARTISTS -> {
                Library.registerArtistListener(object : Loader.TaskListener<Artist> {

                    override fun onCompleted(result: List<Artist>) {
                        items = result as List<T>
                        notifyDataSetChanged()
                    }

                    override fun onOneLoaded(item: Artist, pos: Int) {

                    }

                })
                items = Library.artists as List<T>
            }
            LibraryData.SONGS -> {
                Library.registerSongListener(object : Loader.TaskListener<Song> {

                    override fun onCompleted(result: List<Song>) {
                        items = result as List<T>
                        notifyDataSetChanged()
                    }

                    override fun onOneLoaded(item: Song, pos: Int) {

                    }

                })
                items = Library.songs as List<T>
            }
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        val itemForCheck = items!!.first()
        when (itemForCheck) {
            is Album -> return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_album, parent, false), recyclerView)
            is Artist -> return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false), null)
            is Song -> return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_song, parent, false), null)
            else -> return null
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        val item = items!![position]

        if (item is Album) {
            holder!!.title?.text = item.albumName
            holder.subtitle?.text = item.albumArtistName
            holder.animateAlbum(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    generateColors(holder, item)
                }
            })
            holder.image?.setImageResource(R.drawable.default_artwork_gem)
            Log.d("MainAdapter", item.albumArtworkPath)
            Glide.with(context).load(item.albumArtworkPath).placeholder(R.drawable.default_artwork_gem).crossFade().into(holder.image)
            holder.itemView.setOnClickListener { context.startActivity(Intent(context, AlbumEditorActivity::class.java).putExtra("album_id", item.id), ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity).toBundle()) }

        } else if (item is Artist) {
            holder!!.title?.text = item.artistName
            holder.itemView.setOnClickListener { context.startActivity(Intent(context, ArtistDetailActivity::class.java).putExtra("artist_id", item.id), ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity).toBundle()) }
        } else if (item is Song) {
            holder!!.title?.text = item.songTitle
            holder.subtitle?.text = item.songArtistName
            holder.image?.setImageResource(R.drawable.default_artwork_gem)
            Library.findAlbumById(item.songAlbumID!!)!!.requestArt(holder.image!!)
        }

    }

    private fun generateColors(holder: ItemViewHolder, item: Album) {
        if (item.colors != null) return animateAlbum(holder, item)
        try {
            DynamicColors
                    .from(item.albumArtworkPath!!)
                    .generateSimple(object : DynamicColorsCallback {
                        override fun onColorsReady(colors: ColorPackage) {
                            item.colors = colors
                            animateAlbum(holder, item)
                        }
                    })
        } catch(e: NullPointerException) { animateAlbum(holder, item)}
    }

    private fun animateAlbum(holder: ItemViewHolder, item: Album) {
        if(item.colors == null) item.colors = DynamicColorsOptions.defaultColors

        val colorAnim = ValueAnimator.ofObject(ArgbEvaluator(), Color.WHITE, (item.colors!! as ColorPackage).primaryColor)
        colorAnim.duration = 500
        colorAnim.addUpdateListener {
            (holder.itemView as CardView).setCardBackgroundColor(it.animatedValue as Int)
        }
        val primaryTextAnim = ValueAnimator.ofObject(ArgbEvaluator(), ColorConstants.TEXT_COLOR_LIGHT_BG, (item.colors!! as ColorPackage).textColor)
        primaryTextAnim.duration = 500
        primaryTextAnim.addUpdateListener {
            holder.title?.setTextColor(it.animatedValue as Int)
        }
        val secondaryTextAnim = ValueAnimator.ofObject(ArgbEvaluator(), ColorConstants.TEXT_COLOR_SECONDARY_LIGHT_BG, (item.colors!! as ColorPackage).secondaryTextColor)
        secondaryTextAnim.duration = 500
        secondaryTextAnim.addUpdateListener {
            holder.subtitle?.setTextColor(it.animatedValue as Int)
        }

        colorAnim.start()
        primaryTextAnim.start()
        secondaryTextAnim.start()
    }

    override fun getSectionName(position: Int): String {
        return items!![position].metadata!!.getString(MediaMetadataCompat.METADATA_KEY_TITLE).first().toString().capitalize()
    }
}