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

package mobile.substance.app.tags.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import mobile.substance.app.tags.R
import mobile.substance.app.tags.SpacingItemDecoration
import mobile.substance.app.tags.VerticalSpacingItemDecoration
import mobile.substance.app.tags.adapters.ArtistAdapter
import mobile.substance.sdk.music.core.objects.Album
import mobile.substance.sdk.music.core.objects.Artist
import mobile.substance.sdk.music.loading.Library
import mobile.substance.sdk.music.loading.LibraryData

class ArtistDetailActivity : AppCompatActivity() {
    private var artist: Artist? = null

    private var albumsRecyclerView: RecyclerView? = null
    private var songsRecyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artist = Library.findArtistById(intent.getLongExtra("artist_id", 0))
        setContentView(R.layout.activity_artist_detail)

        init()
    }

    private fun init() {
        initViews()

        // Toolbar initialization
        setSupportActionBar(toolbar)
        supportActionBar!!.title = artist?.artistName
        toolbar?.setNavigationIcon(R.drawable.ic_close_black_24dp)
        toolbar?.setNavigationOnClickListener { onBackPressed() }

        albumsRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        albumsRecyclerView?.addItemDecoration(VerticalSpacingItemDecoration(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f, resources.displayMetrics).toInt(), false))
        albumsRecyclerView?.adapter = ArtistAdapter<Album>(this, artist!!, LibraryData.ALBUMS)
    }

    private fun initViews() {
        albumsRecyclerView = findViewById(R.id.activity_artist_detail_albums_rv) as RecyclerView
        songsRecyclerView = findViewById(R.id.activity_artist_detail_songs_rv) as RecyclerView
        toolbar = findViewById(R.id.activity_artist_detail_toolbar) as Toolbar
    }
}
