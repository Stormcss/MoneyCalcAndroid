package ru.strcss.projects.moneycalc.moneycalcandroid.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Identifications;
import ru.strcss.projects.moneycalc.enitities.Person;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.DaggerTestComponent;
import ru.strcss.projects.moneycalc.moneycalcandroid.handlers.ServerHandler;

public class RegisterActivity extends AppCompatActivity {

    @Inject
    ServerHandler serverHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DaggerTestComponent.builder().build().inject(this);

        final EditText etRegisterLogin = (EditText) findViewById(R.id.etRegisterLogin);
        final EditText etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        final EditText etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        final EditText etRegisterName = (EditText) findViewById(R.id.etRegisterName);
        final FloatingActionButton fabRegisterDone = (FloatingActionButton) findViewById(R.id.fabRegisterDone);

        fabRegisterDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        Toast.makeText(getApplicationContext(),
                                response.body().toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<AjaxRs<Person>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
