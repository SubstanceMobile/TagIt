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

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.transition.ArcMotion
import android.transition.ChangeBounds
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

@TargetApi(21)
open class ViewMorphTransition : ChangeBounds {
    var isShowViewGroup = false
    private var startCornerRadius = 0
    private var endCornerRadius = 0

    private constructor(startCornerRadius: Int, endCornerRadius: Int, isShowViewGroup: Boolean) : super() {
        setStartCornerRadius(startCornerRadius)
        setEndCornerRadius(endCornerRadius)
        setIsShowViewGroup(isShowViewGroup)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun getTransitionProperties(): Array<String> {
        return TRANSITION_PROPERTIES
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        super.captureStartValues(transitionValues)
        val mView = transitionValues.view
        if (mView.width <= 0 || mView.height <= 0) {
            return
        }
        transitionValues.values.put(PROPERTY_CORNER_RADIUS, startCornerRadius)//mView.getHeight() / 2
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        super.captureEndValues(transitionValues)
        val mView = transitionValues.view
        if (mView.width <= 0 || mView.height <= 0) {
            return
        }
        transitionValues.values.put(PROPERTY_CORNER_RADIUS, endCornerRadius)
    }

    open val isWithColors: Boolean
        get() = false

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        if (isWithColors) return super.createAnimator(sceneRoot, startValues, endValues)
        val changeBounds = super.createAnimator(sceneRoot, startValues, endValues)
        if (startValues == null || endValues == null || changeBounds == null) {
            return null
        }
        val startCornerRadius = startValues.values[PROPERTY_CORNER_RADIUS] as Int
        val endCornerRadius = endValues.values[PROPERTY_CORNER_RADIUS] as Int
        if (startCornerRadius == null || endCornerRadius == null) {
            return null
        }

        val background = MorphDrawable(0, startCornerRadius.toFloat())
        endValues.view.background = background
        val corners = ObjectAnimator.ofFloat<MorphDrawable>(background, MorphDrawable.CORNER_RADIUS, endCornerRadius.toFloat())
        if (isShowViewGroup) {
            if (endValues.view is ViewGroup) {
                val mViewGroup = endValues.view as ViewGroup
                var offset = (mViewGroup.height / 3).toFloat()
                for (i in 0..mViewGroup.childCount - 1) {
                    val v = mViewGroup.getChildAt(i)
                    v.translationY = offset
                    v.alpha = 0f
                    v.animate().alpha(1f).translationY(0f).setDuration(150).setStartDelay(150).setInterpolator(AnimationUtils.loadInterpolator(mViewGroup.context, android.R.interpolator.fast_out_slow_in)).start()
                    offset *= 1.8f
                }
            }
        } else {//hide child views
            if (endValues.view is ViewGroup) {
                val vg = endValues.view as ViewGroup
                for (i in 0..vg.childCount - 1) {
                    val v = vg.getChildAt(i)
                    v.animate().alpha(0f).translationY((v.height / 3).toFloat()).setStartDelay(0L).setDuration(50L).setInterpolator(AnimationUtils.loadInterpolator(vg.context, android.R.interpolator.fast_out_linear_in)).start()
                }
            }
        }
        val transition = AnimatorSet()
        transition.playTogether(changeBounds, corners)
        transition.duration = 300
        transition.interpolator = AnimationUtils.loadInterpolator(sceneRoot.context, android.R.interpolator.fast_out_slow_in)
        return transition
    }

    fun setEndCornerRadius(endCornerRadius: Int) {
        this.endCornerRadius = endCornerRadius
    }

    fun setStartCornerRadius(startCornerRadius: Int) {
        this.startCornerRadius = startCornerRadius
    }

    fun setIsShowViewGroup(isShowViewGroup: Boolean) {
        this.isShowViewGroup = isShowViewGroup
    }

    class WithColor : ViewMorphTransition {
        private var startColor = Color.TRANSPARENT
        private var endColor = Color.TRANSPARENT

        private val TRANSITION_PROPERTIES = arrayOf(super.getTransitionProperties()[0], PROPERTY_COLOR)

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        }

        private constructor(startCornerRadius: Int, endCornerRadius: Int, isShowViewGroup: Boolean, startColor: Int, endColor: Int) : super(startCornerRadius, endCornerRadius, isShowViewGroup) {
            setStartColor(startColor)
            setEndColor(endColor)
        }

        fun setEndColor(endColor: Int) {
            this.endColor = endColor
        }

        fun setStartColor(startColor: Int) {
            this.startColor = startColor
        }

        override val isWithColors: Boolean
            get() = true

        override fun getTransitionProperties(): Array<String> {
            return TRANSITION_PROPERTIES
        }

        override fun captureStartValues(transitionValues: TransitionValues) {
            super.captureStartValues(transitionValues)
            transitionValues.values.put(PROPERTY_COLOR, startColor)
        }

        override fun captureEndValues(transitionValues: TransitionValues) {
            super.captureEndValues(transitionValues)
            transitionValues.values.put(PROPERTY_COLOR, endColor)
        }

        override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
            val changeBounds = super.createAnimator(sceneRoot, startValues, endValues)
            if (startValues == null || endValues == null || changeBounds == null) {
                return null
            }
            val startColor = startValues.values[PROPERTY_COLOR] as Int
            val startCornerRadius = startValues.values[PROPERTY_CORNER_RADIUS] as Int
            val endColor = endValues.values[PROPERTY_COLOR] as Int
            val endCornerRadius = endValues.values[PROPERTY_CORNER_RADIUS] as Int
            if (startColor == null || startCornerRadius == null || endColor == null || endCornerRadius == null) {
                return null
            }

