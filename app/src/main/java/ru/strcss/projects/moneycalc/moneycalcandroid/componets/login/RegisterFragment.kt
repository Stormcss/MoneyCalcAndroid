package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import dagger.android.support.DaggerFragment
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_login
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.showProgress
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Access
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Identifications
import javax.inject.Inject

class RegisterFragment @Inject
constructor() : DaggerFragment(), RegisterContract.View {

    @Inject
    lateinit var presenter: RegisterContract.Presenter

    // UI references.
    private var registerFormView: View? = null
    private var etRegisterLogin: EditText? = null
    private var etRegisterPassword: EditText? = null
    private var etRegisterEmail: EditText? = null
    private var etRegisterName: EditText? = null
    private var fabRegisterDone: FloatingActionButton? = null
    private var progressView: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.register_frag, container, false)

        registerFormView = root.findViewById(R.id.register_form)
        etRegisterLogin = root.findViewById(R.id.etRegisterLogin)
        etRegisterPassword = root.findViewById(R.id.etRegisterPassword)
        etRegisterEmail = root.findViewById(R.id.etRegisterEmail)
        etRegisterName = root.findViewById(R.id.etRegisterName)
        fabRegisterDone = root.findViewById(R.id.fabRegisterDone)
        progressView = root.findViewById(R.id.register_progress)

        fabRegisterDone!!.setOnClickListener { attemptRegister() }
        return root
    }

    private fun attemptRegister() {
        // Reset errors.
        etRegisterLogin!!.error = null
        etRegisterPassword!!.error = null
        etRegisterEmail!!.error = null
        etRegisterName!!.error = null

        // Store values at the time of the login attempt.
        val login = etRegisterLogin!!.text.toString()
        val email = etRegisterEmail!!.text.toString()
        val password = etRegisterPassword!!.text.toString()
        val name = etRegisterName!!.text.toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(login)) {
            etRegisterLogin!!.error = getString(R.string.error_invalid_login)
            focusView = etRegisterLogin
            cancel = true
        } else if (TextUtils.isEmpty(password)) {
            etRegisterPassword!!.error = getString(R.string.error_invalid_password)
            focusView = etRegisterPassword
            cancel = true
        } else if (TextUtils.isEmpty(email)) {
            etRegisterEmail!!.error = getString(R.string.error_field_required)
            focusView = etRegisterEmail
            cancel = true
        } else if (TextUtils.isEmpty(name)) {
            etRegisterName!!.error = getString(R.string.error_field_required)
            focusView = etRegisterName
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            println("generating Credentials")
            val access = Access(login, password, email)

            // Show a progress spinner, and kick off a background task to
            //            showProgress(true);
            // perform the user login attempt.
            presenter.attemptRegister(Credentials(access, Identifications(null, name)))
            showSpinner()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun showMainActivity() {
        val mainActivityIntent = Intent(activity, HomeActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivityForResult(mainActivityIntent, 0)
    }

    override fun showErrorMessage(msg: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun showSpinner() {
        val animTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        showProgress(true, registerFormView, progressView, animTime)
    }

    override fun hideSpinner() {
        val animTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        showProgress(false, registerFormView, progressView, animTime)
    }

    override fun saveLoginToPreferences(login: String) {
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        defaultSharedPreferences.edit().putString(appl_storage_login.name, login).apply()
    }
}
