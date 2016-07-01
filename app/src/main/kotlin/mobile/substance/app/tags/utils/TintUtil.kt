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

package mobile.substance.app.tags.utils

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat

/**
 * Created by Julian on 05.06.16.
 */
object TintUtil {

    fun createTintedDrawable(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        var drawable: Drawable? = drawable ?: return null
        drawable = DrawableCompat.wrap(drawable!!.mutate())
        DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
        DrawableCompat.setTint(drawable, color)
        return drawable
    }
}
