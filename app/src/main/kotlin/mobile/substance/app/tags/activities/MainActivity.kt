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

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.MenuItem
import butterknife.bindView
import com.afollestad.assent.Assent
import com.afollestad.assent.AssentActivity
import com.afollestad.assent.AssentCallback
import com.afollestad.assent.PermissionResultSet
import mobile.substance.app.tags.DividerItemDecoration
import mobile.substance.app.tags.R
import mobile.substance.app.tags.SpacingItemDecoration
import mobile.substance.app.tags.TagIt
import mobile.substance.app.tags.adapters.MainAdapter
import mobile.substance.app.tags.fragments.RecyclerViewFragment
import mobile.substance.app.tags.utils.RecyclerViewUtil
import mobile.substance.sdk.music.core.objects.Album
import mobile.substance.sdk.music.core.objects.Artist
import mobile.substance.sdk.music.core.objects.Song
import mobile.substance.sdk.music.loading.LibraryData
import java.util.*

class MainActivity : AssentActivity(), AssentCallback {

    companion object {
        val READ_PERMISSION_REQUEST_CODE: Int by lazy {
            Random().nextInt(100)
        }
        val WRITE_PERMISSION_REQUEST_CODE: Int by lazy {
            Random().nextInt(100)
        }
    }

    override fun onPermissionResult(result: PermissionResultSet?) {
        if(result!!.allPermissionsGranted())
            init(null)
    }

    private val drawerLayout: DrawerLayout by bindView<DrawerLayout>(R.id.activity_main_drawer_layout)
    private val navigationView: NavigationView by bindView<NavigationView>(R.id.activity_main_navigationview)
    private val toolbar: Toolbar by bindView<Toolbar>(R.id.aactivity_main_toolbar)
    private var fragment: RecyclerViewFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread() {
            run { if (!Assent.isPermissionGranted(Assent.READ_EXTERNAL_STORAGE)) Assent.requestPermissions(this, READ_PERMISSION_REQUEST_CODE, Assent.READ_EXTERNAL_STORAGE) else runOnUiThread { init(savedInstanceState) } }
        }.start()
    }

    private fun init(savedInstanceState: Bundle?) {

        (application as TagIt).buildLibrary()

        // Toolbar initialization
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_drawer_black_24dp)
        toolbar.setNavigationOnClickListener { drawerLayout?.openDrawer(GravityCompat.START) }

        // NavigationView initialization
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            it.isChecked = true
            handleNavigation(false, it)
        }

        // App navigation
        if (savedInstanceState != null) {
            fragment = supportFragmentManager.getFragment(savedInstanceState, "FRAGMENT") as RecyclerViewFragment
            handleNavigation(false, null)
        } else handleNavigation(true, null)

    }

    private fun handleNavigation(launch: Boolean, item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.nav_albums -> {
                    if (fragment?.type != LibraryData.ALBUMS) fragment = RecyclerViewFragment(albumsCallback) else return false
                }
                R.id.nav_artists -> {
                    if (fragment?.type != LibraryData.ARTISTS) fragment = RecyclerViewFragment(artistsCallback) else return false
                }
                R.id.nav_songs -> {
                    if (fragment?.type != LibraryData.SONGS) fragment = RecyclerViewFragment(songsCallback) else return false
                }
            }
        } else {
            fragment = RecyclerViewFragment(albumsCallback)
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_fragment_placeholder, fragment)
                .commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if(Assent.isPermissionGranted(Assent.READ_EXTERNAL_STORAGE)) {
            super.onSaveInstanceState(outState)
            if (fragment != null) supportFragmentManager.putFragment(outState, "FRAGMENT", fragment)
        }
    }

    object albumsCallback : RecyclerViewFragment.RecyclerViewCallback {

        override val type: LibraryData = LibraryData.ALBUMS

        override fun onReady(recyclerView: RecyclerView) {
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
            recyclerView.addItemDecoration(SpacingItemDecoration(RecyclerViewUtil.getSpanCount(recyclerView.context), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f, recyclerView.context.resources.displayMetrics).toInt(), true, true))
            recyclerView.adapter = MainAdapter<Album>(recyclerView.context, LibraryData.ALBUMS, recyclerView)
        }

    }

    object artistsCallback : RecyclerViewFragment.RecyclerViewCallback {

        override val type: LibraryData = LibraryData.ARTISTS

        override fun onReady(recyclerView: RecyclerView) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = MainAdapter<Artist>(recyclerView.context, LibraryData.ARTISTS, recyclerView)
        }

    }

    object songsCallback : RecyclerViewFragment.RecyclerViewCallback {

        override val type: LibraryData = LibraryData.SONGS

        override fun onReady(recyclerView: RecyclerView) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = MainAdapter<Song>(recyclerView.context, LibraryData.SONGS, recyclerView)
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL))
        }

    }

}
