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

package mobile.substance.app.tags.lastfm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Julian on 28.06.16.
 */
class LastFMSearchAlbum {

    @Expose
    val name: String? = null

    @Expose
    val artist: String? = null

    @Expose
    val url: String? = null

    @Expose
    val mbid: String? = null

    @Expose
    val image: List<Image>? = null

    class Image {

        @Expose @SerializedName("#text")
        val text: String? = null

        @Expose
        val size: String? = null

    }

    class AlbumMatches {

        @Expose
        val album: List<LastFMSearchAlbum>? = null

    }

    class Results {

        @Expose
        val albummatches: AlbumMatches? = null

    }

    class Response {

        @Expose
        val results: Results? = null

    }

}