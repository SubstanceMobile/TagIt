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

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue

/**
 * Created by Julian on 02.06.16.
 */
object RecyclerViewUtil {

    fun calculateMaxAnimateablePosition(recyclerView: RecyclerView): Int {
        val fourDpinPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, recyclerView.context.resources.displayMetrics).toInt()
        val spanCount = if (recyclerView.layoutManager !is GridLayoutManager) 1 else (recyclerView.layoutManager as GridLayoutManager).spanCount
        val size = (recyclerView.context.resources.displayMetrics.widthPixels - (spanCount + 1) * fourDpinPx) / spanCount
        return (recyclerView.height / (size.toFloat() +
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 68.0f, recyclerView.context.resources.displayMetrics)
                + fourDpinPx.toFloat()) * spanCount).toInt()
    }

    fun getSpanCount(context: Context): Int {
        if(context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            when(context.resources.configuration.screenLayout) {
                Configuration.SCREENLAYOUT_SIZE_SMALL -> return 2
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> return 2
                Configuration.SCREENLAYOUT_SIZE_LARGE -> return 4
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> return 4
                Configuration.SCREENLAYOUT_SIZE_UNDEFINED -> return 2
            }
        } else if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE || context.resources.configuration.orientation == Configuration.ORIENTATION_UNDEFINED) {
            when(context.resources.configuration.screenLayout) {
                Configuration.SCREENLAYOUT_SIZE_SMALL -> return 4
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> return 4
                Configuration.SCREENLAYOUT_SIZE_LARGE -> return 6
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> return 8
                Configuration.SCREENLAYOUT_SIZE_UNDEFINED -> return 4
            }
        }
        return 2
    }

}