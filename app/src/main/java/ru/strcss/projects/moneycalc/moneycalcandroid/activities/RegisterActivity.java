package ru.strcss.projects.moneycalc.moneycalcandroid.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etRegisterLogin = (EditText) findViewById(R.id.etRegisterLogin);
        final EditText etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        final EditText etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        final EditText etRegisterName = (EditText) findViewById(R.id.etRegisterName);
        final FloatingActionButton fabRegisterDone = (FloatingActionButton) findViewById(R.id.fabRegisterDone);

    }
}
