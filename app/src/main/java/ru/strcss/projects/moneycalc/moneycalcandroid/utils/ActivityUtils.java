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

package ru.strcss.projects.moneycalc.moneycalcandroid.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static dagger.internal.Preconditions.checkNotNull;
import static ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(FragmentActivity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void snackBarAction(final Context context, int messageRes, int doCancelRes) {
        snackBarAction(context, context.getText(messageRes), doCancelRes);
    }

    public static void snackBarAction(final Context context, CharSequence message, int doCancelRes) {
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context, message, 3000);

        snackbarWrapper.setAction(context.getText(doCancelRes),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "CANCEL!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        snackbarWrapper.show();
    }

    public static void snackBarAction(CharSequence message) {
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(getAppContext(), message, 3000);
        snackbarWrapper.show();
    }

    public static void snackBarAction(final Context context, CharSequence message) {
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context, message, 3000);
        snackbarWrapper.show();
    }

    public static void snackBarAction(int resId) {
        snackBarAction(resId, 3000);
    }

    public static void snackBarAction(int resId, int duration) {
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(getAppContext(), getAppContext().getText(resId), duration);
        snackbarWrapper.show();
    }

}
