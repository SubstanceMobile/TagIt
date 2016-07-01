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

package mobile.substance.app.tags.transitions

/**
 * Created by Julian on 05.06.16.
 */
import android.annotation.TargetApi
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.util.Property

@TargetApi(21)
class MorphDrawable(@ColorInt color: Int, cornerRadius: Float) : Drawable() {
    private val paint: Paint

    var cornerRadius: Float = 0.toFloat()
        set(cornerRadius) {
            this.cornerRadius = cornerRadius
            invalidateSelf()
        }

    init {
        this.cornerRadius = cornerRadius
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
    }

    var color: Int
        get() = paint.color
        set(color) {
            paint.color = color
            invalidateSelf()
        }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat(), cornerRadius, cornerRadius, paint)//hujiawei
    }

    override fun getOutline(outline: Outline) {
        outline.setRoundRect(bounds, cornerRadius)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return paint.alpha
    }

    companion object {

        val CORNER_RADIUS: Property<MorphDrawable, Float> = object : Property<MorphDrawable, Float>(Float::class.java, "cornerRadius") {

            override fun set(morphDrawable: MorphDrawable, value: Float?) {
                morphDrawable.cornerRadius = value!!
            }

            override fun get(morphDrawable: MorphDrawable): Float {
                return morphDrawable.cornerRadius
            }
        }
        val COLOR: Property<MorphDrawable, Int> = object : Property<MorphDrawable, Int>(Int::class.java, "color") {

            override fun set(morphDrawable: MorphDrawable, value: Int?) {
                morphDrawable.color = value!!
            }

            override fun get(morphDrawable: MorphDrawable): Int {
                return morphDrawable.color
            }
        }
    }
}
