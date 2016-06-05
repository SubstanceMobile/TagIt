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

package mobile.substance.app.tags.viewholders

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import mobile.substance.app.tags.R
import mobile.substance.app.tags.utils.RecyclerViewUtil

/**
 * Created by Julian on 02.06.16.
 */
class ItemViewHolder(itemView: View, recyclerView: RecyclerView?) : RecyclerView.ViewHolder(itemView) {
    val ANIM_MAX: Int? = if(recyclerView != null) RecyclerViewUtil.calculateMaxAnimateablePosition(recyclerView) else null
    val title: TextView = itemView.findViewById(R.id.item_title) as TextView
    var subtitle: TextView? = itemView.findViewById(R.id.item_subtitle) as TextView?
    var image: ImageView? = itemView.findViewById(R.id.item_image) as ImageView?
    var animated = false

    fun animateAlbum(adapter: AnimatorListenerAdapter) {
        if(adapterPosition < ANIM_MAX!! && !animated) {
            if(adapterPosition + 1 > ANIM_MAX) animated = true
            itemView.translationY = 800.0f
            itemView.animate()
                    .setStartDelay(adapterPosition * 100.toLong())
                    .setDuration(400)
                    .translationY(0.0f)
                    .setListener(adapter)
                    .start()
        } else adapter.onAnimationCancel(null)
    }
}