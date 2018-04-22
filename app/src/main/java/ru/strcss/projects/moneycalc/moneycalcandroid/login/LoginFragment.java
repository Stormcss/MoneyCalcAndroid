package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.Access;
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped;
import ru.strcss.projects.moneycalc.moneycalcandroid.home.HomeActivity;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UIutils.showProgress;

@ActivityScoped
public class LoginFragment extends DaggerFragment implements LoginContract.View {
    @Inject
    LoginContract.Presenter presenter;

    @Inject
    public LoginFragment() {
    }

    // UI references
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private ProgressBar progressView;
    private View mLoginFormView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_frag, container, false);

        // Set up the login form.
        mLoginView = root.findViewById(R.id.etLoginLogin);

        mPasswordView = root.findViewById(R.id.etLoginPassword);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btnLoginSignIn = root.findViewById(R.id.btnLoginSignIn);
        btnLoginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mLoginFormView = root.findViewById(R.id.login_form);
        progressView = root.findViewById(R.id.login_progress);

        return root;
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

    private void attemptLogin() {
        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Access access = Access.builder()
                    .login(login)
                    .password(password)
                    .build();

            // Show a progress spinner, and kick off a background task to
            showSpinner();
            // perform the user login attempt.
            presenter.attemptLogin(access);
        }
    }

    @Override
    public void showMainActivity() {
        Intent mainActivityIntent = new Intent(getActivity(), HomeActivity.class);
        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        mainActivityIntent.setFlags(mainActivityIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(mainActivityIntent);
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage!");
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSpinner() {
        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        showProgress(true, mLoginFormView, progressView, animTime);
    }

    @Override
    public void hideSpinner() {
        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        showProgress(false, mLoginFormView, progressView, animTime);
    }

    private boolean isLoginValid(String login) {
        //TODO: Replace this with your own logic
        return login.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }
}
