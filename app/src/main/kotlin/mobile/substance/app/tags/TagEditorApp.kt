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

package mobile.substance.app.tags

import android.app.Application
import mobile.substance.sdk.music.core.MusicCoreOptions
import mobile.substance.sdk.music.loading.Library
import mobile.substance.sdk.music.loading.LibraryConfig
import mobile.substance.sdk.music.loading.LibraryData

/**
 * Created by Julian on 03.06.16.
 */
class TagEditorApp : Application() {
    private var isBuilt = false

    override fun onCreate() {
        super.onCreate()
    }

    fun buildLibrary() {
        if(isBuilt) return

        Library.init(this, LibraryConfig()
                .put(LibraryData.ALBUMS)
                .put(LibraryData.ARTISTS)
                .put(LibraryData.SONGS))
        Library.build()

        MusicCoreOptions.defaultArt = R.drawable.default_artwork_gem
    }

}