package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import retrofit2.Response
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.entities.Access
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO) : LoginContract.Presenter {

    @Inject
    lateinit var dataStorage: DataStorage

    private var loginView: LoginContract.View? = null

    override fun attemptLogin(access: Access) {
        moneyCalcServerDAO.login(access)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<Void>> {
                    override fun onCompleted() {
                        println("completed")
                    }

                    override fun onError(ex: Throwable) {
                        loginView!!.hideSpinner()
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(rs: Response<Void>) {
                        val token = rs.headers().get("Authorization")
                        if (token != null) {
                            moneyCalcServerDAO.token = token
                            dataStorage.activeUserData.userLogin = access.login
                            loginView!!.saveLoginToPreferences(access.login)
                            println("token = " + moneyCalcServerDAO.token!!)
                            loginView!!.showMainActivity()
                        } else {
                            loginView!!.showErrorMessage("Bad credentials")
                        }
                        loginView!!.hideSpinner()
                    }
                })
    }

    override fun takeView(view: LoginContract.View) {
        loginView = view
    }

    override fun dropView() {
        loginView = null
    }
}
