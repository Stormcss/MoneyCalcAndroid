package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.dto.Credentials;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.enitities.Identifications;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UIutils.showProgress;

public class RegisterFragment extends DaggerFragment implements RegisterContract.View {

    @Inject
    RegisterContract.Presenter presenter;

    // UI references.
    private View registerFormView;
    private EditText etRegisterLogin;
    private EditText etRegisterPassword;
    private EditText etRegisterEmail;
    private EditText etRegisterName;
    private FloatingActionButton fabRegisterDone;
    private ProgressBar progressView;

    @Inject
    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_frag, container, false);

        registerFormView = root.findViewById(R.id.register_form);
        etRegisterLogin = root.findViewById(R.id.etRegisterLogin);
        etRegisterPassword = root.findViewById(R.id.etRegisterPassword);
        etRegisterEmail = root.findViewById(R.id.etRegisterEmail);
        etRegisterName = root.findViewById(R.id.etRegisterName);
        fabRegisterDone = root.findViewById(R.id.fabRegisterDone);
        progressView = root.findViewById(R.id.register_progress);

        fabRegisterDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        return root;
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
            etRegisterLogin.setError(getString(R.string.error_invalid_login));
            focusView = etRegisterLogin;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError(getString(R.string.error_invalid_password));
            focusView = etRegisterPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            etRegisterEmail.setError(getString(R.string.error_field_required));
            focusView = etRegisterEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(name)) {
            etRegisterName.setError(getString(R.string.error_field_required));
            focusView = etRegisterName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            System.out.println("generating Credentials");
            Access access = Access.builder()
                    .login(login)
                    .password(password)
                    .email(email)
                    .build();

            // Show a progress spinner, and kick off a background task to
//            showProgress(true);
            // perform the user login attempt.
            presenter.attemptRegister(new Credentials(access, Identifications.builder().name(name).build()));
            showSpinner();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }

    @Override
    public void showMainActivity() {
        Intent mainActivityIntent = new Intent(getActivity(), HomeActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(mainActivityIntent, 0);
    }

    @Override
    public void showErrorMessage(String msg) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSpinner() {
        int animTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        showProgress(true, registerFormView, progressView, animTime);
    }

    @Override
    public void hideSpinner() {
        int animTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        showProgress(false, registerFormView, progressView, animTime);
    }
}
