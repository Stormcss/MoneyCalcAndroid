package ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.androidcomponents

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by Stormcss
 * Date: 28.01.2019
 */
class VerticalViewPager : ViewPager {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        setPageTransformer(true, PageTransformer())

        // disable over scroll shadow
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val interceped = super.onInterceptTouchEvent(swapXY(ev))
        swapXY(ev) // swap x,y back for other touch events.
        return interceped
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }

    // swap x and y
    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()

        val newX = ev.rawY / height * width
        val newY = ev.rawX / width * height

        ev.setLocation(newX, newY)
        return ev
    }

    inner class PageTransformer : ViewPager.PageTransformer {

        override fun transformPage(page: View, position: Float) {

            if (position < -1) {
                // [-infinity, -1], view page is off-screen to the left

                // hide the page.
                page.visibility = View.INVISIBLE

            } else if (position <= 1) {
                // [-1, 1], page is on screen

                // show the page
                page.visibility = View.VISIBLE

                // get page back to the center of screen since it will get swipe horizontally by default.
                page.translationX = page.width * -position

                // set Y position to swipe in vertical direction.
                val y = position * page.height
                page.translationY = y

            } else {
                // [1, +infinity], page is off-screen to the right

                // hide the page.
                page.visibility = View.INVISIBLE
            }
        }

    }
}
