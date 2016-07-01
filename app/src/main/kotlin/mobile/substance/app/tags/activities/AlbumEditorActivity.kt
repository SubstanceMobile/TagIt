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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import butterknife.bindView
import butterknife.bindViews
import com.afollestad.assent.AssentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.rengwuxian.materialedittext.MaterialEditText
import de.julianostarek.generictasks.GenericTask
import de.julianostarek.generictasks.TaskCallback
import mobile.substance.app.tags.CustomFABProgressCircle
import mobile.substance.app.tags.R
import mobile.substance.app.tags.TransitionListenerAdapter
import mobile.substance.app.tags.adapters.SearchResultsAdapter
import mobile.substance.app.tags.lastfm.LastFMSearchAlbum
import mobile.substance.app.tags.lastfm.LastFMService
import mobile.substance.app.tags.utils.TintUtil
import mobile.substance.sdk.colors.ColorPackage
import mobile.substance.sdk.music.core.objects.Album
import mobile.substance.sdk.music.loading.Library

/**
 * Created by Julian on 05.06.16.
 */
class AlbumEditorActivity : AssentActivity() {
    private var album: Album? = null
    private var colors: ColorPackage? = null
    private val image: ImageView by bindView<ImageView>(R.id.activity_album_editor_image)
    private val toolbar: Toolbar by bindView<Toolbar>(R.id.activity_album_editor_toolbar)
    private val fab: FloatingActionButton by bindView<FloatingActionButton>(R.id.activity_album_editor_fab)
    private val fabProgressCircle: CustomFABProgressCircle by bindView<CustomFABProgressCircle>(R.id.activity_album_editor_fab_progresscircle)

    private val editTexts: List<MaterialEditText> by bindViews<MaterialEditText>(R.id.activity_album_editor_et_1, R.id.activity_album_editor_et_2, R.id.activity_album_editor_et_3, R.id.activity_album_editor_et_4, R.id.activity_album_editor_et_5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        album = Library.findAlbumById(intent.getLongExtra("album_id", 0))
        colors = album?.colors as ColorPackage?

        setContentView(R.layout.activity_album_editor)

        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition.addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition?) {
                    fab.animate()!!
                            .setDuration(200)
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .start()
                }
            })
        } else {
            fab.animate()!!
                    .setStartDelay(500)
                    .setDuration(200)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .start()
        }

        image.setImageBitmap(BitmapFactory.decodeFile(album?.albumArtworkPath))

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fab.backgroundTintList = ColorStateList.valueOf(colors!!.accentColor)
        fab.setImageDrawable(TintUtil.createTintedDrawable(resources.getDrawable(R.drawable.ic_autorenew_black_24dp), colors!!.accentIconActiveColor))

        fabProgressCircle.setArcColor(colors!!.primaryColor)

        fab.setOnClickListener {
            fabProgressCircle.show()
            GenericTask<List<LastFMSearchAlbum>?>(object : TaskCallback<List<LastFMSearchAlbum>?> {
                override fun onTaskResult(result: List<LastFMSearchAlbum>?, failed: Boolean) {
                    fabProgressCircle.hide()
                    MaterialDialog.Builder(this@AlbumEditorActivity)
                            .title("These albums were found")
                            .adapter(SearchResultsAdapter(result!!), object : MaterialDialog.ListCallback {
                                override fun onSelection(dialog: MaterialDialog?, itemView: View?, which: Int, text: CharSequence?) {

                                }
                            })
                            .show()
                }
            }).execute { LastFMService.searchAlbumAsync(this, album?.albumName!!) }
        }

        if (savedInstanceState == null) {
            editTexts[0].setText(album?.albumName)
            editTexts[1].setText(album?.albumArtistName)
            editTexts[2].setText(album?.albumYear.toString())
        }

    }

    override fun onBackPressed() {
        fab.animate()!!
                .setDuration(200)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        try {
                            super@AlbumEditorActivity.onBackPressed()
                        } catch (e: Exception) {}
                    }
                })
                .start()
    }

}