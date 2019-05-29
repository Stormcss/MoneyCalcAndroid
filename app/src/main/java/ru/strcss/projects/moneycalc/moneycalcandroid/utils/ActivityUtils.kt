/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.strcss.projects.moneycalc.moneycalcandroid.utils

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import dagger.internal.Preconditions.checkNotNull
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import retrofit2.HttpException
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper

/**
 * This provides methods to help Activities load their UI.
 */
class ActivityUtils {

    companion object {

        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addFragmentToActivity(fragmentManager: FragmentManager,
                                  fragment: Fragment, frameId: Int) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }

        /**
         * Hides the soft keyboard
         */
        fun hideSoftKeyboard(activity: FragmentActivity) {
            if (activity.currentFocus != null) {
                val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }

        fun snackBarAction(context: Context, messageRes: Int, doCancelRes: Int) {
            snackBarAction(context, context.getText(messageRes), doCancelRes)
        }

        fun snackBarAction(context: Context, message: CharSequence, doCancelRes: Int) {
            val snackbarWrapper = SnackbarWrapper.make(context, message, 3000)

            snackbarWrapper.setAction(context.getText(doCancelRes)
            ) {
                Toast.makeText(context, "CANCEL!!!",
                        Toast.LENGTH_SHORT).show()
            }

            snackbarWrapper.show()
        }

        fun snackBarAction(message: CharSequence) {
            val snackbarWrapper = SnackbarWrapper.make(getAppContext(), message, 3000)
            snackbarWrapper.show()
        }

        fun snackBarAction(context: Context, message: CharSequence) {
            val snackbarWrapper = SnackbarWrapper.make(context, message, 3000)
            snackbarWrapper.show()
        }

        @JvmOverloads
        fun snackBarAction(resId: Int, duration: Int = 3000) {
            val snackbarWrapper = SnackbarWrapper.make(getAppContext(), getAppContext().getText(resId), duration)
            snackbarWrapper.show()
        }

        fun changeActivityOnCondition(condition: Boolean, context: Context, targetActivity: Class<*>) {
            if (condition) {
                val intent = Intent(context, targetActivity)
                intent.flags = intent.flags or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        }

        fun changeActivity(context: Context, targetActivity: Class<*>) {
            val intent = Intent(context, targetActivity)
            intent.flags = intent.flags or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun changeActivityOnHttpExceptionCode(throwable: Throwable, code: Int, context: Context, targetActivity: Class<*>): Boolean {
            if (throwable is HttpException && throwable.code() == code) {
                val intent = Intent(context, targetActivity)
                intent.flags = intent.flags or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                return true
            }
            return false
        }

        fun setViewVisibility(view: View?, isShowed: Boolean) {
            if (isShowed) {
                view?.visibility = VISIBLE
            } else {
                view?.visibility = GONE
            }
        }

        fun showAboutPopup(context: Context) {
            val dialogAbout = Dialog(context)
            dialogAbout.setContentView(R.layout.about_popup)

            val closeBtn: ImageView = dialogAbout.findViewById(R.id.aboutCloseButton)

            closeBtn.setOnClickListener { dialogAbout.dismiss() }
            dialogAbout.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogAbout.show()
        }

    }
}
