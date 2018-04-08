package ru.strcss.projects.moneycalc.moneycalcandroid.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.strcss.projects.moneycalc.dto.AjaxRs;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.dto.Status;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Identifications;
import ru.strcss.projects.moneycalc.enitities.Person;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.DaggerTestComponent;
import ru.strcss.projects.moneycalc.moneycalcandroid.handlers.ServerHandler;

public class RegisterActivity extends AppCompatActivity {

    @Inject
    ServerHandler serverHandler;

    // UI references.
    private EditText etRegisterLogin;
    private EditText etRegisterPassword;
    private EditText etRegisterEmail;
    private EditText etRegisterName;
    private FloatingActionButton fabRegisterDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DaggerTestComponent.builder().build().inject(this);

        etRegisterLogin = (EditText) findViewById(R.id.etRegisterLogin);
        etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etRegisterName = (EditText) findViewById(R.id.etRegisterName);
        fabRegisterDone = (FloatingActionButton) findViewById(R.id.fabRegisterDone);

        fabRegisterDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        // Reset errors.
        etRegisterLogin.setError(null);
        etRegisterPassword.setError(null);
        etRegisterEmail.setError(null);
        etRegisterName.setError(null);

        // Store values at the time of the login attempt.
        String login = etRegisterLogin.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String name = etRegisterName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(login)) {
            etRegisterLogin.setError(getString(R.string.error_field_required));
            focusView = etRegisterLogin;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            etRegisterEmail.setError(getString(R.string.error_field_required));
            focusView = etRegisterEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError(getString(R.string.error_field_required));
            focusView = etRegisterPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(name)) {
            etRegisterName.setError(getString(R.string.error_field_required));
            focusView = etRegisterName;
            cancel = true;
        }
        Toast.makeText(getApplicationContext(), "cancel is " + cancel, Toast.LENGTH_LONG).show();
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            final Identifications identifications = Identifications.builder()
                    .name(etRegisterName.getText().toString())
                    .build();

            final Access access = Access.builder()
                    .login(etRegisterLogin.getText().toString())
                    .password(etRegisterPassword.getText().toString())
                    .email(etRegisterEmail.getText().toString())
                    .build();

            serverHandler.getClient().registerPerson(new Credentials(access, identifications)).enqueue(new Callback<AjaxRs<Person>>() {
                @Override
                public void onResponse(Call<AjaxRs<Person>> call, Response<AjaxRs<Person>> response) {
                    if (!response.isSuccessful()) {
                        Snackbar.make(fabRegisterDone, "Request has failed", Snackbar.LENGTH_SHORT);
                    }
                    if (response.body().getStatus().equals(Status.SUCCESS))
                        Snackbar.make(fabRegisterDone, "Registration is successful", Snackbar.LENGTH_SHORT).show();
                    else
                        Snackbar.make(fabRegisterDone, "Registration has failed: " + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<AjaxRs<Person>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),
                            t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
