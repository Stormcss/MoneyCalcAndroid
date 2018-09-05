package ru.strcss.projects.moneycalc.moneycalcandroid;

import android.content.Context;

public interface BaseView<T> {
    void showErrorMessage(String msg);

    Context getContext();
}
