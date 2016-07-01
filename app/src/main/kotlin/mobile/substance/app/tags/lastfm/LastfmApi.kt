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

import mobile.substance.app.tags.BuildConfig

/**
 * Created by Julian on 05.06.16.
 */
object LastFMApi {

    val API_KEY: String = BuildConfig.LASTFM_KEY
    const val API_URL_BASE = "http://ws.audioscrobbler.com/2.0/?method="
    const val API_METHOD_ALBUM_INFO = "album.getInfo"
    const val API_METHOD_ALBUM_SEARCH = "album.search"
    const val API_FORMAT_JSON = "&format=json"

}