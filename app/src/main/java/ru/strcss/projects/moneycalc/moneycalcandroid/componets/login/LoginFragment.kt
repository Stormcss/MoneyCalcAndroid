package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_login
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.di.ActivityScoped
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Access
import javax.inject.Inject

@ActivityScoped
class LoginFragment @Inject
constructor() : DaggerFragment(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginContract.Presenter

    // UI references
    private var mLoginView: AutoCompleteTextView? = null
    private var mPasswordView: EditText? = null
    private var progressView: ProgressBar? = null
    private var mLoginFormView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.login_frag, container, false)

        // Set up the login form.
        mLoginView = root.findViewById(R.id.etLoginLogin)

        mPasswordView = root.findViewById(R.id.etLoginPassword)
        mPasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        val btnLoginSignIn = root.findViewById<Button>(R.id.btnLoginSignIn)
        btnLoginSignIn.setOnClickListener { attemptLogin() }

        mLoginFormView = root.findViewById(R.id.login_form)
        progressView = root.findViewById(R.id.login_progress)

        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    private fun attemptLogin() {
        // Reset errors.
        mLoginView!!.error = null
        mPasswordView!!.error = null

        // Store values at the time of the login attempt.
        val login = mLoginView!!.text.toString()
        val password = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView!!.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView!!.error = getString(R.string.error_field_required)
            focusView = mLoginView
            cancel = true
        } else if (!isLoginValid(login)) {
            mLoginView!!.error = getString(R.string.error_invalid_login)
            focusView = mLoginView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            val access = Access(login, password, null)

            // Show a progress spinner, and kick off a background task to
            showSpinner()
            // perform the user login attempt.
            presenter.attemptLogin(access)
        }
    }

    override fun showMainActivity() {
        val mainActivityIntent = Intent(activity, HomeActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainActivityIntent)
    }

    override fun showErrorMessage(msg: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showSpinner() {
        val animTime = resources.getInteger(android.R.integer.config_mediumAnimTime)
        showProgress(true, mLoginFormView, progressView, animTime)
    }

    override fun hideSpinner() {
        val animTime = resources.getInteger(android.R.integer.config_mediumAnimTime)
        showProgress(false, mLoginFormView, progressView, animTime)
    }

    override fun saveLoginToPreferences(login: String) {
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        defaultSharedPreferences.edit().putString(appl_storage_login.name, login).apply()
    }

    private fun isLoginValid(login: String): Boolean {
        return login.length > 2
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 2
    }
}
