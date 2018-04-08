package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;

public class RegisterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_frag, container, false);
    }
}
