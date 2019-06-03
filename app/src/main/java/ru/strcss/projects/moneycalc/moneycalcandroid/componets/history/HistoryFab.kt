package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.ScaleAnimation

import com.gordonwong.materialsheetfab.AnimatedFab

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R

class HistoryFab : FloatingActionButton, AnimatedFab {

    private val interpolator: Interpolator
        get() = AnimationUtils.loadInterpolator(context, R.interpolator.msf_interpolator)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * Shows the FAB.
     */
    override fun show() {
        show(0f, 0f)
    }

    /**
     * Shows the FAB and sets the FAB's translation.
     *
     * @param translationX translation X value
     * @param translationY translation Y value
     */
    override fun show(translationX: Float, translationY: Float) {
        // Set FAB's translation
        //        setTranslation(translationX, translationY);
        //
        //        // Only use scale animation if FAB is hidden
        //        if (getVisibility() != View.VISIBLE) {
        //            // Pivots indicate where the animation begins from
        //            float pivotX = getPivotX() + translationX;
        //            float pivotY = getPivotY() + translationY;
        //
        //            ScaleAnimation anim;
        //            // If pivots are 0, that means the FAB hasn't been drawn yet so just use the
        //            // center of the FAB
        //            if (pivotX == 0 || pivotY == 0) {
        //                anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
        //                        Animation.RELATIVE_TO_SELF, 0.5f);
        //            } else {
        //                anim = new ScaleAnimation(0, 1, 0, 1, pivotX, pivotY);
        //            }
        //
        //            // Animate FAB expanding
        //            anim.setDuration(FAB_ANIM_DURATION);
        //            anim.setInterpolator(getInterpolator());
        //            startAnimation(anim);
        //        }
        visibility = View.VISIBLE
    }

    /**
     * Hides the FAB.
     */
    override fun hide() {
        // Only use scale animation if FAB is visible
        if (visibility == View.VISIBLE) {
            // Pivots indicate where the animation begins from
            val pivotX = pivotX + translationX
            val pivotY = pivotY + translationY

            // Animate FAB shrinking
            val anim = ScaleAnimation(1f, 0f, 1f, 0f, pivotX, pivotY)
            anim.duration = FAB_ANIM_DURATION.toLong()
            anim.interpolator = interpolator
            startAnimation(anim)
        }
        visibility = View.INVISIBLE
    }

    private fun setTranslation(translationX: Float, translationY: Float) {
        animate().setInterpolator(interpolator).setDuration(FAB_ANIM_DURATION.toLong())
                .translationX(translationX).translationY(translationY)
    }

    companion object {

        private val FAB_ANIM_DURATION = 200
    }
}