            val background = MorphDrawable(startColor, startCornerRadius.toFloat())
            endValues.view.background = background
            val color = ObjectAnimator.ofArgb(background, MorphDrawable.COLOR, endColor)
            val corners = ObjectAnimator.ofFloat(background, MorphDrawable.CORNER_RADIUS, endCornerRadius.toFloat())
            if (isShowViewGroup) {
                if (endValues.view is ViewGroup) {
                    val mViewGroup = endValues.view as ViewGroup
                    var offset = (mViewGroup.height / 3).toFloat()
                    for (i in 0..mViewGroup.childCount - 1) {
                        val v = mViewGroup.getChildAt(i)
                        v.translationY = offset
                        v.alpha = 0f
                        v.animate().alpha(1f).translationY(0f).setDuration(150).setStartDelay(150).setInterpolator(AnimationUtils.loadInterpolator(mViewGroup.context, android.R.interpolator.fast_out_slow_in)).start()
                        offset *= 1.8f
                    }
                }
            } else {//hide child views
                if (endValues.view is ViewGroup) {
                    val vg = endValues.view as ViewGroup
                    for (i in 0..vg.childCount - 1) {
                        val v = vg.getChildAt(i)
                        v.animate().alpha(0f).translationY((v.height / 3).toFloat()).setStartDelay(0L).setDuration(50L).setInterpolator(AnimationUtils.loadInterpolator(vg.context, android.R.interpolator.fast_out_linear_in)).start()
                    }
                }
            }
            val transition = AnimatorSet()
            transition.playTogether(changeBounds, corners, color)
            transition.duration = 300
            transition.interpolator = AnimationUtils.loadInterpolator(sceneRoot.context, android.R.interpolator.fast_out_slow_in)
            return transition
        }

        companion object {

            private val PROPERTY_COLOR = "color"

            fun initEnterTransition(activity: Activity, viewToAnimate: View?, startCornerRadius: Int, endCornerRadius: Int, withPathMotion: Boolean, startColor: Int, endColor: Int) {
                val interpolator = AnimationUtils.loadInterpolator(activity, android.R.interpolator.accelerate_decelerate)
                val sharedEnter = WithColor(startCornerRadius, endCornerRadius, true, startColor, endColor)
                if (withPathMotion) {
                    val mArcMotion = newArcMotion()
                    sharedEnter.pathMotion = mArcMotion
                }
                sharedEnter.interpolator = interpolator
                if (viewToAnimate != null) {
                    sharedEnter.addTarget(viewToAnimate)
                }
                activity.window.sharedElementEnterTransition = sharedEnter
            }

            fun initReturnTransition(activity: Activity, viewToAnimate: View?, startCornerRadius: Int, endCornerRadius: Int, withPathMotion: Boolean, startColor: Int, endColor: Int) {
                val easeInOut = AnimationUtils.loadInterpolator(activity, android.R.interpolator.accelerate_decelerate)
                val sharedReturn = WithColor(startCornerRadius, endCornerRadius, true, startColor, endColor)
                if (withPathMotion) {
                    val mArcMotion = newArcMotion()
                    sharedReturn.pathMotion = mArcMotion
                }
                sharedReturn.interpolator = easeInOut
                if (viewToAnimate != null) {
                    sharedReturn.addTarget(viewToAnimate)
                }
                activity.window.sharedElementReturnTransition = sharedReturn
            }
        }
    }

    companion object {

        val PROPERTY_CORNER_RADIUS = "cornerRadius"
        val TRANSITION_PROPERTIES = arrayOf(PROPERTY_CORNER_RADIUS)

        fun newArcMotion(): ArcMotion {
            val arcMotion = ArcMotion()
            arcMotion.minimumHorizontalAngle = 50f
            arcMotion.minimumVerticalAngle = 50f
            return arcMotion
        }

        fun initEnterTransition(activity: Activity, viewToAnimate: View?, startCornerRadius: Int, endCornerRadius: Int, withPathMotion: Boolean) {

            val interpolator = AnimationUtils.loadInterpolator(activity, android.R.interpolator.accelerate_decelerate)
            val sharedEnter = ViewMorphTransition(startCornerRadius, endCornerRadius, true)
            if (withPathMotion) {
                val mArcMotion = newArcMotion()
                sharedEnter.pathMotion = mArcMotion
            }
            sharedEnter.interpolator = interpolator
            if (viewToAnimate != null) {
                sharedEnter.addTarget(viewToAnimate)
            }
            activity.window.sharedElementEnterTransition = sharedEnter
        }

        fun initReturnTransition(activity: Activity, viewToAnimate: View?, startCornerRadius: Int, endCornerRadius: Int, withPathMotion: Boolean) {
            val easeInOut = AnimationUtils.loadInterpolator(activity, android.R.interpolator.accelerate_decelerate)
            val sharedReturn = ViewMorphTransition(startCornerRadius, endCornerRadius, true)
            if (withPathMotion) {
                val mArcMotion = newArcMotion()
                sharedReturn.pathMotion = mArcMotion
            }
            sharedReturn.interpolator = easeInOut
            if (viewToAnimate != null) {
                sharedReturn.addTarget(viewToAnimate)
            }
            activity.window.sharedElementReturnTransition = sharedReturn
        }
    }

}
